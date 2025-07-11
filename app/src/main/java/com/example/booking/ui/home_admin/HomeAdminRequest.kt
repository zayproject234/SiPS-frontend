package com.example.booking.ui.home_admin

import android.os.Bundle
import android.view.View
import android.widget.Button
import android.widget.FrameLayout
import android.widget.LinearLayout
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.booking.ConfirmRejectFragment
import com.example.booking.R
import com.example.booking.ui.profile_admin.ProfileAdminFragment

class HomeAdminRequest : AppCompatActivity() {

    private lateinit var recyclerView: RecyclerView
    private lateinit var adapter: BookingAdapter
    private lateinit var homeAdminLayout: LinearLayout
    private lateinit var fragmentContainer: FrameLayout

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_home_admin_request)

        recyclerView = findViewById(R.id.rvBookings)
        homeAdminLayout = findViewById(R.id.homeAdminLayout)
        fragmentContainer = findViewById(R.id.adminFragmentContainer)
        recyclerView.layoutManager = LinearLayoutManager(this)

        val bookings = listOf(
            Booking("1", "Rabu, 14 Des 2024", "18.00 - 21.00", "pending"),
            Booking("2", "Rabu, 14 Des 2024", "19.00 - 22.00", "accepted"),
            Booking("3", "Kamis, 15 Des 2024", "17.00 - 20.00", "rejected")
        )

        adapter = BookingAdapter(bookings) { booking ->
            val fragment = ConfirmRejectFragment.newInstance(
                booking.id, booking.date, booking.time
            )
            showFragment(fragment)
        }

        recyclerView.adapter = adapter

        val editAdminBtn = findViewById<Button>(R.id.btneditadmin)
        editAdminBtn.setOnClickListener {
            val fragment = ProfileAdminFragment()
            showFragment(fragment)
        }
    }

    private fun showFragment(fragment: androidx.fragment.app.Fragment) {
        supportFragmentManager.beginTransaction()
            .replace(R.id.adminFragmentContainer, fragment)
            .addToBackStack(null)
            .commit()
        homeAdminLayout.visibility = View.GONE
        fragmentContainer.visibility = View.VISIBLE
    }
}
