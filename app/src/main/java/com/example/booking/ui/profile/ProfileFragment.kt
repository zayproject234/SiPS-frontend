package com.example.booking.ui.profile

import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import com.example.booking.LoginActivity
import com.example.booking.databinding.FragmentProfileUserBinding

class ProfileFragment : Fragment() {

    private var _binding: FragmentProfileUserBinding? = null

    // This property is only valid between onCreateView and
    // onDestroyView.
    private val binding get() = _binding!!
    private lateinit var sharedPreferences: SharedPreferences

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        val profileViewModel =
            ViewModelProvider(this).get(ProfileViewModel::class.java)

        _binding = FragmentProfileUserBinding.inflate(inflater, container, false)
        val root: View = binding.root

        sharedPreferences = requireContext().getSharedPreferences("LoginPrefs", Context.MODE_PRIVATE)

        val textView: TextView = binding.textProfile
        profileViewModel.text.observe(viewLifecycleOwner) {
            textView.text = it
        }

        binding.btnLogout.setOnClickListener {
            // clear shared preferences
            sharedPreferences.edit().clear().apply()
            val intent = Intent(activity, LoginActivity::class.java)
            intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
            startActivity(intent)
            activity?.finish()
        }
        return root
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}