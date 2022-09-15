package kaoline.converter.domain

import kaoline.converter.utils.Result
import kotlinx.coroutines.flow.Flow

interface IGetAvailableCurrenciesUseCase {
    fun performAction(): Flow<Result<List<String>>>
}