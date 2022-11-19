package com.gruppe16.birdwatcher.viewmodels

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel

class SharedViewModel : ViewModel() {
    private val _pictureUri = MutableLiveData("")
    val pictureUri: LiveData<String> = _pictureUri

    private val _date = MutableLiveData("")
    val date: LiveData<String> = _date

    private val _birdName = MutableLiveData("")
    val birdName: LiveData<String> = _birdName

    private val _description = MutableLiveData("")
    val description: LiveData<String> = _description

    private val _picture = MutableLiveData("")
    val picture: LiveData<String> = _picture

    private val _user = MutableLiveData("")
    val user: LiveData<String> = _user

    private val _listId = MutableLiveData("")
    val listId: LiveData<String> = _listId

    fun setPictureUri(uri: String) {
        _pictureUri.value = uri
    }

    fun setDate(pictureDate: String) {
        _date.value = pictureDate
    }

    fun setBirdName(birdName: String) {
        _birdName.value = birdName
    }

    fun setDescription(description: String) {
        _description.value = description
    }

    fun setPicture(picture: String) {
        _picture.value = picture
    }

    fun setUser(user: String) {
        _user.value = user
    }

    fun setListId(listId: String) {
        _listId.value = listId
    }
}