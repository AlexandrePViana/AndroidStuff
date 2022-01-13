package com.example.androidprojectocrprice

import com.google.gson.annotations.SerializedName
import java.util.jar.Attributes

data class Results (
    @SerializedName("ASIN")
    val ASIN: String,
    @SerializedName("title")
    val title: String,
    @SerializedName("price")
    val price: String,
    @SerializedName("imageUrl")
    val imageUrl: String,
    @SerializedName("detailPageURL")
    val detailPageURL: String = ""
        )


