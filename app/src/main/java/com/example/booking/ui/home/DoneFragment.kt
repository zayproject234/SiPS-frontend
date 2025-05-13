package com.example.booking.ui.home

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.TextView
import androidx.fragment.app.Fragment
import com.example.booking.MainActivity
import com.example.booking.R
import java.util.UUID

class DoneFragment : Fragment() {

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        val view = inflater.inflate(R.layout.fragment_done, container, false)

        // Retrieve arguments with default fallback
        val transactionId = generateTransactionId()
        val orderName = arguments?.getString("orderName") ?: "N/A"
        val bandName = arguments?.getString("bandName") ?: "N/A"
        val totalTime = arguments?.getString("totalTime") ?: "N/A"
        val totalPayment = arguments?.getString("totalPayment") ?: "N/A"

        // Bind views
//        val transactionIdTextView = view.findViewById<TextView>(R.id.transactionIdTextView)
        val orderNameTextView = view.findViewById<TextView>(R.id.orderNameTextView)
        val bandNameTextView = view.findViewById<TextView>(R.id.bandNameTextView)
        val totalTimeTextView = view.findViewById<TextView>(R.id.totalTimeTextView)
        val totalPaymentTextView = view.findViewById<TextView>(R.id.totalPaymentTextView)
        val finishButton = view.findViewById<Button>(R.id.finishButton)

        // Set values dynamically
//        transactionIdTextView.text = transactionId
        orderNameTextView.text = orderName
        bandNameTextView.text = bandName
        totalTimeTextView.text = totalTime
        totalPaymentTextView.text = totalPayment

        // Handle "Finish" button click
        finishButton.setOnClickListener {
            val intent = Intent(requireContext(), MainActivity::class.java)
            startActivity(intent)
        }




        return view
    }
    fun generateTransactionId(): String {
        return UUID.randomUUID().toString()
    }
}