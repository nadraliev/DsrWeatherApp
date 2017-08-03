package soutvoid.com.DsrWeatherApp.ui.screen.weather.data

enum class TimeOfDay(val hours: Int) {
    MORNING(8),
    DAY(14),
    EVENING(19),
    NIGHT(23);

    companion object {
        fun getByTime(hours: Int) : TimeOfDay {
            when(hours) {
                in 5..11 -> return MORNING
                in 12..17 -> return DAY
                in 18..23 -> return EVENING
            }
            return NIGHT
        }
    }
}