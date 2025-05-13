package com.example.booking.ui.history

import android.content.Context
import android.graphics.Color
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.BaseAdapter
import android.widget.ListAdapter
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import com.example.booking.databinding.FragmentHistoryBinding

class HistoryFragment : Fragment() {

    private var _binding: FragmentHistoryBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        // Initialize ViewModel
        val historyViewModel = ViewModelProvider(this).get(HistoryViewModel::class.java)

        // Inflate the layout using View Binding
        _binding = FragmentHistoryBinding.inflate(inflater, container, false)
        val root: View = binding.root

        // Initialize the ListView
        val bookings = listOf(
            Booking("Rabu, 14 Des 2024", "18.00 - 21.00", true),
            Booking("Rabu, 14 Des 2024", "18.00 - 21.00", false),
            Booking("Rabu, 14 Des 2024", "18.00 - 21.00", true)
        )

        // Set up the custom adapter
        val adapter = BookingListAdapter(requireContext(), bookings)
        binding.listView.adapter = adapter

        // Observe ViewModel for other data if necessary
        historyViewModel.text.observe(viewLifecycleOwner) { text ->
            binding.textHistory.text = text
        }

        return root
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    // Data class to represent a Booking
    data class Booking(val id: String, val time: String, val isApproved: Boolean)

    // Custom adapter for ListView
    inner class BookingListAdapter(
        private val context: Context,
        private val bookings: List<Booking>
    ) : BaseAdapter(), ListAdapter {

        override fun getCount(): Int = bookings.size
        override fun getItem(position: Int): Any = bookings[position]
        override fun getItemId(position: Int): Long = position.toLong()

        override fun getView(position: Int, convertView: View?, parent: ViewGroup?): View {
            val view = convertView ?: LayoutInflater.from(context).inflate(
                android.R.layout.simple_list_item_2, parent, false
            )

            // Populate the default Android layout with booking data
            val booking = bookings[position]
            val text1 = view.findViewById<TextView>(android.R.id.text1)
            val text2 = view.findViewById<TextView>(android.R.id.text2)

            text1.text = "Booking ID: ${booking.id}"
            text2.text = booking.time

            // Set background color based on approval status
            view.setBackgroundColor(
                if (booking.isApproved) Color.parseColor("#006400")
                else Color.parseColor("#8b0000")
            )

            // Set the text color to white
            text1.setTextColor(Color.parseColor("#FFFFFF"))
            text2.setTextColor(Color.parseColor("#FFFFFF"))

            return view
        }
    }
}