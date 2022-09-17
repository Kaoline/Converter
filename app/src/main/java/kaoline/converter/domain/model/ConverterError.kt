package kaoline.converter.domain.model

/**
 * Errors that can be thrown by the app. Should be caught in UI to display a user friendly message.
 */
sealed class ConverterError(message: String? = null) : Throwable(message) {
    object NoNetworkError : ConverterError()
    class ApiError(message: String? = null) : ConverterError(message)
    object RestrictedAccessError : ConverterError()
    object NoRateAvailableError : ConverterError()
    object IncorrectAmount : ConverterError()
    object NoSuchCurrency : ConverterError()
}
