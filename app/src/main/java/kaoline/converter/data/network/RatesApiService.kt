package kaoline.converter.data.network

import kaoline.converter.data.network.model.RatesResponse
import retrofit2.Response
import retrofit2.http.GET

interface RatesApiService {
    /**
     * Get all rates from USD
     */
    @GET("latest.json?base=USD")
    suspend fun getRates() : Response<RatesResponse>
}
