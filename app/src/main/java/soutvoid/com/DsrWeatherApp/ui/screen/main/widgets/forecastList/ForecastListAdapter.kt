package soutvoid.com.DsrWeatherApp.ui.screen.main.widgets.forecastList

import android.support.v7.widget.RecyclerView
import android.view.View
import android.view.ViewGroup
import android.widget.LinearLayout.LayoutParams
import butterknife.BindView
import butterknife.ButterKnife
import soutvoid.com.DsrWeatherApp.R
import soutvoid.com.DsrWeatherApp.domain.ThreeHoursForecast
import soutvoid.com.DsrWeatherApp.ui.screen.main.widgets.forecastList.widgets.TemperatureGraphView
import soutvoid.com.DsrWeatherApp.ui.util.dpToPx
import soutvoid.com.DsrWeatherApp.ui.util.inflate

class ForecastListAdapter(var forecasts: List<ThreeHoursForecast> = emptyList())
    : RecyclerView.Adapter<ForecastListAdapter.ForecastViewHolder>() {

    private val minTemp: Double by lazy {
        forecasts.minBy { it.main.temperature }?.main?.temperature ?: .0
    }

    private val maxTemp: Double by lazy {
        forecasts.maxBy { it.main.temperature }?.main?.temperature ?: .0
    }

    private val tempGraphHeight: Int by lazy {
        calculateTempGraphHeight()
    }

    override fun onCreateViewHolder(parent: ViewGroup?, viewType: Int): ForecastViewHolder? {
        val view = parent?.inflate(R.layout.forecast_list_item)
        view?.let { initTempGraph(it.findViewById(R.id.tempGraph)) }
        return view?.let { ForecastViewHolder(it) }
    }

    override fun onBindViewHolder(holder: ForecastViewHolder?, position: Int) {
        holder?.bind(forecasts.getOrNull(position - 1), forecasts[position], forecasts.getOrNull(position + 1))
    }

    override fun getItemCount(): Int = forecasts.size

    /**
     * высота графика в зависимости от разброса температуры
     * @return высота в dp
     */
    private fun calculateTempGraphHeight(): Int {
        return (maxTemp - minTemp).toInt() * 8
    }

    private fun initTempGraph(view: TemperatureGraphView) {
        view.layoutParams = LayoutParams(150, view.dpToPx(tempGraphHeight.toDouble()).toInt())
        view.maxTemp = maxTemp
        view.minTemp = minTemp
    }

    class ForecastViewHolder(view: View): RecyclerView.ViewHolder(view) {

        @BindView(R.id.tempGraph)
        lateinit var graph: TemperatureGraphView

        init {
            ButterKnife.bind(this, view)
        }

        fun bind(prevForecast: ThreeHoursForecast? = null, currentForecast: ThreeHoursForecast, nextForecast: ThreeHoursForecast? = null) {
            graph.prevTemp = prevForecast?.main?.temperature
            graph.currentTemp = currentForecast.main.temperature
            graph.nextTemp = nextForecast?.main?.temperature
        }

    }

}