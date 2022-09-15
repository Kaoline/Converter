package kaoline.converter.utils

sealed class Result<out T> {
    object Loading : Result<Nothing>()
    data class Success<T>(val data: T) : Result<T>()
    data class Failure(val error: Throwable, val message: String? = null) : Result<Nothing>()
}
