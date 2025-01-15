package com.example.booking.retrofit

import okhttp3.MultipartBody
import retrofit2.Call
import retrofit2.http.Body
import retrofit2.http.Header
import retrofit2.http.Multipart
import retrofit2.http.POST
import retrofit2.http.Part

interface ApiServices {
    @POST("register")
    fun register(@Body request: RegisterRequest): Call<RegisterResponse>

    @POST("login")
    fun login(@Body request: LoginRequest): Call<LoginResponse>

    @Multipart
    @POST("bookings")
    fun booking(
        @Header("Authorization") token: String, // Add the Authorization header
        @Part("bookingRequest") bookingRequest: BookingRequest, // Sending the booking data as a JSON in the body
        @Part paymentProof: MultipartBody.Part // Sending the image file as a multipart part
    ): Call<BookingResponse>

}
