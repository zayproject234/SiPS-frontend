package com.example.booking

import android.content.Intent
import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.example.booking.databinding.ActivityRegisterBinding
import com.example.booking.retrofit.ApiConfig
import com.example.booking.retrofit.RegisterRequest
import com.example.booking.retrofit.RegisterResponse
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class RegisterActivity : AppCompatActivity() {
    lateinit var binding: ActivityRegisterBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityRegisterBinding.inflate(layoutInflater)
        setContentView(binding.root)

        // Navigasi ke halaman login
        binding.tvBackToLogin.setOnClickListener {
            finish() // Kembali ke halaman sebelumnya (LoginActivity)
        }

        // Handle klik tombol register
        binding.btnRegisterSubmit.setOnClickListener {
            val username = binding.etUsernameRegister.text.toString().trim()
            val phone = binding.etPhoneRegister.text.toString().trim()
            val email = binding.etPasswordRegister.text.toString().trim()
            val password = binding.etConfirmPasswordRegister.text.toString().trim()

            // Validasi input
            if (username.isEmpty() || phone.isEmpty() || email.isEmpty() || password.isEmpty()) {
                Toast.makeText(this, "Semua field harus diisi!", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }

            // Buat objek request
            val request = RegisterRequest(
                username = username,
                email = email,
                password = password,
                phoneNumber = phone
            )

            // Panggil API
            registerUser(request)
        }
    }

    private fun registerUser(request: RegisterRequest) {
        val apiService = ApiConfig.getApiService()
        val call = apiService.register(request)

        call.enqueue(object : Callback<RegisterResponse> {
            override fun onResponse(
                call: Call<RegisterResponse>,
                response: Response<RegisterResponse>
            ) {
                if (response.isSuccessful) {
                    val responseBody = response.body()
                    if (responseBody != null) {
                        Toast.makeText(
                            this@RegisterActivity,
                            "Registrasi berhasil: ${responseBody.message}",
                            Toast.LENGTH_SHORT
                        ).show()
                        navigateToLoginActivity()
                        finish()
                    } else {
                        Toast.makeText(
                            this@RegisterActivity,
                            "Registrasi gagal: ${responseBody?.message}",
                            Toast.LENGTH_SHORT
                        ).show()
                    }
                } else {
                    Toast.makeText(
                        this@RegisterActivity,
                        "Registrasi gagal: ${response.message()}",
                        Toast.LENGTH_SHORT
                    ).show()
                }
            }

            override fun onFailure(call: Call<RegisterResponse>, t: Throwable) {
                Toast.makeText(
                    this@RegisterActivity,
                    "Error: ${t.message}",
                    Toast.LENGTH_SHORT
                ).show()
            }
        })
    }
    private fun navigateToLoginActivity() {
        val intent = Intent(this, LoginActivity::class.java)
        startActivity(intent)
    }
}
