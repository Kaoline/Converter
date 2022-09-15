package kaoline.converter.di

import kaoline.converter.ui.main.ConverterViewModel
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.dsl.module

val appModule = module {

    // Domain
    single<IConvertAmountUseCase> { ConvertAmountUseCase(get()) }

    viewModel { ConverterViewModel() }
}
