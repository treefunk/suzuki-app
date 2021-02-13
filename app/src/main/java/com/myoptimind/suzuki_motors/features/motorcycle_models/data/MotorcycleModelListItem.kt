package com.myoptimind.suzuki_motors.features.motorcycle_models.data

import com.google.gson.annotations.SerializedName

class MotorcycleModelListItem (

        val id: String,

        val name: String,

        @SerializedName("tagline")
        val tagLine: String,

        val price: String,

        val category: String,

        val thumbnail: String,

        @SerializedName("info_link")
        val infoLink: String
)