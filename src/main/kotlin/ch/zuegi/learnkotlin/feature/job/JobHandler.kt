package ch.zuegi.learnkotlin.feature.job

import kotlinx.coroutines.delay
import learn.coroutine.backgroundTask
import learn.coroutine.customJob
import learn.logger
import org.springframework.stereotype.Component
import org.springframework.web.reactive.function.server.ServerRequest
import org.springframework.web.reactive.function.server.ServerResponse
import org.springframework.web.reactive.function.server.awaitBody
import org.springframework.web.reactive.function.server.bodyValueAndAwait

@Component
class JobHandler {
    private val log by logger()

    suspend fun addJob(req: ServerRequest): ServerResponse {
        log.info("job.addJob")
        val jobDto = req.awaitBody<JobDto>()
        log.info("jobDto: $jobDto")

        customJob {
            log.info("${jobDto.name} -> JOB_LAUNCHED")
            backgroundTask {
                log.info("${jobDto.taskDto.name} -> SYNC_TASK_LAUNCHED")
                delay(500)
                log.info("${jobDto.taskDto.name} -> SYNC_TASK_FINISHED")
            }
            log.info("${jobDto.name} -> JOB_FINISHED")
        }

        log.info("bevor ServierResponse")
        return ServerResponse.ok().bodyValueAndAwait("Job added")
    }

    suspend fun listJobs(req: ServerRequest): ServerResponse = ServerResponse.ok().bodyValueAndAwait("Jobs listed")
}
