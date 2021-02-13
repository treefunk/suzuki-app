package com.myoptimind.suzuki_motors.features.whats_new.data

import com.google.gson.annotations.SerializedName
import com.myoptimind.suzuki_motors.features.shared.BaseRemoteEntity


class Article (
        val id: String,

        val heading: String,

        @SerializedName("sub_heading")
        val subHeading: String,

        val category: String,

        val content: String,

        val thumbnail: String,

        val banner: String,

        @SerializedName("is_active")
        val isActive: String,

        @SerializedName("f_created_at")
        val formattedCreatedAt: String
): BaseRemoteEntity()