package com.example.booking.ui.home_admin

import android.os.Bundle
import android.widget.Button
import android.widget.ImageView
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.example.booking.R

class HomeDetailAdmin : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_home_detail_admin)

        val btnConfirm = findViewById<Button>(R.id.btnConfirm)
        val btnReject = findViewById<Button>(R.id.btnReject)
        val imgProof = findViewById<ImageView>(R.id.imgProof)

        btnConfirm.setOnClickListener {
            Toast.makeText(this, "Booking dikonfirmasi", Toast.LENGTH_SHORT).show()
        }

        btnReject.setOnClickListener {
            Toast.makeText(this, "Booking ditolak", Toast.LENGTH_SHORT).show()
        }

        imgProof.setOnClickListener {
            Toast.makeText(this, "Bukti pembayaran ditampilkan (belum diimplementasi)", Toast.LENGTH_SHORT).show()
        }
    }
}