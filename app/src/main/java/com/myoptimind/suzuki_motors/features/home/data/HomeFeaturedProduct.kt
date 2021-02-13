package com.myoptimind.suzuki_motors.features.home.data

import com.google.gson.annotations.SerializedName
import com.myoptimind.suzuki_motors.features.shared.BaseRemoteEntity

/**
 *  Items that displays on home slider
 */
class HomeFeaturedProduct(
        val id: String,

        @SerializedName("content_type")
        val contentType: String,

        val image: String
): BaseRemoteEntity()