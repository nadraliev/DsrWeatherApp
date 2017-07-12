package soutvoid.com.DsrWeatherApp.ui.util

import com.mikepenz.weather_icons_typeface_library.WeatherIcons

object WeatherIconsHelper {

    fun getWeatherIcon(id: Int, dt: Long, sunrise: Long, sunset: Long) : WeatherIcons.Icon {
        var night: Boolean = dt in sunset..sunrise

        if (night)
            return getNightWeatherIcon(id)
        else return getDayWeatherIcon(id)
    }

    fun getDayWeatherIcon(id : Int) : WeatherIcons.Icon {
        when (id) {
            200, 201 -> return WeatherIcons.Icon.wic_day_storm_showers
            202 -> return WeatherIcons.Icon.wic_day_thunderstorm
            in 210..221 -> return WeatherIcons.Icon.wic_day_lightning
            in 230..232 -> return WeatherIcons.Icon.wic_day_storm_showers

            in 300..302 -> return WeatherIcons.Icon.wic_day_sprinkle
            in 310..312 -> return WeatherIcons.Icon.wic_day_rain
            in 313..321 -> return WeatherIcons.Icon.wic_day_showers

            500 -> return WeatherIcons.Icon.wic_day_sprinkle
            in 501..504 -> return WeatherIcons.Icon.wic_day_rain
            511 -> return WeatherIcons.Icon.wic_day_rain_mix
            in 520..531 -> return WeatherIcons.Icon.wic_day_showers

            in 600..602 -> return WeatherIcons.Icon.wic_day_snow
            611, 612 -> return WeatherIcons.Icon.wic_day_sleet
            in 615..622 -> return WeatherIcons.Icon.wic_day_rain_mix

            701 -> return WeatherIcons.Icon.wic_day_haze
            711 -> return WeatherIcons.Icon.wic_smoke
            721 -> return WeatherIcons.Icon.wic_day_haze
            731 -> return WeatherIcons.Icon.wic_sandstorm
            741 -> return WeatherIcons.Icon.wic_day_fog
            751, 761 -> return WeatherIcons.Icon.wic_dust
            762 -> return WeatherIcons.Icon.wic_volcano
            771 -> return WeatherIcons.Icon.wic_strong_wind
            781 -> return WeatherIcons.Icon.wic_tornado

            800 -> return WeatherIcons.Icon.wic_day_sunny
            801, 802 -> return WeatherIcons.Icon.wic_day_cloudy
            803 -> return WeatherIcons.Icon.wic_day_cloudy
            804 -> return WeatherIcons.Icon.wic_day_sunny_overcast

            900 -> return WeatherIcons.Icon.wic_tornado
            901 -> return WeatherIcons.Icon.wic_strong_wind
            902 -> return WeatherIcons.Icon.wic_hurricane
            903 -> return WeatherIcons.Icon.wic_snowflake_cold
            904 -> return WeatherIcons.Icon.wic_hot
            905 -> return WeatherIcons.Icon.wic_day_windy
            906 -> return WeatherIcons.Icon.wic_day_hail

            in 951..953 -> return WeatherIcons.Icon.wic_day_light_wind
            in 954..955 -> return WeatherIcons.Icon.wic_day_windy
            in 956..961 -> return WeatherIcons.Icon.wic_strong_wind
            962 -> return WeatherIcons.Icon.wic_hurricane
        }
        return WeatherIcons.Icon.wic_day_sunny
    }

    fun getNightWeatherIcon(id : Int) : WeatherIcons.Icon {
        when (id) {
            200, 201 -> return WeatherIcons.Icon.wic_night_storm_showers
            202 -> return WeatherIcons.Icon.wic_night_thunderstorm
            in 210..221 -> return WeatherIcons.Icon.wic_night_lightning
            in 230..232 -> return WeatherIcons.Icon.wic_night_storm_showers

            in 300..302 -> return WeatherIcons.Icon.wic_night_sprinkle
            in 310..312 -> return WeatherIcons.Icon.wic_night_rain
            in 313..321 -> return WeatherIcons.Icon.wic_night_showers

            500 -> return WeatherIcons.Icon.wic_night_sprinkle
            in 501..504 -> return WeatherIcons.Icon.wic_night_rain
            511 -> return WeatherIcons.Icon.wic_night_rain_mix
            in 520..531 -> return WeatherIcons.Icon.wic_night_showers

            in 600..602 -> return WeatherIcons.Icon.wic_night_snow
            611, 612 -> return WeatherIcons.Icon.wic_night_sleet
            in 615..622 -> return WeatherIcons.Icon.wic_night_rain_mix

            701 -> return WeatherIcons.Icon.wic_day_haze
            711 -> return WeatherIcons.Icon.wic_smoke
            721 -> return WeatherIcons.Icon.wic_day_haze
            731 -> return WeatherIcons.Icon.wic_sandstorm
            741 -> return WeatherIcons.Icon.wic_night_fog
            751, 761 -> return WeatherIcons.Icon.wic_dust
            762 -> return WeatherIcons.Icon.wic_volcano
            771 -> return WeatherIcons.Icon.wic_strong_wind
            781 -> return WeatherIcons.Icon.wic_tornado

            800 -> return WeatherIcons.Icon.wic_night_clear
            801, 802 -> return WeatherIcons.Icon.wic_night_cloudy
            803 -> return WeatherIcons.Icon.wic_night_cloudy
            804 -> return WeatherIcons.Icon.wic_night_cloudy

            900 -> return WeatherIcons.Icon.wic_tornado
            901 -> return WeatherIcons.Icon.wic_strong_wind
            902 -> return WeatherIcons.Icon.wic_hurricane
            903 -> return WeatherIcons.Icon.wic_snowflake_cold
            904 -> return WeatherIcons.Icon.wic_hot
            905 -> return WeatherIcons.Icon.wic_night_cloudy_windy
            906 -> return WeatherIcons.Icon.wic_night_hail

            in 951..953 -> return WeatherIcons.Icon.wic_night_cloudy_windy
            in 954..955 -> return WeatherIcons.Icon.wic_night_cloudy_windy
            in 956..961 -> return WeatherIcons.Icon.wic_strong_wind
            962 -> return WeatherIcons.Icon.wic_hurricane
        }
        return WeatherIcons.Icon.wic_night_clear
    }

    fun getNeutralWeatherIcon(id : Int) : WeatherIcons.Icon {
        when(id) {
            200,201 -> return WeatherIcons.Icon.wic_storm_showers
            202 -> return WeatherIcons.Icon.wic_thunderstorm
            in 210..221 -> return WeatherIcons.Icon.wic_lightning
            in 230..232 -> return WeatherIcons.Icon.wic_storm_showers

            in 300..302 -> return WeatherIcons.Icon.wic_sprinkle
            in 310..312 -> return WeatherIcons.Icon.wic_rain
            in 313..321 -> return WeatherIcons.Icon.wic_showers

            500 -> return WeatherIcons.Icon.wic_sprinkle
            in 501..504 -> return WeatherIcons.Icon.wic_rain
            511 -> return WeatherIcons.Icon.wic_rain_mix
            in 520..531 -> return WeatherIcons.Icon.wic_showers

            in 600..602 -> return WeatherIcons.Icon.wic_snow
            611, 612 -> return WeatherIcons.Icon.wic_sleet
            in 615..622 -> return WeatherIcons.Icon.wic_rain_mix

            701 -> return WeatherIcons.Icon.wic_day_haze
            711 -> return WeatherIcons.Icon.wic_smoke
            721 -> return WeatherIcons.Icon.wic_day_haze
            731 -> return WeatherIcons.Icon.wic_sandstorm
            741 -> return WeatherIcons.Icon.wic_fog
            751,761 -> return WeatherIcons.Icon.wic_dust
            762 -> return WeatherIcons.Icon.wic_volcano
            771 -> return WeatherIcons.Icon.wic_strong_wind
            781 -> return WeatherIcons.Icon.wic_tornado

            800 -> return WeatherIcons.Icon.wic_day_sunny
            801, 802 -> return WeatherIcons.Icon.wic_cloud
            803 -> return WeatherIcons.Icon.wic_cloudy
            804 -> return WeatherIcons.Icon.wic_day_sunny_overcast

            900 -> return WeatherIcons.Icon.wic_tornado
            901 -> return WeatherIcons.Icon.wic_strong_wind
            902 -> return WeatherIcons.Icon.wic_hurricane
            903 -> return WeatherIcons.Icon.wic_snowflake_cold
            904 -> return WeatherIcons.Icon.wic_hot
            905 -> return WeatherIcons.Icon.wic_windy
            906 -> return WeatherIcons.Icon.wic_hail

            951 -> return WeatherIcons.Icon.wic_cloud
            in 952..955 -> return WeatherIcons.Icon.wic_windy
            in 956..961 -> return WeatherIcons.Icon.wic_strong_wind
            962 -> return WeatherIcons.Icon.wic_hurricane
        }
        return WeatherIcons.Icon.wic_cloud
    }

    fun getDirectionalIcon(degrees: Double) : WeatherIcons.Icon {
        when (degrees) {
            in 27..71 -> return WeatherIcons.Icon.wic_direction_down_right
            in 72..116 -> return WeatherIcons.Icon.wic_cloud_down
            in 117..162 -> return WeatherIcons.Icon.wic_direction_down_left
            in 163..208 -> return WeatherIcons.Icon.wic_direction_left
            in 209..254 -> return WeatherIcons.Icon.wic_direction_up_left
            in 255..300 -> return WeatherIcons.Icon.wic_direction_up
            in 301..345 -> return WeatherIcons.Icon.wic_direction_up_right
        }
        return WeatherIcons.Icon.wic_direction_right
    }

}