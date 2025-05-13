package com.example.booking.ui.profile

import android.net.Uri
import com.google.gson.annotations.SerializedName

data class ProfileModel(
    @field:SerializedName("createdAt")
    val createdAt: String? = null,

    @field:SerializedName("profile_img")
    val profileImg: Uri? = null,

    @field:SerializedName("password")
    val password: String? = null,

    @field:SerializedName("confirmPass")
    val confirmPass: String? = null,

    @field:SerializedName("user_id")
    val userId: String? = null,

    @field:SerializedName("fullname")
    val fullname: String? = null,

    @field:SerializedName("email")
    val email: String? = null,

    @field:SerializedName("token")
    val token: String? = null,
)