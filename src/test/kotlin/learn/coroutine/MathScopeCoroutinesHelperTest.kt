package learn.coroutine

import kotlinx.coroutines.*
import learn.coroutine.MathScopeCoroutinesHelper.Companion.startJob
import learn.coroutine.MathScopeCoroutinesHelper.Companion.startTask
import learn.coroutine.MathScopeCoroutinesHelper.Companion.startTaskAsync
import learn.logger
import org.assertj.core.api.Assertions
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test

class MathScopeCoroutinesHelperTest {
    private class TestException(
        message: String,
    ) : Exception(message)

    private val trackedEvents = mutableListOf<String>()
    private lateinit var job: Job
    private lateinit var coroutineScope: CoroutineScope
    private val log by logger()

    @BeforeEach
    fun before() {
        job = Job()
        coroutineScope = CoroutineScope(job + Dispatchers.Default)
        trackedEvents.clear()
    }

    @Test
    fun `should start job and sync task`() {
        // given
        startJob(coroutineScope, coroutineScope.coroutineContext) {
            trackEvent("JOB_START")

            startTask(coroutineContext) {
                trackEvent("SYNC_TASK_START")
                delay(100)
                trackEvent("SYNC_TASK_END")
            }

            trackEvent("JOB_END")
        }
        // when
        job.start()

        // damit die Coroutine ausgeführt werden kann
        Thread.sleep(500L)

        // then
        assertThatEventsSequenceIs(
            "JOB_START",
            "SYNC_TASK_START",
            "SYNC_TASK_END",
            "JOB_END",
        )
    }

    @Test
    fun `should start void job and track start stop`() {
        startJob(coroutineScope, coroutineScope.coroutineContext) {
            trackEvent("JOB_START")
            delay(100)
            trackEvent("JOB_END")
        }

        job.start()

        Thread.sleep(500L)

        assertThatEventsSequenceIs(
            "JOB_START",
            "JOB_END",
        )
    }

    @Test
    fun `should start job and async task  and results in exception`() =
        runBlocking {
            startJob(coroutineScope, coroutineScope.coroutineContext) {
                trackEvent("JOB_START")

                val deferred1 =
                    startTaskAsync(this, Dispatchers.Default) {
                        delay(100)
                        trackEvent("TASK1_START")
                        delay(1000)
                        throw TestException("TASK1_EXCEPTION")
                    }

                try {
                    awaitAllOrCancel(deferred1)
                } catch (e: TestException) {
                    trackEvent(e.message!!)
                }
                trackEvent("JOB_END")
            }

            job.children.forEach { it.join() }

            assertThatEventsSequenceIs(
                "JOB_START",
                "TASK1_START",
                "TASK1_EXCEPTION",
                "JOB_END",
            )
        }

    private fun trackEvent(event: String) {
        trackedEvents.add(event)
    }

    private fun assertThatEventsSequenceIs(vararg expectedEventsSequence: String) {
        Assertions.assertThat(trackedEvents).isEqualTo(expectedEventsSequence.asList())
    }
}
