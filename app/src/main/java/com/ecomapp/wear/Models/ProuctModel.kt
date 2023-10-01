package com.ecomapp.wear.Models

data class ProuctModel(
    val productId : String? = null,
    val productTitle : String? = null,
    val productSubTitle : String? = null,
    val productDesc : String? = null,
    val productOldPrice : String? = null,
    val productPrice : String? = null,
    val productMainImage : String? = null,
    var noOfRating : Float? = null,
    var rate : Float? = null

)
