package learn.coroutine

import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Deferred
import kotlinx.coroutines.awaitAll
import learn.coroutine.MathScopeCoroutinesHelper.Companion.startJob
import kotlin.coroutines.cancellation.CancellationException

fun CoroutineScope.uiJob(timeout: Long = 0L, block: suspend CoroutineScope.() -> Unit) {
    startJob(this, MathScopeConfiguration.uiDispatcher, timeout, block)
}

fun CoroutineScope.backgroundJob(timeout: Long = 0L, block: suspend CoroutineScope.() -> Unit) {
    startJob(this, MathScopeConfiguration.backgroundDispatcher, timeout, block)
}





suspend fun <T> awaitAllOrCancel(vararg deferreds: Deferred<T>): List<T> {
    try {
        return awaitAll(*deferreds)
    } catch (e: Exception) {
        if (e !is CancellationException) {
            deferreds.forEach { if (it.isActive) it.cancel() }
        }

        throw e
    }
}
