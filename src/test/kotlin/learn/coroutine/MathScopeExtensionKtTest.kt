package learn.coroutine

import kotlinx.coroutines.delay
import kotlinx.coroutines.runBlocking
import org.assertj.core.api.Assertions
import org.junit.jupiter.api.Test

class MathScopeExtensionKtTest {

    private val trackedEvents = mutableListOf<String>()

//    mache noch einen Test mit job wie mit dem fun <T> CoroutineScope.backgroundTaskAsync
    @Test
    fun `should start customJob with one backgroundTaskAsync`() = runBlocking {
         customJob {
            trackEvent("JOB_LAUNCHED")
            backgroundTaskAsync {
                trackEvent("ASYNC_TASK_LAUNCHED")
                delay(500)
                trackEvent("ASYNC_TASK_FINISHED")
            }.start()
            trackEvent("JOB_FINISHED")
        }

        assertThatEventsSequenceIs(
            "JOB_LAUNCHED",
            "JOB_FINISHED",
            "ASYNC_TASK_LAUNCHED",
            "ASYNC_TASK_FINISHED"
        )
    }

    @Test
    fun `should start customJob with one backgroundTask`() = runBlocking {
        customJob {
            trackEvent("JOB_LAUNCHED")
            backgroundTask {
                trackEvent("SYNC_TASK_LAUNCHED")
                delay(500)
                trackEvent("SYNC_TASK_FINISHED")
            }
            trackEvent("JOB_FINISHED")
        }
        assertThatEventsSequenceIs(
            "JOB_LAUNCHED",
            "SYNC_TASK_LAUNCHED",
            "SYNC_TASK_FINISHED",
            "JOB_FINISHED"
        )
    }


    private fun trackEvent(event: String) {
        trackedEvents.add(event)
    }

    private fun assertThatEventsSequenceIs(vararg expectedEventsSequence: String) {
        Assertions.assertThat(trackedEvents).isEqualTo(expectedEventsSequence.asList())
    }
}

