package learn.coroutine.repository

import java.io.IOException

class RemoteRepository {

    fun fetchData(input: Long): Long {
        return input * 10
    }

    fun fetchDataWithException(): Long {
        throw IOException("Error while reading repository data")
    }
}
