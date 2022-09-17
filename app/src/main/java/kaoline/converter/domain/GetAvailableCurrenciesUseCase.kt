package kaoline.converter.domain

import kaoline.converter.domain.model.Amount
import kaoline.converter.utils.Result
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.flow

class GetAvailableCurrenciesUseCase(
    private val ratesRepository: IRatesRepository
) : IGetAvailableCurrenciesUseCase {
    override fun performAction(): Flow<Result<List<String>>> = flow {
        emit(Result.Loading)
        val currencies = ratesRepository.getRates().map { it.toCurrency }.toMutableList()
            .apply { if (!contains(Amount.USD)) add(Amount.USD) }
        emit(Result.Success(currencies))
    }.catch {
        emit(Result.Failure(it))
    }
}
