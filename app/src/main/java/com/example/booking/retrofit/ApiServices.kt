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
        @Header("Authorization") token: String,
        @Part("userId") userId: Int,
        @Part("scheduleId") scheduleId: Int,
        @Part("bandName") bandName: String,
        @Part("duration") duration: Int,
        @Part("totalPrice") totalPrice: Int,
        @Part paymentProof: MultipartBody.Part,
        @Part("notes") notes: String
    ): Call<BookingResponse>
}