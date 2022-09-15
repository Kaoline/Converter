package kaoline.converter.domain.model

sealed class ConverterError : Throwable() {
    object NoRateAvailableError : ConverterError()
}
