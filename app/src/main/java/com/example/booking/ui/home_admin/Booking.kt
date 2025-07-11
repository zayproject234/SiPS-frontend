package com.example.booking.ui.home_admin

data class Booking(
    val id: String,
    val date: String,
    val time: String,
    val status: String = "pending" // ⬅️ nilai default
)
