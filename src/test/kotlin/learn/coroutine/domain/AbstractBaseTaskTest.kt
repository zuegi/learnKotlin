package learn.coroutine.domain

import andreabresolin.androidcoroutinesplayground.app.coroutines.testing.TestMathScope
import kotlinx.coroutines.Dispatchers
import learn.coroutine.MathScopeConfiguration
import org.junit.jupiter.api.BeforeAll
import org.junit.jupiter.api.BeforeEach


abstract class AbstractBaseTaskTest {

    companion object {
        @BeforeAll
        @JvmStatic
        fun beforeClassBaseMockitoTest() {
            with(MathScopeConfiguration) {
                uiDispatcher = Dispatchers.Unconfined
                backgroundDispatcher = Dispatchers.Unconfined
                ioDispatcher = Dispatchers.Unconfined
                isDelayEnabled = false
                useTestTimeout = true
            }
        }
    }

    protected lateinit var testMathScope: TestMathScope

    @BeforeEach
    fun beforeBaseMockitoTest() {
        testMathScope = TestMathScope()
    }
}
