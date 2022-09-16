package kaoline.converter.data.base

import kaoline.converter.domain.model.ConverterError
import retrofit2.Response
import java.io.IOException

abstract class BaseRepository {
    protected suspend fun <T> handleErrorsCall(apiCall: suspend () -> Response<T>): T? {
        try {
            val response = apiCall()
            if (response.isSuccessful) {
                return response.body()
            } else {
                throw if (response.code() == 429) ConverterError.RestrictedAccessError
                else ConverterError.ApiError(response.message())
            }
        } catch (e: IOException) {
            // No network
            throw ConverterError.NoNetworkError
        }
    }
}
