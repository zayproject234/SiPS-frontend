package com.example.booking.ui.home_admin

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.example.booking.R

class HomeAdminActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_home_admin)

        // Menampilkan HomeAdminFragment ke dalam activity
        supportFragmentManager.beginTransaction()
            .replace(R.id.fragmentContainer, HomeAdminFragment())
            .commit()
    }
}
