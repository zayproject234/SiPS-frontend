package com.example.booking

import android.content.Intent
import android.content.SharedPreferences
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import com.example.booking.databinding.ActivityLoginBinding
import com.example.booking.retrofit.ApiConfig
import com.example.booking.retrofit.LoginRequest
import com.example.booking.retrofit.LoginResponse
import com.example.booking.ui.home_admin.HomeAdminActivity
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class LoginActivity : AppCompatActivity() {
    private lateinit var binding: ActivityLoginBinding
    private lateinit var sharedPreferences: SharedPreferences

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityLoginBinding.inflate(layoutInflater)
        setContentView(binding.root)

        sharedPreferences = getSharedPreferences("LoginPrefs", MODE_PRIVATE)

        binding.btnLogin.setOnClickListener {
            val username = binding.etUsername.text.toString()
            val password = binding.etPassword.text.toString()

            if (username.isNotEmpty() && password.isNotEmpty()) {
                // Cek manual admin
                if (username == "admin" && password == "admin123") {
                    sharedPreferences.edit().putBoolean("isLoggedIn", true).apply()
                    sharedPreferences.edit().putString("role", "admin").apply()

                    startActivity(Intent(this, HomeAdminActivity::class.java))
                    finish()
                } else {
                    // Login user biasa ke server
                    val request = LoginRequest(username = username, password = password)
                    performLogin(request)
                }
            } else {
                Toast.makeText(this, "Please fill in all fields", Toast.LENGTH_SHORT).show()
            }
        }

        binding.btnRegister.setOnClickListener {
            startActivity(Intent(this, RegisterActivity::class.java))
        }
    }

    private fun performLogin(request: LoginRequest) {
        val apiService = ApiConfig.getApiService()
        val call = apiService.login(request)

        call.enqueue(object : Callback<LoginResponse> {
            override fun onResponse(call: Call<LoginResponse>, response: Response<LoginResponse>) {
                if (response.isSuccessful) {
                    val responseBody = response.body()
                    if (responseBody != null) {
                        Toast.makeText(this@LoginActivity, "Login berhasil", Toast.LENGTH_SHORT).show()

                        // Simpan token & info user
                        sharedPreferences.edit().apply {
                            putBoolean("isLoggedIn", true)
                            putString("authToken", responseBody.data?.token ?: "")
                            putInt("userId", responseBody.data?.id ?: -1)
                            putString("role", "user") // default role
                            apply()
                        }

                        // Arahkan ke MainActivity untuk user biasa
                        startActivity(Intent(this@LoginActivity, MainActivity::class.java))
                        finish()
                    } else {
                        Toast.makeText(this@LoginActivity, "Login gagal", Toast.LENGTH_SHORT).show()
                    }
                } else {
                    Toast.makeText(this@LoginActivity, "Login error: ${response.message()}", Toast.LENGTH_SHORT).show()
                }
            }

            override fun onFailure(call: Call<LoginResponse>, t: Throwable) {
                Toast.makeText(this@LoginActivity, "Error: ${t.message}", Toast.LENGTH_SHORT).show()
            }
        })
    }
}
