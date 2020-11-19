package com.myoptimind.suzuki_app.features.suzuki_diary.my_motorcycles.data

import com.google.gson.annotations.SerializedName

class MotorcycleModel (
        val id: String,
        val name: String,
        val tagline: String,
        val price: String,
        @SerializedName("frame_number_prefix")
        val frameNumber: String,
        @SerializedName("engine_number_prefix")
        val engineNumber: String,
        val category: String,
        val thumbnail: String,
        @SerializedName("info_link")
        val infoLink: String
)