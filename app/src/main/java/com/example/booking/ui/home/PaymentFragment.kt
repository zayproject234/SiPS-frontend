package com.example.booking.ui.home

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.Button
import android.widget.EditText
import android.widget.ImageView
import android.widget.Spinner
import android.widget.TextView
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.example.booking.R
import com.example.booking.retrofit.ApiConfig
import com.example.booking.retrofit.BookingResponse
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.MultipartBody
import okhttp3.RequestBody.Companion.asRequestBody
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.io.File

class PaymentFragment : Fragment() {
    private var selectedImageFile: File? = null
    private val PICK_IMAGE_REQUEST = 1

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_payment, container, false)

        val orderName = arguments?.getString("orderName") ?: "Unknown Order"
        val bandName = arguments?.getString("bandName") ?: "Unknown Band"
        val note = arguments?.getString("note") ?: ""
        val totalTime = arguments?.getInt("totalTime") ?: 1
        val userId = arguments?.getInt("userId") ?: -1
        val token = arguments?.getString("authToken") ?: ""
        val totalPayment = calculatePayment(totalTime)

        val timerTextView = view.findViewById<TextView>(R.id.countdownText)
        val uploadProofButton = view.findViewById<Button>(R.id.uploadProofButton)
        val payButton = view.findViewById<Button>(R.id.payButton)
        val proofImageView = view.findViewById<ImageView>(R.id.proofImageView)
        val amountText = view.findViewById<TextView>(R.id.amountText)
        val bankAccountEditText = view.findViewById<EditText>(R.id.bankAccountEditText)

        val bankTypeSpinner = view.findViewById<Spinner>(R.id.bankTypeSpinner)
        ArrayAdapter.createFromResource(
            requireContext(),
            R.array.bank_types,
            android.R.layout.simple_spinner_item
        ).also { adapter ->
            adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
            bankTypeSpinner.adapter = adapter
        }

        // Set bill amount
        amountText.text = "Rp%,d".format(totalPayment).replace(',', '.')

        setupCountdownTimer(timerTextView)

        uploadProofButton.setOnClickListener {
            openImagePicker()
        }

        payButton.setOnClickListener {
            val bankAccount = bankAccountEditText.text.toString().trim()
            val selectedBank = bankTypeSpinner.selectedItem.toString()
            if (bankAccount.isEmpty()) {
                Toast.makeText(context, "Please enter your bank account", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }
            if (selectedImageFile != null && token.isNotEmpty() && userId != -1) {
                submitPaymentProof(
                    orderName, bandName, totalTime, totalPayment,
                    selectedImageFile!!, token, userId, note, selectedBank, bankAccount
                )
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
        val ratePerHour = 100000
        return ratePerHour * totalTime
    }

    // Update submitPaymentProof to accept bankType and bankAccount if needed
    private fun submitPaymentProof(
        orderName: String,
        bandName: String,
        totalTime: Int,
        totalPayment: Int,
        file: File,
        token: String,
        userId: Int,
        note: String,
        bankType: String,
        bankAccount: String
    ) {
        val apiService = ApiConfig.getApiService()
        val requestFile = file.asRequestBody("image/jpeg".toMediaTypeOrNull())
        val paymentProofPart = MultipartBody.Part.createFormData("paymentProof", file.name, requestFile)

        val call = apiService.booking(
            token = "Bearer $token",
            userId = userId,
            scheduleId = 7, // TODO: Replace with actual scheduleId if available
            bandName = bandName,
            duration = totalTime,
            totalPrice = totalPayment,
            paymentProof = paymentProofPart,
            notes = note
        )

        call.enqueue(object : Callback<BookingResponse> {
            override fun onResponse(
                call: Call<BookingResponse>,
                response: Response<BookingResponse>
            ) {
                if (response.isSuccessful) {
                    val responseBody = response.body()
                    Toast.makeText(
                        requireContext(),
                        "Payment berhasil: ${responseBody?.message}",
                        Toast.LENGTH_SHORT
                    ).show()
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
                        "Payment error: ${response.message()}",
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
    }
}