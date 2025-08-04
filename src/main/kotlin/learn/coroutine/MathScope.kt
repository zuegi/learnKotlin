package learn.coroutine

import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Job
import kotlin.coroutines.CoroutineContext

open class MathScope : CoroutineScope {
    private val job = Job()

    override val coroutineContext: CoroutineContext
        get() = MathScopeConfiguration.uiDispatcher + job
}
