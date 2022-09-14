package kaoline.converter.di

import org.junit.Test
import org.koin.test.KoinTest
import org.koin.test.check.checkModules

class CheckModulesTest : KoinTest {

    @Test
    fun checkAllModules() = checkModules {
        modules(appModule)
    }
}
