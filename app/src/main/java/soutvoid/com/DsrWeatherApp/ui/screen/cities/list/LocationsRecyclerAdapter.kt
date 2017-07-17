package soutvoid.com.DsrWeatherApp.ui.screen.cities.list

import android.support.v7.widget.RecyclerView
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import butterknife.BindView
import butterknife.ButterKnife
import soutvoid.com.DsrWeatherApp.R
import soutvoid.com.DsrWeatherApp.domain.CurrentWeather
import soutvoid.com.DsrWeatherApp.domain.location.Location
import soutvoid.com.DsrWeatherApp.ui.screen.cities.widget.FavoriteButton
import soutvoid.com.DsrWeatherApp.ui.util.UnitsUtils
import soutvoid.com.DsrWeatherApp.ui.util.inflate

class LocationsRecyclerAdapter(var locations: List<Location> = emptyList(),
                               var currentWeathers: List<CurrentWeather> = emptyList(),
                               var onClick: (Int) -> Unit)
    : RecyclerView.Adapter<LocationsRecyclerAdapter.LocationsViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup?, viewType: Int): LocationsViewHolder? {
        val view = parent?.inflate(R.layout.locations_list_item)
        return view?.let { LocationsViewHolder(view, onClick) }
    }

    override fun onBindViewHolder(holder: LocationsViewHolder?, position: Int) {
        holder?.bind(locations[position], currentWeathers[position])
    }

    override fun getItemCount(): Int = locations.size

    class LocationsViewHolder(view: View, var onClick: (Int) -> Unit)
        : RecyclerView.ViewHolder(view) {

        @BindView(R.id.locations_item_name)
        lateinit var name: TextView
        @BindView(R.id.locations_item_current_temp)
        lateinit var currentTemp: TextView
        @BindView(R.id.locations_item_favorite_btn)
        lateinit var favoriteBtn: FavoriteButton

        init {
            ButterKnife.bind(this, itemView)
            itemView.setOnClickListener { onClick(adapterPosition) }
        }

        fun bind(location: Location, currentWeather: CurrentWeather) {
            name.text = location.name
            currentTemp.text = "${Math.round(currentWeather.main.temperature)}${UnitsUtils.getDegreesUnits(itemView.context)}"
            favoriteBtn.isChecked = location.isFavorite
        }

    }

}