package com.example.booking.ui.home_user

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.booking.retrofit.ApiConfig
import com.example.booking.retrofit.BookingResponse
import com.example.booking.retrofit.DataBooking
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class HomeUserViewModel : ViewModel() {

    private val _schedules = MutableLiveData<List<DataBooking>>()
    val schedules: LiveData<List<DataBooking>> = _schedules

    private val _error = MutableLiveData<String>("")
    val error: LiveData<String> = _error

    fun fetchSchedules(token: String) {
        val apiService = ApiConfig.getApiService()
        apiService.getBookings("Bearer $token").enqueue(object : Callback<BookingResponse> {
            override fun onResponse(
                call: Call<BookingResponse>,
                response: Response<BookingResponse>
            ) {
                if (response.isSuccessful) {
                    val bookings = response.body()?.data
                    _schedules.value = bookings ?: emptyList()
                } else {
                    _error.value = "Failed to load schedules: ${response.message()}"
                }
            }

            override fun onFailure(call: Call<BookingResponse>, t: Throwable) {
                _error.value = "Failed to load schedules: ${t.message}"
            }
        })
    }
}