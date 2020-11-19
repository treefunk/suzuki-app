package com.myoptimind.suzuki_app.features.shared.api

data class MetaResponse(
        val message: String,
        val code: String,
        val status: String,
        val total: Int? = null
)