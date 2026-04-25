package me.rezapour.domain.model

data class Routine(
    val id: Long = 0,
    val name: String,
    val workSeconds: Long,
    val restSeconds: Long,
    val rounds: Int
) {
    val workMilliSecond
        get() = workSeconds * 1000

    val restMilliSecond
        get() = restSeconds * 1000


}