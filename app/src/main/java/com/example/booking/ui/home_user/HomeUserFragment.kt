package com.example.booking.ui.home_user

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.booking.databinding.FragmentHomeUserBinding
import java.text.SimpleDateFormat
import java.util.*

// TODO: Replace with your actual adapter implementation or import
class ScheduleAdapter : androidx.recyclerview.widget.ListAdapter<com.example.booking.retrofit.DataBooking, androidx.recyclerview.widget.RecyclerView.ViewHolder>(
    object : androidx.recyclerview.widget.DiffUtil.ItemCallback<com.example.booking.retrofit.DataBooking>() {
        override fun areItemsTheSame(oldItem: com.example.booking.retrofit.DataBooking, newItem: com.example.booking.retrofit.DataBooking) = oldItem.id == newItem.id
        override fun areContentsTheSame(oldItem: com.example.booking.retrofit.DataBooking, newItem: com.example.booking.retrofit.DataBooking) = oldItem == newItem
    }
) {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): androidx.recyclerview.widget.RecyclerView.ViewHolder {
        // Implement your ViewHolder here
        throw NotImplementedError("Implement your ViewHolder")
    }
    override fun onBindViewHolder(holder: androidx.recyclerview.widget.RecyclerView.ViewHolder, position: Int) {
        // Bind your data here
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

        scheduleAdapter = ScheduleAdapter()
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