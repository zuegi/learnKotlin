package ch.zuegi.learnkotlin.feature.job

import kotlinx.serialization.Serializable

@Serializable
data class JobDto(
    val name: String,
    val taskDto: CustomTaskDto
)

@Serializable
data class CustomTaskDto(val name: String)

