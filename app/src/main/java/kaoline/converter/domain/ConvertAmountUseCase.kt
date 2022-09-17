package kaoline.converter.domain

import kaoline.converter.domain.model.Amount
import kaoline.converter.domain.model.ConversionRate
import kaoline.converter.domain.model.ConverterError
import kaoline.converter.utils.Result
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.flow

/**
 * Convert any amount to all currencies available
 */
class ConvertAmountUseCase(
    private val ratesRepository: IRatesRepository
) : IConvertAmountUseCase {
    override fun performAction(amount: Amount): Flow<Result<List<Amount>>> = flow {
        emit(Result.Loading)

        val rates = ratesRepository.getRates()
        if (rates.isEmpty()) throw ConverterError.NoRateAvailableError

        val usdAmount = if (amount.currency != Amount.USD) {
            val currencyRate = rates.firstOrNull { it.toCurrency == amount.currency }
                ?: throw ConverterError.NoRateAvailableError
            convert(amount, currencyRate)
        } else amount

        val convertedAmount = ratesRepository.getRates().map {
            convert(usdAmount, it)
        }.toMutableList().apply {
            if (!contains(usdAmount)) add(usdAmount)
        }

        emit(Result.Success(convertedAmount))
    }.catch {
        emit(Result.Failure(it))
    }

    private fun convert(amount: Amount, rate: ConversionRate): Amount {
        return if (amount.currency == rate.toCurrency) {
            Amount(amount.amountValue / rate.rate, Amount.USD)
        } else {
            Amount(amount.amountValue * rate.rate, rate.toCurrency)
        }
    }
}
