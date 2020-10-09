package com.myoptimind.suzuki_app.shared.api

sealed class Result<out T> {
    class Success<out R>(val data: R): Result<R>()
    class Error(val error: Exception): Result<Nothing>()
    object Loading : Result<Nothing>()
}