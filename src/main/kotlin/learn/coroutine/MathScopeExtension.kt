package learn.coroutine

import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Deferred
import kotlinx.coroutines.awaitAll
import kotlinx.coroutines.delay
import learn.coroutine.MathScopeCoroutinesHelper.Companion.startJob
import learn.coroutine.MathScopeCoroutinesHelper.Companion.startTask
import learn.coroutine.MathScopeCoroutinesHelper.Companion.startTaskAsync
import kotlin.coroutines.cancellation.CancellationException

fun CoroutineScope.uiJob(timeout: Long = 0L, block: suspend CoroutineScope.() -> Unit) {
    startJob(this, MathScopeConfiguration.uiDispatcher, timeout, block)
}

fun CoroutineScope.backgroundJob(timeout: Long = 0L, block: suspend CoroutineScope.() -> Unit) {
    startJob(this, MathScopeConfiguration.backgroundDispatcher, timeout, block)
}

fun CoroutineScope.ioJob(timeout: Long = 0L, block: suspend CoroutineScope.() -> Unit) {
    startJob(this, MathScopeConfiguration.ioDispatcher, timeout, block)
}

suspend fun <T> uiTask(timeout: Long = 0L, block: suspend CoroutineScope.() -> T): T {
    return startTask(MathScopeConfiguration.uiDispatcher, timeout, block)
}

suspend fun <T> backgroundTask(timeout: Long = 0L, block: suspend CoroutineScope.() -> T): T {
    return startTask(MathScopeConfiguration.backgroundDispatcher, timeout, block)
}

suspend fun <T> ioTask(timeout: Long = 0L, block: suspend CoroutineScope.() -> T): T {
    return startTask(MathScopeConfiguration.ioDispatcher, timeout, block)
}

fun <T> CoroutineScope.uiTaskAsync(timeout: Long = 0L, block: suspend CoroutineScope.() -> T): Deferred<T> {
    return startTaskAsync(this, MathScopeConfiguration.uiDispatcher, timeout, block)
}

fun <T> CoroutineScope.backgroundTaskAsync(timeout: Long = 0L, block: suspend CoroutineScope.() -> T): Deferred<T> {
    return startTaskAsync(this, MathScopeConfiguration.backgroundDispatcher, timeout, block)
}

fun <T> CoroutineScope.ioTaskAsync(timeout: Long = 0L, block: suspend CoroutineScope.() -> T): Deferred<T> {
    return startTaskAsync(this, MathScopeConfiguration.ioDispatcher, timeout, block)
}

suspend fun delayTask(milliseconds: Long) {
    if (MathScopeConfiguration.isDelayEnabled) {
        delay(milliseconds)
    }
}

suspend fun <T> Deferred<T>.awaitOrReturn(returnIfCancelled: T): T {
    return try {
        await()
    } catch (e: CancellationException) {
        returnIfCancelled
    }
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
