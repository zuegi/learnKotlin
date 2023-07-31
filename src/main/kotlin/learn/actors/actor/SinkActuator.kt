package learn.actors.actor

import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.InternalCoroutinesApi
import kotlinx.coroutines.asCoroutineDispatcher
import kotlinx.coroutines.channels.ReceiveChannel
import kotlinx.coroutines.channels.produce
import learn.actors.model.ActorsMessage
import learn.actors.model.NamedThreadFactory
import learn.actors.model.RichData
import learn.actors.model.log
import java.time.Instant
import java.util.concurrent.Executors

private val context = Executors.newFixedThreadPool(2, NamedThreadFactory("sink")).asCoroutineDispatcher()

@ExperimentalCoroutinesApi
@InternalCoroutinesApi
fun CoroutineScope.sinkActor(inbox: ReceiveChannel<ActorsMessage<RichData>>): ReceiveChannel<ActorsMessage<RichData>> =
    produce(
        context,
        capacity = 100,
        onCompletion = {
            context.close() // close context on stopping the actor
            log("🛑 Completed. Exception: $it")
        }
    )
    {
        for (msg in inbox) { // iterate over incoming messages
            val created = msg.metaData.timestampMillis
            log("📝 Writing $msg, processed in ${Instant.now().toEpochMilli() - created}ms")
            Thread.sleep(100) // to simulate blocking operation
            log("✅ Done with $msg")
            channel.send(msg)
        }
    }

