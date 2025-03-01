package com.prafullkumar.common.utils

sealed class BaseClass<out T> {
    data class Success<T>(val data: T) : BaseClass<T>()
    data class Error<T>(val error: Throwable) : BaseClass<T>()
    data object Loading : BaseClass<Nothing>()
}