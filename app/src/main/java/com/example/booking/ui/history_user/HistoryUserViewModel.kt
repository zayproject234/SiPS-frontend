package com.example.booking.ui.history_user

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel

class HistoryUserViewModel : ViewModel() {

    private val _text = MutableLiveData<String>().apply {
        value = "Halo"
    }
    val text: LiveData<String> = _text
}