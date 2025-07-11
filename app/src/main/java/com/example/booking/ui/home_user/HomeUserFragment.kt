package com.example.booking.ui.home_user

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.navigation.fragment.findNavController
import com.example.booking.R
import com.example.booking.databinding.FragmentHomeUserBinding
import java.text.SimpleDateFormat
import java.util.*

// TODO: Replace with your actual adapter implementation or import
class ScheduleAdapter(
    private val onScheduleClick: (com.example.booking.retrofit.DataBooking) -> Unit
) : androidx.recyclerview.widget.ListAdapter<com.example.booking.retrofit.DataBooking, ScheduleAdapter.ScheduleViewHolder>(

    object : androidx.recyclerview.widget.DiffUtil.ItemCallback<com.example.booking.retrofit.DataBooking>() {
        override fun areItemsTheSame(oldItem: com.example.booking.retrofit.DataBooking, newItem: com.example.booking.retrofit.DataBooking) =
            oldItem.id == newItem.id

        override fun areContentsTheSame(oldItem: com.example.booking.retrofit.DataBooking, newItem: com.example.booking.retrofit.DataBooking) =
            oldItem == newItem
    }
) {
    inner class ScheduleViewHolder(itemView: View) : androidx.recyclerview.widget.RecyclerView.ViewHolder(itemView) {
        val tvBookingDate: TextView = itemView.findViewById(R.id.tvBookingDate)
        val tvBookingTime: TextView = itemView.findViewById(R.id.tvBookingTime)
        val tvDetailBooking: TextView = itemView.findViewById(R.id.tvDetailBooking)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ScheduleViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.item_schedule, parent, false)
        return ScheduleViewHolder(view)
    }

    override fun onBindViewHolder(holder: ScheduleViewHolder, position: Int) {
        val schedule = getItem(position)

        // Format waktu dan tanggal
        val inputFormat = SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSS'Z'", Locale.getDefault())
        inputFormat.timeZone = TimeZone.getTimeZone("UTC")

        val outputDate = SimpleDateFormat("EEEE, dd MMM yyyy", Locale("id", "ID"))
        val outputTime = SimpleDateFormat("HH.mm", Locale("id", "ID"))

        schedule.createdAt?.let { createdAt ->
            try {
                val date = inputFormat.parse(createdAt)

                val startCal = Calendar.getInstance().apply { time = date!! }
                val endCal = Calendar.getInstance().apply { time = date; add(Calendar.HOUR_OF_DAY, schedule.duration ?: 0) }

                val formattedDate = outputDate.format(startCal.time)
                val startTime = outputTime.format(startCal.time)
                val endTime = outputTime.format(endCal.time)

                holder.tvBookingDate.text = "Booking ID - $formattedDate"
                holder.tvBookingTime.text = "$startTime - $endTime"
            } catch (e: Exception) {
                holder.tvBookingDate.text = "Format tanggal salah"
                holder.tvBookingTime.text = "-"
            }
        }

        holder.tvDetailBooking.setOnClickListener {
            onScheduleClick(schedule)
        }
    }
}



class HomeFragment : Fragment() {

    private var _binding: FragmentHomeUserBinding? = null
    private val binding get() = _binding!!
    private lateinit var homeUserViewModel: HomeUserViewModel
    private lateinit var scheduleAdapter: ScheduleAdapter

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentHomeUserBinding.inflate(inflater, container, false)
        homeUserViewModel = ViewModelProvider(this)[HomeUserViewModel::class.java]

        val sharedPreferences = requireContext().getSharedPreferences("LoginPrefs", Context.MODE_PRIVATE)
        val userName = sharedPreferences.getString("userName", "User")
        val authToken = sharedPreferences.getString("authToken", null)

        binding.userName.text = userName

        val dateFormat = SimpleDateFormat("EEEE, dd MMM yyyy", Locale("id", "ID"))
        binding.dateTime.text = dateFormat.format(Date())

        scheduleAdapter = ScheduleAdapter { schedule ->
            val sharedPreferences = requireContext().getSharedPreferences("LoginPrefs", Context.MODE_PRIVATE)
            val userId = sharedPreferences.getInt("userId", -1)
            val authToken = sharedPreferences.getString("authToken", null)

            val bundle = Bundle().apply {
                putInt("scheduleId", schedule.id ?: -1)
                putInt("userId", userId)
                putString("authToken", authToken)
                putString("bandName", schedule.bandName ?: "")
            }

            findNavController().navigate(R.id.action_bookingFragment_to_paymentFragment, bundle)
        }

        binding.recyclerView.layoutManager = LinearLayoutManager(requireContext())
        binding.recyclerView.adapter = scheduleAdapter

        homeUserViewModel.schedules.observe(viewLifecycleOwner) { schedules ->
            scheduleAdapter.submitList(schedules)
        }

        homeUserViewModel.error.observe(viewLifecycleOwner) { errorMsg ->
            if (errorMsg.isNotEmpty()) {
                Toast.makeText(requireContext(), errorMsg, Toast.LENGTH_SHORT).show()
            }
        }

        // Only fetch schedules if token is available
        if (!authToken.isNullOrEmpty()) {
            homeUserViewModel.fetchSchedules(authToken)
        } else {
            Toast.makeText(requireContext(), "No auth token found", Toast.LENGTH_SHORT).show()
        }

        binding.button.setOnClickListener {
            val intent = Intent(requireContext(), MainActivityBooking::class.java)
            startActivity(intent)
        }

        return binding.root
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}