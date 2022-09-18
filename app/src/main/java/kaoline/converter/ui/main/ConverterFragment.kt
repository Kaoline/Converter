package kaoline.converter.ui.main

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.*
import androidx.core.widget.doOnTextChanged
import androidx.fragment.app.Fragment
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout
import kaoline.converter.R
import kaoline.converter.domain.model.ConverterError
import org.koin.androidx.viewmodel.ext.android.viewModel

/**
 * The converter page
 */
class ConverterFragment : Fragment() {

    companion object {
        fun newInstance() = ConverterFragment()
    }

   val viewModel: ConverterViewModel by viewModel()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        return inflater.inflate(R.layout.fragment_converter, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val amountEditText = view.findViewById<EditText>(R.id.amount_value_et)
        val currenciesSpinner = view.findViewById<Spinner>(R.id.amount_currency_sp)
        val swipeRefresh = view.findViewById<SwipeRefreshLayout>(R.id.swipe_refresh)

        // Listen to errors
        viewModel.errorEvent.observe(viewLifecycleOwner) {
            it?.let { error ->
                val message = when (error) {
                    is ConverterError.NoNetworkError -> requireContext().getString(R.string.no_network_error)
                    is ConverterError.IncorrectAmount -> requireContext().getString(R.string.incorrect_amount)
                    is ConverterError.NoSuchCurrency -> requireContext().getString(R.string.no_such_currency)
                    is ConverterError.RefreshTooEarly -> requireContext().getString(R.string.refresh_too_early, error.timeLeftMs / 1000 / 60)
                    else -> "${error}: ${error.message}"
                }
                Toast.makeText(requireContext(), message, Toast.LENGTH_LONG).show()
            }
        }

        // Listen to loading state
        viewModel.loadingState.observe(viewLifecycleOwner) {
            swipeRefresh.isRefreshing = it
        }

        // Init interactions
        amountEditText.doOnTextChanged { text, _, _, _ ->
            viewModel.onAmountChanged(text.toString(), currenciesSpinner.selectedItem as? String)
        }
        currenciesSpinner.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onItemSelected(parent: AdapterView<*>?, p1: View?, pos: Int, p3: Long) {
                parent?.let {
                    viewModel.onAmountChanged(
                        amountEditText.text.toString(),
                        it.selectedItem as? String
                    )
                }
            }

            override fun onNothingSelected(p0: AdapterView<*>?) {
                // Do nothing
            }

        }
        swipeRefresh.setOnRefreshListener { viewModel.refresh(amountEditText.text.toString(), currenciesSpinner.selectedItem as? String) }

        // Init view population with retrieved data
        viewModel.availableCurrencies.observe(viewLifecycleOwner) {
            currenciesSpinner.adapter =
                ArrayAdapter<String>(requireContext(), android.R.layout.simple_spinner_item).apply {
                    addAll(it)
                }
        }

        viewModel.convertedAmounts.observe(viewLifecycleOwner) {
            view.findViewById<GridView>(R.id.converter_values_gv).adapter =
                ArrayAdapter<String>(requireContext(), android.R.layout.simple_list_item_1).apply {
                    addAll(it)
                }
        }
    }

}