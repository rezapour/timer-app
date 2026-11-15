package me.rezapour.ui.formatter

object TimerDurationFormatter {
    fun formatToLabelBreakLine(seconds: Long): String {
        val mins = seconds / 60
        val seconds = seconds % 60

        return when {
            mins != 0L && seconds != 0L -> "${mins}min\n${seconds}s"
            mins != 0L -> "${mins}min"
            else -> "${seconds}s"
        }
    }

    fun formatForToMMSS(seconds: Long): String {
        val minutes = (seconds / 60)
        val seconds = (seconds % 60)

        val minStr = minutes.toString().padStart(2, '0')
        val secStr = seconds.toString().padStart(2, '0')

        return "$minStr:$secStr"
    }

    fun formatForLabel(seconds: Long): String {
        val mins = seconds / 60
        val seconds = seconds % 60

        return when {
            mins != 0L && seconds != 0L -> "${mins}min ${seconds}s"
            mins != 0L -> "${mins} min"
            else -> "${seconds}s"
        }
    }
}