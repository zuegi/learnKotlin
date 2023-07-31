package learn.actors.actor

import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.InternalCoroutinesApi
import kotlinx.coroutines.asCoroutineDispatcher
import kotlinx.coroutines.channels.produce
import learn.actors.model.*
import java.time.Instant
import java.util.concurrent.Executors
import kotlin.random.Random


    private val context = Executors.newFixedThreadPool(5, NamedThreadFactory("producer")).asCoroutineDispatcher()

    @InternalCoroutinesApi
    @ExperimentalCoroutinesApi
    fun CoroutineScope.sourceActor(total: Int) = produce<ActorsMessage<RawData>>(
        context,
        capacity = 10,
        onCompletion = {
            context.close() // close context on stopping the actor
            log("🛑 Completed. Exception: $it")
        }
    ) {
        for (i in 1..total) {
            val rawData = RawData(randomDouble(), randomDouble())
            val result = ActorsMessage(rawData, ActorsMetaData(Instant.now().toEpochMilli()))
            log("🐥Producing $result")
            channel.send(result)
        }
        channel.close()
    }

    private fun randomDouble() = Random.nextDouble(0.01, 3.5)
