package com.gruppe16.birdwatcher.viewmodels

import androidx.lifecycle.ViewModel

class HomeCreateViewModel : ViewModel() {
    private var _pictureUri : String = "TEST"
    val pictureUri: String
        get() = _pictureUri

    fun setPictureUri(uri: String){
        _pictureUri = uri
    }
}