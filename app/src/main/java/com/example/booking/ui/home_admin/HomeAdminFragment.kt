package com.example.booking.ui.home_admin

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import com.example.booking.databinding.FragmentHomeAdminBinding

class HomeAdminFragment : Fragment() {

    private var _binding: FragmentHomeAdminBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentHomeAdminBinding.inflate(inflater, container, false)
        val root: View = binding.root

        // Tombol Book Requests
        binding.btnBookRequests.setOnClickListener {
            val intent = Intent(requireContext(), HomeAdminRequest::class.java)
            startActivity(intent)
        }

        // Booking slot click example
        binding.slot09.setOnClickListener {
            Toast.makeText(requireContext(), "Slot 09.00 clicked", Toast.LENGTH_SHORT).show()
        }

        binding.slot10.setOnClickListener {
            Toast.makeText(requireContext(), "Slot 10.00 clicked", Toast.LENGTH_SHORT).show()
        }

        binding.slot11.setOnClickListener {
            Toast.makeText(requireContext(), "Slot 11.00 clicked", Toast.LENGTH_SHORT).show()
        }

        binding.slot12.setOnClickListener {
            Toast.makeText(requireContext(), "Slot 12.00 clicked", Toast.LENGTH_SHORT).show()
        }

        binding.slot13.setOnClickListener {
            Toast.makeText(requireContext(), "Slot 13.00 clicked", Toast.LENGTH_SHORT).show()
        }

        binding.slot14.setOnClickListener {
            Toast.makeText(requireContext(), "Slot 14.00 clicked", Toast.LENGTH_SHORT).show()
        }

        return root
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
