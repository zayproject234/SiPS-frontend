package com.example.booking.ui.home

import android.app.Activity
import android.content.Context
import android.content.Context.MODE_PRIVATE
import android.content.Intent
import android.content.SharedPreferences
import android.net.Uri
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.example.booking.R
import com.example.booking.R.*
import com.example.booking.retrofit.ApiConfig
import com.example.booking.retrofit.BookingRequest
import com.example.booking.retrofit.BookingResponse
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.MultipartBody
import okhttp3.RequestBody.Companion.asRequestBody
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.io.File

class PaymentFragment : Fragment() {
    private lateinit var sharedPreferences: SharedPreferences
    private var selectedImageFile: File? = null
    private val PICK_IMAGE_REQUEST = 1

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(layout.fragment_payment, container, false)
        val sharedPreferences = requireContext().getSharedPreferences("user_preferences", Context.MODE_PRIVATE)
        val token = sharedPreferences.getString("auth_token", null)
        val orderName = arguments?.getString("orderName") ?: "Unknown Order"
        val bandName = arguments?.getString("bandName") ?: "Unknown Band"
        val totalTime = arguments?.getInt("totalTime") ?: 1
        val totalPayment = calculatePayment(totalTime)

        val timerTextView = view.findViewById<TextView>(R.id.countdownText)
        val uploadProofButton = view.findViewById<Button>(R.id.uploadProofButton)
        val payButton = view.findViewById<Button>(R.id.payButton)
        val proofImageView = view.findViewById<ImageView>(R.id.proofImageView)

        setupCountdownTimer(timerTextView)

        uploadProofButton.setOnClickListener {
            openImagePicker()
        }

        payButton.setOnClickListener {
            if (selectedImageFile != null) {
                submitPaymentProof(orderName, bandName, totalTime, totalPayment, selectedImageFile!!, token!!)
            } else {
                Toast.makeText(context, "Please upload a payment proof first", Toast.LENGTH_SHORT).show()
            }
        }

        return view
    }

    private fun setupCountdownTimer(timerTextView: TextView) {
        object : android.os.CountDownTimer(600000, 1000) {
            override fun onTick(millisUntilFinished: Long) {
                val minutes = (millisUntilFinished / 1000) / 60
                val seconds = (millisUntilFinished / 1000) % 60
                timerTextView.text = String.format("%02d:%02d", minutes, seconds)
            }

            override fun onFinish() {
                Toast.makeText(context, "Payment time expired!", Toast.LENGTH_SHORT).show()
            }
        }.start()
    }

    private fun openImagePicker() {
        val intent = Intent(Intent.ACTION_PICK)
        intent.type = "image/*"
        startActivityForResult(intent, PICK_IMAGE_REQUEST)
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        if (requestCode == PICK_IMAGE_REQUEST && resultCode == Activity.RESULT_OK) {
            val selectedImageUri: Uri? = data?.data
            selectedImageUri?.let {
                val file = uriToFile(it)
                selectedImageFile = file
                view?.findViewById<ImageView>(R.id.proofImageView)?.setImageURI(it)
            }
        }
    }

    private fun uriToFile(uri: Uri): File {
        val contentResolver = requireContext().contentResolver
        val inputStream = contentResolver.openInputStream(uri)
        val tempFile = File.createTempFile("payment_proof", ".jpg")
        tempFile.outputStream().use { inputStream?.copyTo(it) }
        return tempFile
    }

    private fun calculatePayment(totalTime: Int): String {
        val ratePerHour = 100000 // Example rate
        return "Rp${ratePerHour * totalTime}"
    }

    private fun submitPaymentProof(orderName: String, bandName: String, totalTime: Int, totalPayment: String, file: File, token: String) {
        val apiService = ApiConfig.getApiService()

        val requestFile = file.asRequestBody("image/jpeg".toMediaTypeOrNull())
        val paymentProofPart = MultipartBody.Part.createFormData("paymentProof", file.name, requestFile)
        val bookingRequest = BookingRequest(
            userId = 1, // Replace with actual user ID
            scheduleId = 123, // Replace with actual schedule ID
            bandName = bandName,
            duration = totalTime,
            totalPrice = totalPayment,
            paymenProof = file.name,
            notes = "aaa"
        )

        val call = apiService.booking(token, bookingRequest, paymentProofPart)

        call.enqueue(object : Callback<BookingResponse> {
            override fun onResponse(
                call: Call<BookingResponse>,
                response: Response<BookingResponse>
            ) {
                if (response.isSuccessful) {
                    val responseBody = response.body()
                    if (responseBody != null) {
                        Toast.makeText(
                            requireContext(),
                            "payment berhasil: ${responseBody.message}",
                            Toast.LENGTH_SHORT
                        ).show()
//                        findNavController().navigate(R.id.doneFragment)
                    } else {
                        Toast.makeText(
                            requireContext(),
                            "payment gagal: ${responseBody?.message}",
                            Toast.LENGTH_SHORT
                        ).show()

                    }
                } else {
                    Toast.makeText(
                        requireContext(),
                        "payment eror: ${response.message()}",
                        Toast.LENGTH_SHORT
                    ).show()
                }
            }

            override fun onFailure(call: Call<BookingResponse>, t: Throwable) {
                Toast.makeText(
                    requireContext(),
                    "Error: ${t.message}",
                    Toast.LENGTH_SHORT
                ).show()
            }
        })

//        lifecycleScope.launch {
//            try {
//                val response = apiService.booking(bookingRequest)
//                if () {
//                    findNavController().navigate(R.id.doneFragment)
//                } else {
//                    Toast.makeText(context, "Payment submission failed", Toast.LENGTH_SHORT).show()
//                }
//            } catch (e: Exception) {
//                Toast.makeText(context, "Error: ${e.message}", Toast.LENGTH_SHORT).show()
//            }
//        }


    }
}
