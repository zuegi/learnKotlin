package learn.actors.model

import java.time.Instant
import java.util.UUID
import java.util.concurrent.ThreadFactory
import java.util.concurrent.atomic.AtomicInteger

data class RawData(val bid: Double, val ask: Double)

data class RichData(val bid: Double, val ask: Double, val mid: Double)

data class ActorsMetaData(val timestampMillis: Long, val correlationId: UUID = UUID.randomUUID())

data class ActorsMessage<T>( val payload: T, val metaData: ActorsMetaData)

fun <T, R> transformMessage(input: ActorsMessage<T>, block: (T) -> R): ActorsMessage<R> {
    val result = block(input.payload)
    return ActorsMessage(result, input.metaData)
}

fun log(msg: String) = println("${Instant.now()} [${Thread.currentThread().name}] $msg")


class NamedThreadFactory(private val prefix: String) : ThreadFactory {
    private val counter = AtomicInteger()
    override fun newThread(r: Runnable): Thread {
        val t = Thread(r)
        t.name = "${this.prefix}-${counter.getAndIncrement()}"
        return t
    }
}

