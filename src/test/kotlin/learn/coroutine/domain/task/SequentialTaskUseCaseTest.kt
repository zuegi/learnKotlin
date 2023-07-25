package learn.coroutine.domain.task

import kotlinx.coroutines.runBlocking
import learn.coroutine.domain.AbstractBaseTaskTest
import learn.coroutine.model.TaskExecutionResult
import learn.coroutine.model.TaskExecutionSuccess
import learn.coroutine.repository.RemoteRepository
import org.assertj.core.api.Assertions
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.mockito.BDDMockito
import org.mockito.Mockito

class SequentialTaskUseCaseTest: AbstractBaseTaskTest() {

    private var mockRemoteRepository = Mockito.mock(RemoteRepository::class.java)

    private lateinit var subject: SequentialTaskUseCase

    private lateinit var actualExecuteResult: TaskExecutionResult

    @BeforeEach
    fun before() {
        subject = SequentialTaskUseCase(mockRemoteRepository)
    }

    // region Test

    @Test
    fun execute_executesTask() {
        givenRemoteRepositoryWillReturn(100)
        whenExecuteWith(10, 20, 30)
        thenResultIs(TaskExecutionSuccess(100))
    }

    // endregion Test

    // region Given

    private fun givenRemoteRepositoryWillReturn(result: Long) {
        BDDMockito.given(mockRemoteRepository.fetchData(BDDMockito.anyLong())).willReturn(result)
    }

    // endregion Given

    // region When

    private fun whenExecuteWith(startDelay: Long, minDuration: Long, maxDuration: Long) = runBlocking {
        actualExecuteResult = subject.execute(startDelay, minDuration, maxDuration)
    }

    // endregion When

    // region Then

    private fun thenResultIs(result: TaskExecutionResult) {
        Assertions.assertThat(actualExecuteResult).isEqualTo(result)
    }

    // endregion Then
}
