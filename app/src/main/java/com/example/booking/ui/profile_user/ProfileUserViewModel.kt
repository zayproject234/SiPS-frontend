package com.example.booking.ui.profile_user

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel

class ProfileUserViewModel : ViewModel() {

    private val _text = MutableLiveData<String>().apply {
        value = "my"
    }
    val text: LiveData<String> = _text
}