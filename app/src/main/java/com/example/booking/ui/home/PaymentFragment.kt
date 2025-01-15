package com.example.booking.ui.home

import android.app.Activity
import android.content.Context
import android.content.Context.MODE_PRIVATE
import android.content.Intent
import android.content.SharedPreferences
import android.net.Uri
import android.os.Bundle
import android.util.Log
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
import kotlin.random.Random

class PaymentFragment : Fragment() {
    private lateinit var sharedPreferences: SharedPreferences
    private var selectedImageFile: File? = null
    private val PICK_IMAGE_REQUEST = 1

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(layout.fragment_payment, container, false)
        val sharedPreferences = requireContext().getSharedPreferences("LoginPrefs", Context.MODE_PRIVATE)
        val token = sharedPreferences.getString("authToken", null)
        val userId = sharedPreferences.getInt("userId", -1)
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
            // tampilkan token
            Log.d("PaymentFragment", "Token: $token")
            if (selectedImageFile != null) {
                submitPaymentProof(orderName, bandName, totalTime, totalPayment, selectedImageFile!!, token!!, userId)
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

    private fun calculatePayment(totalTime: Int): Int {
        val ratePerHour = 100000 // Example rate
        return ratePerHour * totalTime
    }

    private fun submitPaymentProof(
        orderName: String,
        bandName: String,
        totalTime: Int,
        totalPayment: Int,
        file: File,
        token: String,
        userId: Int
    ) {
        val apiService = ApiConfig.getApiService()

        val requestFile = file.asRequestBody("image/jpeg".toMediaTypeOrNull())
        val paymentProofPart = MultipartBody.Part.createFormData("paymentProof", file.name, requestFile)

        // Generate random userId and scheduleId
        val randomUserId = Random.nextInt(1, 1000) // User ID acak antara 1 hingga 1000
        val randomScheduleId = Random.nextInt(1, 1000) // Schedule ID acak antara 1 hingga 1000

        val call = apiService.booking(
            token = "Bearer $token",
            userId = userId,
            scheduleId = 7,
            bandName = bandName,
            duration = totalTime,
            totalPrice = totalPayment,
            paymentProof = paymentProofPart,
            notes = "aaa"
        )

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
                            "Payment berhasil: ${responseBody.message}",
                            Toast.LENGTH_SHORT
                        ).show()

                        // navigate to DoneFragment
                        val bundle = Bundle().apply {
                            putString("orderName", orderName)
                            putString("bandName", bandName)
                            putString("totalTime", totalTime.toString())
                            putString("totalPayment", totalPayment.toString())
                        }
                        findNavController().navigate(R.id.action_paymentFragment_to_doneFragment, bundle)

                    } else {
                        Toast.makeText(
                            requireContext(),
                            "Payment gagal: ${responseBody?.message}",
                            Toast.LENGTH_SHORT
                        ).show()
                    }
                } else {
                    Toast.makeText(
                        requireContext(),
                        "Payment error: ${response.message()}",
                        Toast.LENGTH_SHORT
                    ).show()
                }
            }

//            private fun navigateToDoneFragment(orderName: String, bandName: String, totalTime: String, totalPayment: String) {
//                val intent = Intent(requireContext(), DoneFragment::class.java)
//                intent.putExtra("orderName", orderName)
//                intent.putExtra("bandName", bandName)
//                intent.putExtra("totalTime", totalTime)
//                intent.putExtra("totalPayment", totalPayment)
//                startActivity(intent)
//            }

            override fun onFailure(call: Call<BookingResponse>, t: Throwable) {
                Toast.makeText(
                    requireContext(),
                    "Error: ${t.message}",
                    Toast.LENGTH_SHORT
                ).show()
            }
        })
    }
}
