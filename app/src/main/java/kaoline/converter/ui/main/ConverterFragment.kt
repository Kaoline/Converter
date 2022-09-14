package kaoline.converter.ui.main

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import kaoline.converter.R
import org.koin.androidx.viewmodel.ext.android.viewModel

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

    }

}