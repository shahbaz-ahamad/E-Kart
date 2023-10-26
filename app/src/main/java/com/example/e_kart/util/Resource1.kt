package com.example.e_kart.util

sealed class Resource1<T>(
    val data: T? = null,
    val message: String? = null
) {
    class Success<T>(data: T): Resource1<T>(data)
    class Error<T>(message: String): Resource1<T>(message = message)
    class Loading<T>: Resource1<T>()
    class Unspecified<T> : Resource1<T>()
}
