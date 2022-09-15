package kaoline.converter.domain

import kaoline.converter.domain.model.Amount
import kaoline.converter.utils.Result
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow

/**
 * Convert any amount to all currencies available
 */
class ConvertAmountUseCase(
    private val ratesRepository: IRatesRepository
) : IConvertAmountUseCase {
    override fun performAction(amount: Amount): Flow<Result<List<Amount>>> = flow {
    }
}
