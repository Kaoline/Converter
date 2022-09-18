package kaoline.converter.domain

import kaoline.converter.utils.Result
import kotlinx.coroutines.flow.Flow

interface IRefreshRatesUseCase {
    fun performAction(): Flow<Result<Any?>>
}
