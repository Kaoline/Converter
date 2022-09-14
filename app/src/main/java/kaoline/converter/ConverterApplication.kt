package kaoline.converter

import android.app.Application
import kaoline.converter.di.appModule
import org.koin.android.ext.koin.androidContext
import org.koin.android.ext.koin.androidLogger
import org.koin.core.component.KoinComponent
import org.koin.core.context.startKoin

class ConverterApplication : Application(), KoinComponent {

    override fun onCreate() {
        super.onCreate()

        startKoin {
            androidLogger()
            androidContext(this@ConverterApplication)
            modules(appModule)
        }
    }
}
