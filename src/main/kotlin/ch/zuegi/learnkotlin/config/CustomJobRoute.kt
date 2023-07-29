package ch.zuegi.learnkotlin.config

import ch.zuegi.learnkotlin.feature.job.JobHandler
import kotlinx.coroutines.FlowPreview
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.web.reactive.function.server.coRouter

@Configuration
class CustomJobRoute(private val jobHandler: JobHandler) {

    @FlowPreview
    @Bean
    fun jobRoute() = coRouter {
        GET("/api/job/list", jobHandler::listJobs)
        POST("/api/job/add", jobHandler::addJob)
    }

}
