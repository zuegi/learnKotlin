package learn.actors.actor

import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.InternalCoroutinesApi
import kotlinx.coroutines.asCoroutineDispatcher
import kotlinx.coroutines.channels.ReceiveChannel
import kotlinx.coroutines.channels.produce
import learn.actors.model.*
import java.util.concurrent.Executors

private val context = Executors.newFixedThreadPool(2, NamedThreadFactory("calculator")).asCoroutineDispatcher()

@ExperimentalCoroutinesApi
@InternalCoroutinesApi
fun CoroutineScope.calculateActor(inbox: ReceiveChannel<ActorsMessage<RawData>>): ReceiveChannel<ActorsMessage<RichData>> =
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
            log("🥁 Processing $msg")
            val result = transformMessage(msg) { calculate(msg.payload) }
            log("🥁 Calculated $result")
            channel.send(result) // send to next
        }

    }
private fun calculate(rawData: RawData): RichData {
    val bid = rawData.bid
    val ask = rawData.ask
    val mid = (bid + ask) / 2
    return RichData(bid, ask, mid)
}

