package com.gruppe16.birdwatcher.data

data class Listing (
    // rekkefølge må være lik firebase
    var birdName: String,
    var description: String,
    var listingId: Int,
    var picture: String,
//    var place: String,
//    var time: String,
    var user: String

)

