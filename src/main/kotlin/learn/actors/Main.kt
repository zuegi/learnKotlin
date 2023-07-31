package learn.actors

import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.InternalCoroutinesApi
import kotlinx.coroutines.cancelChildren
import kotlinx.coroutines.runBlocking
import learn.actors.actor.calculateActor
import learn.actors.actor.sinkActor
import learn.actors.actor.sourceActor
import learn.actors.model.log
import kotlin.system.measureTimeMillis

const val total = 15

@InternalCoroutinesApi
@ExperimentalCoroutinesApi
fun main() {
    val time = measureTimeMillis {
        runBlocking {

            val raw = sourceActor(total)
            val enriched = calculateActor(raw)
            val sunk = sinkActor(enriched)

            var counter = 0
            for (msg in sunk) {
                counter++
                log("🏁 Processed ${counter} : ${msg}")
            }
            log("The End")
            coroutineContext.cancelChildren()
        }
    }
    println("Done in $time ms")

}

