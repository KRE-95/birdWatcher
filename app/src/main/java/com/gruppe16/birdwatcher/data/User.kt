package com.gruppe16.birdwatcher.data

data class User(
    val userId: Int? = null,
    var userName: String,
    var userList: MutableList<Listing>
)