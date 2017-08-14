package soutvoid.com.DsrWeatherApp.ui.screen.weather.widgets.forecastList

import android.graphics.Color
import android.graphics.Typeface
import android.support.v7.widget.RecyclerView
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import butterknife.BindView
import butterknife.ButterKnife
import com.mikepenz.iconics.IconicsDrawable
import soutvoid.com.DsrWeatherApp.R
import soutvoid.com.DsrWeatherApp.domain.ThreeHoursForecast
import soutvoid.com.DsrWeatherApp.ui.screen.weather.widgets.forecastList.widgets.TemperatureGraphView
import soutvoid.com.DsrWeatherApp.ui.util.*

class ForecastListAdapter(var forecasts: List<ThreeHoursForecast> = emptyList())
    : RecyclerView.Adapter<ForecastListAdapter.ForecastViewHolder>() {

    private val minTemp: Double by lazy {
        forecasts.minBy { it.main.temperature }?.main?.temperature ?: .0
    }

    private val maxTemp: Double by lazy {
        forecasts.maxBy { it.main.temperature }?.main?.temperature ?: .0
    }

    override fun onCreateViewHolder(parent: ViewGroup?, viewType: Int): ForecastViewHolder? {
        val view = parent?.inflate(R.layout.forecast_list_item)
        view?.let { initTempGraph(it.findViewById(R.id.forecast_item_graph)) }
        return view?.let { ForecastViewHolder(it) }
    }

    override fun onBindViewHolder(holder: ForecastViewHolder?, position: Int) {
        holder?.bind(forecasts.getOrNull(position - 1), forecasts[position], forecasts.getOrNull(position + 1))
    }

    override fun getItemCount(): Int = forecasts.size



    private fun initTempGraph(view: TemperatureGraphView) {
        view.setMinMaxTemp(minTemp, maxTemp)
    }

    class ForecastViewHolder(view: View): RecyclerView.ViewHolder(view) {

        @BindView(R.id.forecast_item_day_of_week)
        lateinit var dayOfWeek: TextView
        @BindView(R.id.forecast_item_date)
        lateinit var date: TextView
        @BindView(R.id.forecast_item_time)
        lateinit var time: TextView
        @BindView(R.id.forecast_item_icon)
        lateinit var icon: ImageView
        @BindView(R.id.forecast_item_graph)
        lateinit var graph: TemperatureGraphView
        @BindView(R.id.forecast_item_wind_icon)
        lateinit var windIcon: ImageView
        @BindView(R.id.forecast_item_wind)
        lateinit var windTv: TextView

        init {
            ButterKnife.bind(this, itemView)
        }

        fun bind(prevForecast: ThreeHoursForecast? = null, currentForecast: ThreeHoursForecast, nextForecast: ThreeHoursForecast? = null) {
            graph.isBold = getHourOfDay(currentForecast.timeOfData) in 11..13
            graph.prevTemp = prevForecast?.main?.temperature
            graph.currentTemp = currentForecast.main.temperature
            graph.nextTemp = nextForecast?.main?.temperature

            dayOfWeek.text = CalendarUtils.getShortDayOfWeek(currentForecast.timeOfData)
            if (graph.isBold)
                dayOfWeek.typeface = Typeface.DEFAULT_BOLD
            else dayOfWeek.typeface = Typeface.DEFAULT
            if (graph.isBold)
                itemView.setBackgroundColor(itemView.context.getThemeColor(R.attr.themedDividerColor))
            else itemView.setBackgroundColor(Color.TRANSPARENT)

            date.text = CalendarUtils.getNumericDate(currentForecast.timeOfData)

            time.text = CalendarUtils.getFormattedHour(currentForecast.timeOfData)

            icon.setImageDrawable(IconicsDrawable(itemView.context)
                    .icon(WeatherIconsHelper.getWeatherIcon(currentForecast.weather.first().id, currentForecast.timeOfData))
                    .color(itemView.context.getThemeColor(android.R.attr.textColorPrimary))
                    .sizeDp(38))

            windIcon.setImageDrawable(IconicsDrawable(itemView.context)
                    .icon(WeatherIconsHelper.getDirectionalIcon(currentForecast.wind.degrees))
                    .color(itemView.context.getThemeColor(android.R.attr.textColorPrimary))
                    .sizeDp(16))
            windTv.text = "${currentForecast.wind.speed} ${UnitsUtils.getVelocityUnits(itemView.context)}"
        }

    }

}