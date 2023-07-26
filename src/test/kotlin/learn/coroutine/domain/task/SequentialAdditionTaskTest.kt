package learn.coroutine.domain.task

import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.launch
import kotlinx.coroutines.newSingleThreadContext
import kotlinx.coroutines.test.resetMain
import kotlinx.coroutines.test.runTest
import kotlinx.coroutines.test.setMain
import learn.coroutine.domain.AbstractBaseTaskTest
import learn.coroutine.model.TaskExecutionSuccess
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.AfterEach
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test

class SequentialAdditionTaskTest : AbstractBaseTaskTest() {

    private lateinit var subject: SequentialAdditionTask

    @BeforeEach
    fun before() {
        subject = SequentialAdditionTask()
        Dispatchers.setMain(mainThreadSurrogate)
    }

    private val mainThreadSurrogate = newSingleThreadContext("UI thread")


    @AfterEach
    fun tearDown() {
        Dispatchers.resetMain() // reset the main dispatcher to the original Main dispatcher
        mainThreadSurrogate.close()
    }

    @OptIn(ExperimentalCoroutinesApi::class)
    @Test
    fun `should do anything`() = runTest {

        launch(Dispatchers.Main) {
            var execute = subject.execute(0.2, 0.3)
            if (execute is TaskExecutionSuccess) {
               execute = execute as TaskExecutionSuccess
                assertThat(execute.result).isEqualTo(0.5)

            }
            assertThat(execute).isEqualTo(TaskExecutionSuccess(0.5))

        }





    }

}
