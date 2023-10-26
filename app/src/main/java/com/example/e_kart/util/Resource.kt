package com.example.e_kart.util

sealed class Resource<out R>(
){
    data class Success<out R>(val result:R):Resource<R>()
    data class Error(val error:Exception):Resource<Nothing>()
    object Loading:Resource<Nothing>()

    fun <T> Resource<T>.getData(): R? {
        return when (this) {
            is Resource.Success -> result as R
            else -> null
        }
    }
}


