package com.example.booking.ui.home_admin

import android.graphics.Color
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.booking.R

class BookingAdapter(
    private val bookings: List<Booking>,
    private val onDetailClick: (Booking) -> Unit
) : RecyclerView.Adapter<BookingAdapter.BookingViewHolder>() {

    inner class BookingViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val tvBookingId: TextView = itemView.findViewById(R.id.tvBookingId)
        val tvBookingTime: TextView = itemView.findViewById(R.id.tvBookingTime)
        val btnDetail: Button = itemView.findViewById(R.id.btnDetail)
        val container: View = itemView.findViewById(R.id.itemContainer)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): BookingViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_booking_admin, parent, false) // ✅ disesuaikan
        return BookingViewHolder(view)
    }

    override fun onBindViewHolder(holder: BookingViewHolder, position: Int) {
        val booking = bookings[position]
        holder.tvBookingId.text = "Booking ID - ${booking.date}"
        holder.tvBookingTime.text = booking.time
        holder.btnDetail.setOnClickListener { onDetailClick(booking) }

        // Logika warna berdasarkan status
        val bgColor = when (booking.status.lowercase()) {
            "accepted" -> "#4CAF50"
            "rejected" -> "#F44336"
            else -> "#A1887F"
        }
        holder.container.setBackgroundColor(Color.parseColor(bgColor))
    }

    override fun getItemCount(): Int = bookings.size
}
