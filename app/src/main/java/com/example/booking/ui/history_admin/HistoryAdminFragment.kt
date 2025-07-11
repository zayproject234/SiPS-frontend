package com.example.booking.ui.history_admin

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.booking.databinding.FragmentHistoryAdminBinding
import com.example.booking.ui.home_admin.Booking
import com.example.booking.ui.home_admin.BookingAdapter

class HistoryAdminFragment : Fragment() {

    private var _binding: FragmentHistoryAdminBinding? = null
    private val binding get() = _binding!!

    private lateinit var adapter: BookingAdapter

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentHistoryAdminBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        // Dummy data history booking
        val historyBookings = listOf(
            Booking("1", "Senin, 17 Jun 2024", "10.00 - 12.00", "accepted"),
            Booking("2", "Selasa, 18 Jun 2024", "13.00 - 15.00", "rejected"),
            Booking("3", "Rabu, 19 Jun 2024", "15.00 - 17.00", "accepted"),
            Booking("4", "Kamis, 20 Jun 2024", "09.00 - 11.00", "rejected")
        )

        adapter = BookingAdapter(historyBookings) { booking ->
            Toast.makeText(requireContext(), "Detail Booking ID: ${booking.id}", Toast.LENGTH_SHORT).show()
        }

        binding.rvHistory.layoutManager = LinearLayoutManager(requireContext())
        binding.rvHistory.adapter = adapter
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
