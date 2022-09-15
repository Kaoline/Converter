package kaoline.converter.di

import io.mockk.mockk
import org.junit.Test
import org.koin.android.ext.koin.androidContext
import org.koin.test.KoinTest
import org.koin.test.check.checkModules

class CheckModulesTest : KoinTest {

    @Test
    fun checkAllModules() {
        checkModules {
            androidContext(mockk())
            modules(appModule)
        }
    }
}
