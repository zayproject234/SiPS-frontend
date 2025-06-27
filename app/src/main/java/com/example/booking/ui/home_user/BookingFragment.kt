package com.example.booking.ui.home_user

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.example.booking.R
import java.text.NumberFormat
import java.util.Locale

class BookingFragment : Fragment() {

    private var totalTime = 1
    private val pricePerHour = 100_000

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_booking, container, false)

        val orderNameEditText = view.findViewById<EditText>(R.id.orderName)
        val bandNameEditText = view.findViewById<EditText>(R.id.bandName)
        val noteEditText = view.findViewById<EditText>(R.id.note)
        val totalTimeTextBooking = view.findViewById<TextView>(R.id.totalTimeTextBooking)
        val minusButton = view.findViewById<Button>(R.id.minusButton)
        val plusButton = view.findViewById<Button>(R.id.plusButton)
        val continueButton = view.findViewById<Button>(R.id.continueButton)

        fun updatePriceButton() {
            val totalPrice = totalTime * pricePerHour
            val formattedPrice = NumberFormat.getCurrencyInstance(Locale("in", "ID")).format(totalPrice)
            continueButton.text = "continue $formattedPrice"
        }

        totalTimeTextBooking.text = totalTime.toString()
        updatePriceButton()

        minusButton.setOnClickListener {
            if (totalTime > 1) {
                totalTime--
                totalTimeTextBooking.text = totalTime.toString()
                updatePriceButton()
            } else {
                Toast.makeText(context, "Minimum time is 1 hour", Toast.LENGTH_SHORT).show()
            }
        }

        plusButton.setOnClickListener {
            totalTime++
            totalTimeTextBooking.text = totalTime.toString()
            updatePriceButton()
        }

        continueButton.setOnClickListener {
            val orderName = orderNameEditText.text.toString().trim()
            val bandName = bandNameEditText.text.toString().trim()
            val note = noteEditText.text.toString().trim()

            if (orderName.isNotEmpty() && bandName.isNotEmpty()) {
                val sharedPreferences = requireContext().getSharedPreferences("LoginPrefs", Context.MODE_PRIVATE)
                val userId = sharedPreferences.getInt("userId", -1)
                val authToken = sharedPreferences.getString("authToken", null)

                val bundle = Bundle().apply {
                    putString("orderName", orderName)
                    putString("bandName", bandName)
                    putString("note", note)
                    putInt("totalTime", totalTime)
                    putInt("userId", userId)
                    putString("authToken", authToken)
                }
                findNavController().navigate(R.id.action_bookingFragment_to_paymentFragment, bundle)
            } else {
                Toast.makeText(context, "Please fill out all required fields", Toast.LENGTH_SHORT).show()
            }
        }

        return view
    }
}