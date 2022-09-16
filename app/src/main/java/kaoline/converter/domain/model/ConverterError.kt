package kaoline.converter.domain.model

sealed class ConverterError(message: String? = null) : Throwable(message) {
    object NoNetworkError : ConverterError()
    class ApiError(message: String? = null) : ConverterError(message)
    object RestrictedAccessError : ConverterError()
    object NoRateAvailableError : ConverterError()
}
