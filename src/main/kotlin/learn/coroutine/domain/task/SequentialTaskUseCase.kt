package learn.coroutine.domain.task

import learn.coroutine.backgroundTask
import learn.coroutine.delayTask
import learn.coroutine.domain.BaseTask
import learn.coroutine.ioTask
import learn.coroutine.model.TaskExecutionResult
import learn.coroutine.model.TaskExecutionSuccess
import learn.coroutine.repository.RemoteRepository
import kotlin.random.Random

class SequentialTaskUseCase(
    private val remoteRepository: RemoteRepository,
) : BaseTask() {
    suspend fun execute(
        startDelay: Long,
        minDuration: Long,
        maxDuration: Long,
    ): TaskExecutionResult =
        backgroundTask {
            delayTask(startDelay)

            val taskDuration = Random.nextLong(minDuration, maxDuration + 1)

            val fetchedData = ioTask { remoteRepository.fetchData(taskDuration) }

            delayTask(taskDuration)

            return@backgroundTask TaskExecutionSuccess(fetchedData.toDouble())
        }
}
