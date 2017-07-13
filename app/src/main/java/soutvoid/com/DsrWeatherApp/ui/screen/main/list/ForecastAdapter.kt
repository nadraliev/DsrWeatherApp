package soutvoid.com.DsrWeatherApp.ui.screen.main.list

import android.support.v7.widget.RecyclerView
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import butterknife.BindView
import butterknife.ButterKnife
import com.mikepenz.iconics.IconicsDrawable
import soutvoid.com.DsrWeatherApp.R
import soutvoid.com.DsrWeatherApp.domain.OneDayForecast
import soutvoid.com.DsrWeatherApp.domain.OneMomentForecast
import soutvoid.com.DsrWeatherApp.ui.util.*

class ForecastAdapter(var dailyForecasts: List<OneDayForecast> = ArrayList()) : RecyclerView.Adapter<ForecastAdapter.ForecastViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup?, viewType: Int): ForecastViewHolder? {
        val view: View? = parent?.inflate(R.layout.daily_forecast_list_item)
        return view?.let { ForecastViewHolder(it) }
    }

    override fun onBindViewHolder(holder: ForecastViewHolder?, position: Int) {
        holder?.bind(dailyForecasts[position])
    }

    override fun getItemCount(): Int = dailyForecasts.size

    class ForecastViewHolder(view: View) : RecyclerView.ViewHolder(view) {

        @BindView(R.id.daily_forecast_date)
        lateinit var date: TextView
        @BindView(R.id.daily_forecast_icon)
        lateinit var icon: ImageView
        @BindView(R.id.daily_forecast_max_temp)
        lateinit var maxTemp: TextView
        @BindView(R.id.daily_forecast_min_temp)
        lateinit var minTemp: TextView
        @BindView(R.id.daily_forecast_expand_btn)
        lateinit var expandBtn: View

        init {
            ButterKnife.bind(this, view)
            expandBtn.setOnClickListener {  }//TODO("expand here")
        }

        fun bind(oneDayForecast: OneDayForecast) {
            with(oneDayForecast) {
                date.text = CalendarUtils.getFormattedDate(dateTime)
                icon.setImageDrawable(IconicsDrawable(itemView.context)
                        .icon(WeatherIconsHelper.getWeatherIcon(weather[0].id, dateTime))
                        .color(itemView.context.theme.getThemeColor(android.R.attr.textColorPrimary))
                        .sizeDp(48)
                )
                maxTemp.text = "${Math.round(temperature.max)} ${UnitsUtils.getDegreesUnits(itemView.context)}"
                minTemp.text = "${Math.round(temperature.min)} ${UnitsUtils.getDegreesUnits(itemView.context)}"
            }
        }

    }

}