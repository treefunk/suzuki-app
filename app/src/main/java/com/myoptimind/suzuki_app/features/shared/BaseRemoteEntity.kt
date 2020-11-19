package com.myoptimind.suzuki_app.features.shared

import com.google.gson.annotations.SerializedName

open class BaseRemoteEntity (
        @SerializedName("created_at")
        val createdAt: String? = null,

        @SerializedName("updated_at")
        val updatedAt: String? = null
)