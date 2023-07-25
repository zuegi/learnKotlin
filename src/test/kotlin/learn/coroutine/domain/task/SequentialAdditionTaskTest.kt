package learn.coroutine.domain.task

import kotlinx.coroutines.runBlocking
import learn.coroutine.domain.AbstractBaseTaskTest
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test

class SequentialAdditionTaskTest : AbstractBaseTaskTest() {

    private lateinit var subject: SequentialAdditionTask

    @BeforeEach
    fun before() {
        subject = SequentialAdditionTask()
    }

    @Test
    fun `should do anything`() {
        runBlocking {
            val execute = subject.execute(0.2, 0.3)
            println("execute: $execute")
        }

    }

}
