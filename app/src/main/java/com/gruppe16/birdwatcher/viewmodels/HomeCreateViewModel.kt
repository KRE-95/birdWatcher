package com.gruppe16.birdwatcher.viewmodels

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel

class HomeCreateViewModel : ViewModel() {
    private val _pictureUri = MutableLiveData("")
    val pictureUri: LiveData<String> = _pictureUri

    private val _date = MutableLiveData("")
    val date: LiveData<String> = _date

    fun setPictureUri(uri: String){
        _pictureUri.value = uri
    }

    fun setDate(pictureDate: String){
        _date.value = pictureDate
    }
}