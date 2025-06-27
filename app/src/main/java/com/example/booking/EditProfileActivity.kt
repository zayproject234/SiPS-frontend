package com.example.booking

import android.app.AlertDialog
import android.content.Intent
import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.provider.MediaStore
import androidx.activity.result.ActivityResultLauncher
import com.example.booking.databinding.ActivityEditProfileBinding
import com.example.booking.ui.profile_user.ProfileUserModel
import com.example.booking.ui.profile_user.ProfileUserViewModel


class EditProfileActivity : AppCompatActivity() {

    private lateinit var binding: ActivityEditProfileBinding
    private lateinit var profileUserViewModel: ProfileUserViewModel
    private  lateinit var userId : String
    private lateinit var token : String
    private lateinit var imagePickerLauncher: ActivityResultLauncher<Intent>
    private  lateinit var profile : ProfileUserModel
    private lateinit var loadingDialog: AlertDialog

    private var imageUri: Uri? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityEditProfileBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.ivCamera.setOnClickListener {
            selectImage()

        }
    }

    private fun selectImage() {
        val intent = Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI)
        imagePickerLauncher.launch(intent)
    }
}