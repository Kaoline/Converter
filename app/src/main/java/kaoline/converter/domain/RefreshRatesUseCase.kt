package kaoline.converter.domain

import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.flow
import kaoline.converter.utils.Result

/**
 * Refresh the rates with a minimum interval of 30 min between refreshes
 */
class RefreshRatesUseCase(
    private val ratesRepository: IRatesRepository
): IRefreshRatesUseCase {
    override fun performAction() = flow<Result<Any?>> {
        emit(Result.Loading)
        ratesRepository.refreshRates(30 * 60 * 1000)
        emit(Result.Success(null))
    }.catch {
        emit(Result.Failure(it))
    }
}
