package kaoline.converter.domain

import kaoline.converter.utils.Result
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow

class GetAvailableCurrenciesUseCase(
    private val ratesRepository: IRatesRepository
) : IGetAvailableCurrenciesUseCase {
    override fun performAction(): Flow<Result<List<String>>> = flow {
    }
}
