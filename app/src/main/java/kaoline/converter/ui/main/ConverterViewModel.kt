package kaoline.converter.ui.main

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import kaoline.converter.domain.IConvertAmountUseCase
import kaoline.converter.domain.IGetAvailableCurrenciesUseCase
import kaoline.converter.domain.model.Amount
import kaoline.converter.domain.model.ConverterError
import kaoline.converter.ui.base.BaseViewModel
import kaoline.converter.domain.IRefreshRatesUseCase

/**
 * The converter page view model.
 * Fetch available currencies at init.
 */
class ConverterViewModel(
    getAvailableCurrencies: IGetAvailableCurrenciesUseCase,
    private val convertAmount: IConvertAmountUseCase,
    private val refreshRates: IRefreshRatesUseCase
) : BaseViewModel() {

    private val _availableCurrencies: MutableLiveData<List<String>> = MutableLiveData(emptyList())
    val availableCurrencies: LiveData<List<String>>
        get() = _availableCurrencies

    private val _convertedAmounts: MutableLiveData<List<String>> = MutableLiveData(emptyList())
    val convertedAmounts: LiveData<List<String>>
        get() = _convertedAmounts

    init {
        collect(getAvailableCurrencies.performAction()) {
            _availableCurrencies.postValue(it.data)
        }
    }

    fun onAmountChanged(amount: String, currency: String?) {
        val solidAmount = if (amount.isEmpty()) "0" else if (amount == "-") "0" else amount
        if (!validateAmount(solidAmount)) {
            _errorEvent.postValue(ConverterError.IncorrectAmount)
        } else if (!validateCurrency(currency)) {
            _errorEvent.postValue(ConverterError.NoSuchCurrency)
        } else {
            collect(convertAmount.performAction(Amount(solidAmount.toFloat(), currency!!))) {
                val amountsString = it.data.map { c -> "${c.currency} ${c.amountValue}" }
                _convertedAmounts.postValue(amountsString)
            }
        }
    }

    /**
     * Ask for rates refresh and recompute the new converted amounts
     */
    fun refresh(amount: String, currency: String?) {
        collect(refreshRates.performAction()) {
            onAmountChanged(amount, currency)
        }
    }

    /**
     * A correct amount is a float, positive or negative.
     */
    private fun validateAmount(amount: String): Boolean {
        return amount.toFloatOrNull() != null
    }

    /**
     * A correct currency is one retrieved from the data layer.
     */
    private fun validateCurrency(currency: String?): Boolean {
        return _availableCurrencies.value?.contains(currency) ?: false
    }
}