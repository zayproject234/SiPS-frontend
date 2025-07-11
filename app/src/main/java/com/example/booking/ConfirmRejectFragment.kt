package com.example.booking

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.fragment.app.Fragment

class ConfirmRejectFragment : Fragment() {

    private var bookingId: String? = null
    private var bookingDate: String? = null
    private var bookingTime: String? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
            bookingId = it.getString(ARG_BOOKING_ID)
            bookingDate = it.getString(ARG_DATE)
            bookingTime = it.getString(ARG_TIME)
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_confirm_reject_admin, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        // Set data ke TextView sesuai ID-nya
        view.findViewById<TextView>(R.id.transactionIdAdminTextView)?.text = bookingId ?: "-"
        view.findViewById<TextView>(R.id.dateAdminTextView)?.text = bookingDate ?: "-"
        view.findViewById<TextView>(R.id.startAdminTextView)?.text = bookingTime ?: "-"
    }

    companion object {
        private const val ARG_BOOKING_ID = "booking_id"
        private const val ARG_DATE = "booking_date"
        private const val ARG_TIME = "booking_time"

        @JvmStatic
        fun newInstance(id: String, date: String, time: String) =
            ConfirmRejectFragment().apply {
                arguments = Bundle().apply {
                    putString(ARG_BOOKING_ID, id)
                    putString(ARG_DATE, date)
                    putString(ARG_TIME, time)
                }
            }
    }
}
