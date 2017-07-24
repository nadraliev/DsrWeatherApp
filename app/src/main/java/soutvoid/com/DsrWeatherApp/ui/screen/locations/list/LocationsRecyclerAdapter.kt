package soutvoid.com.DsrWeatherApp.ui.screen.locations.list

import android.support.v7.widget.RecyclerView
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import butterknife.BindView
import butterknife.ButterKnife
import soutvoid.com.DsrWeatherApp.R
import soutvoid.com.DsrWeatherApp.domain.CurrentWeather
import soutvoid.com.DsrWeatherApp.domain.location.SavedLocation
import soutvoid.com.DsrWeatherApp.ui.screen.locations.pager.data.LocationWithWeather
import soutvoid.com.DsrWeatherApp.ui.screen.locations.widget.FavoriteButton
import soutvoid.com.DsrWeatherApp.ui.util.UnitsUtils
import soutvoid.com.DsrWeatherApp.ui.util.inflate

/**
 * адаптер для списка сохраненных точек
 * @param [savedLocations] список сохраненных точек
 * @param [currentWeathers] погода для сохраненных точек
 * @param [onClick] слушатель нажатия на элемент списка
 * @param [favoriteStateChangedListener] слушатель нажатия на кнопку "сердце"
 */
class LocationsRecyclerAdapter(var locations: MutableList<LocationWithWeather> = mutableListOf(),
                               var onClick: (Int) -> Unit,
                               var favoriteStateChangedListener: (position: Int, state: Boolean) -> Unit)
    : RecyclerView.Adapter<LocationsRecyclerAdapter.LocationsViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup?, viewType: Int): LocationsViewHolder? {
        val view = parent?.inflate(R.layout.locations_list_item)
        return view?.let { LocationsViewHolder(view, onClick, favoriteStateChangedListener) }
    }

    override fun onBindViewHolder(holder: LocationsViewHolder?, position: Int) {
        holder?.bind(locations[position].location, locations[position].currentWeather)
    }

    override fun getItemCount(): Int = locations.size


    class LocationsViewHolder(view: View,
                              var onClick: (Int) -> Unit,
                              var stateChangedListener: (position: Int, state: Boolean) -> Unit)
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
            favoriteBtn.setOnCheckedChangeListener {
                compoundButton, _ ->
                stateChangedListener(adapterPosition, compoundButton.isChecked)
            }
        }

        fun bind(savedLocation: SavedLocation, currentWeather: CurrentWeather) {
            name.text = savedLocation.name
            currentTemp.text = "${Math.round(currentWeather.main.temperature)}${UnitsUtils.getDegreesUnits(itemView.context)}"
            favoriteBtn.isChecked = savedLocation.isFavorite
        }

    }

}