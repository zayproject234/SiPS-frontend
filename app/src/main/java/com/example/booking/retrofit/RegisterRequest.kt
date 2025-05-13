package com.example.booking.retrofit

data class RegisterRequest(
    val username: String,
    val email: String,
    val password: String,
    val phoneNumber: String
)