package com.example.booking.ui.profile_admin

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel

class ProfileAdminViewModel : ViewModel() {

    private val _text = MutableLiveData<String>().apply {
        value = "This is profile_admin Fragment"
    }
    val text: LiveData<String> = _text
}