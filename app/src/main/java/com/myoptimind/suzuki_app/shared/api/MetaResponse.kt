package com.myoptimind.suzuki_app.shared.api

data class MetaResponse(
        val message: String,
        val code: String,
        val status: String,
        val total: Int? = null
)