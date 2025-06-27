package com.example.booking.ui.home_user

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.booking.retrofit.ApiConfig
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
        apiService.getBookings("Bearer $token").enqueue(object : Callback<List<DataBooking>> {
            override fun onResponse(
                call: Call<List<DataBooking>>,
                response: Response<List<DataBooking>>
            ) {
                if (response.isSuccessful) {
                    _schedules.value = response.body() ?: listOf()
                } else {
                    _error.value = "Failed to load schedules"
                }
            }

            override fun onFailure(call: Call<List<DataBooking>>, t: Throwable) {
                _error.value = "Failed to load schedules: ${t.message}"
            }
        })
    }
}