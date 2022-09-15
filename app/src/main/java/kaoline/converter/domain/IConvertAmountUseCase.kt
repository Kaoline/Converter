package kaoline.converter.domain

import kaoline.converter.domain.model.Amount
import kaoline.converter.utils.Result
import kotlinx.coroutines.flow.Flow

interface IConvertAmountUseCase {
    fun performAction(amount: Amount): Flow<Result<List<Amount>>>
}