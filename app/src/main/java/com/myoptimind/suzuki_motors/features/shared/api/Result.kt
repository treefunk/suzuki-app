package com.myoptimind.suzuki_motors.features.shared.api

sealed class Result<out T> {
    object Loading : Result<Nothing>()
    class Success<out R>(val data: R): Result<R>()
    class Error(val error: Exception): Result<Nothing>()
}