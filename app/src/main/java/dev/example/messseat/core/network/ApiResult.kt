package dev.example.messseat.core.network

sealed interface ApiResult<out T> {
    data class Success<T>(val data: T) : ApiResult<T>

    data class Error(
        val message: String,
        val code: String? = null,
        val status: Int? = null
    ) : ApiResult<Nothing>
}

