package me.rezapour.common.extentionfunctions

fun String.toIntOrZero(): Int = this.toIntOrNull() ?: 0

fun String.toLongOrZero(): Long = this.toLongOrNull() ?: 0L

fun String.digitOnly(maxLen: Int? = null): String {
    val digits = this.filter { it.isDigit() }
    return if (maxLen != null) digits.take(maxLen) else digits
}