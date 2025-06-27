package com.example.booking.ui.home_admin

import android.os.Bundle
import android.widget.Button
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.booking.R

class HomeAdminRequest : AppCompatActivity() {

    private lateinit var recyclerView: RecyclerView
    private lateinit var adapter: BookingAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_home_admin_request)

        val bookings = listOf(
            Booking("1", "Rabu, 14 Des 2024", "18.00 - 21.00"),
            Booking("2", "Rabu, 14 Des 2024", "18.00 - 21.00"),
            Booking("3", "Rabu, 14 Des 2024", "18.00 - 21.00"),
            Booking("4", "Rabu, 14 Des 2024", "18.00 - 21.00"),
            Booking("5", "Rabu, 14 Des 2024", "18.00 - 21.00")
        )

        recyclerView = findViewById(R.id.rvBookings)
        recyclerView.layoutManager = LinearLayoutManager(this)

        adapter = BookingAdapter(bookings) { booking ->
            Toast.makeText(this, "Detail: ${booking.id}", Toast.LENGTH_SHORT).show()
        }

        recyclerView.adapter = adapter

        val scheduleBtn = findViewById<Button>(R.id.btnSchedule)
        scheduleBtn.setOnClickListener {
            Toast.makeText(this, "Menuju halaman schedule", Toast.LENGTH_SHORT).show()
        }
    }
}
