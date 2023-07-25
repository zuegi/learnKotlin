package andreabresolin.androidcoroutinesplayground.app.coroutines.testing

import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.cancelChildren
import learn.coroutine.MathScope
import kotlin.coroutines.CoroutineContext

class TestMathScope:  MathScope() {

    private val job = Job()

    override val coroutineContext: CoroutineContext
        get() = Dispatchers.Unconfined + job

    fun cancelJobs() {
        coroutineContext.cancelChildren()
    }
}
