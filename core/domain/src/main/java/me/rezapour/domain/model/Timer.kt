package me.rezapour.domain.model

data class Timer(
    val id: Long = 0,
    val name: String,
    val workSeconds: Long,
    val restSeconds: Long,
    val rounds: Int
)