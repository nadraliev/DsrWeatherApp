package soutvoid.com.DsrWeatherApp.ui.screen.main.data

enum class TimeOfDay(val hours: Int) {
    MORNING(8),
    DAY(14),
    EVENING(19),
    NIGHT(23);

    companion object {
        fun getByTime(hours: Int) : TimeOfDay {
            when(hours) {
                in 0..11 -> return MORNING
                in 12..16 -> return DAY
                in 17..21 -> return EVENING
                in 22..23 -> return NIGHT
            }
            return MORNING
        }
    }
}