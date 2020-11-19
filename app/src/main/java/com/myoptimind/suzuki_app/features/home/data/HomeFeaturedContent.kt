package com.myoptimind.suzuki_app.features.home.data

import com.google.gson.annotations.SerializedName

class HomeFeaturedContent(
        val id: String,
        val heading: String,
        @SerializedName("sub_heading")
        val subHeading: String,
        val background: String
)