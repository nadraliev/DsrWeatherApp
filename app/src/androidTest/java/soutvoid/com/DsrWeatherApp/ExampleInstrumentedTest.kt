package soutvoid.com.DsrWeatherApp

import android.support.test.InstrumentationRegistry
import android.support.test.runner.AndroidJUnit4
import com.mikepenz.weather_icons_typeface_library.WeatherIcons
import org.junit.Assert.assertEquals
import org.junit.Test
import org.junit.runner.RunWith
import soutvoid.com.DsrWeatherApp.ui.util.WeatherIconsHelper
import soutvoid.com.DsrWeatherApp.ui.util.WindUtils

/**
 * Instrumentation test, which will execute on an Android device.

 * @see [Testing documentation](http://d.android.com/tools/testing)
 */
@RunWith(AndroidJUnit4::class)
class ExampleInstrumentedTest {
    @Test
    @Throws(Exception::class)
    fun useAppContext() {
        // Context of the app under test.
        val appContext = InstrumentationRegistry.getTargetContext()

        assertEquals("soutvoid.com.dsrweatherapp", appContext.packageName)
    }

    @Test
    fun windDirections() {
        val context = InstrumentationRegistry.getTargetContext()
        val directions = listOf<String>("N", "NE", "E", "SE", "S", "SW", "W", "NW")
        val icons = listOf<WeatherIcons.Icon>(
                WeatherIcons.Icon.wic_direction_down,
                WeatherIcons.Icon.wic_direction_down_left,
                WeatherIcons.Icon.wic_direction_right,
                WeatherIcons.Icon.wic_direction_up_left,
                WeatherIcons.Icon.wic_direction_up,
                WeatherIcons.Icon.wic_direction_up_right,
                WeatherIcons.Icon.wic_direction_left,
                WeatherIcons.Icon.wic_direction_down_right)
        for (i in 0..360) {
            assertEquals(
                    directions.indexOf(WindUtils.getFromByDegrees(i.toDouble(), context)),
                    icons.indexOf(WeatherIconsHelper.getDirectionalIcon(i.toDouble()))
            )
        }
    }
}
