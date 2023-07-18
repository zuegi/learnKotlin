package learn

import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking

class Main

//  coroutine builder that bridges the non-coroutine world of a regular fun main()
fun main() = runBlocking {
    launch {
//        delay(1000L)
        println("René")
    }
    println("Hi ")

}
