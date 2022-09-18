package kaoline.converter.di

import androidx.room.Room
import kaoline.converter.data.RatesRepository
import kaoline.converter.data.cache.AppDatabase
import kaoline.converter.data.cache.RateDao
import kaoline.converter.data.network.RatesApiService
import kaoline.converter.domain.*
import kaoline.converter.ui.main.ConverterViewModel
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import org.koin.android.ext.koin.androidContext
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.dsl.module
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

val appModule = module {

    // Network
    single<OkHttpClient> {
        OkHttpClient.Builder()
            .addInterceptor(HttpLoggingInterceptor())
            .addNetworkInterceptor {
                val url = it.request().url.newBuilder()
                    .addQueryParameter("app_id", "8f6766201ccb4ced8d101ba972b2ec39").build()
                val request = it.request().newBuilder().url(url).build()
                it.proceed(request)
            }
            .build()
    }
    single<Retrofit> {
        Retrofit.Builder()
            .baseUrl("https://openexchangerates.org/api/")
            .addConverterFactory(GsonConverterFactory.create())
            .client(get())
            .build()
    }
    single<RatesApiService> { get<Retrofit>().create(RatesApiService::class.java) }

    // Cache
    single<AppDatabase> {
        Room.databaseBuilder(
            androidContext(),
            AppDatabase::class.java,
            "database"
        ).build()
    }
    single<RateDao> { get<AppDatabase>().rateDao() }

    // Data
    single<IRatesRepository> { RatesRepository(get(), get()) }

    // Domain
    single<IConvertAmountUseCase> { ConvertAmountUseCase(get()) }
    single<IGetAvailableCurrenciesUseCase> { GetAvailableCurrenciesUseCase(get()) }
    single<IRefreshRatesUseCase> { RefreshRatesUseCase(get()) }

    // UI
    viewModel { ConverterViewModel(get(), get(), get()) }
}
