package com.myoptimind.suzuki_app.features.safety_tips.data

import com.google.gson.annotations.SerializedName
import com.myoptimind.suzuki_app.features.shared.BaseRemoteEntity

class SafetyTip (
        val id: String,

        val heading: String,

        @SerializedName("sub_heading")
        val subHeading: String,

        val content: String,

        @SerializedName("video_url")
        val videoUrl: String,

        val thumbnail: String,

        val banner: String,

        @SerializedName("is_active")
        val isActive: String,

        @SerializedName("f_created_at")
        val formattedCreatedAt: String
): BaseRemoteEntity()