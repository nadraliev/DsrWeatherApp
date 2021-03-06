package soutvoid.com.DsrWeatherApp.ui.screen.main.locations.list

import android.support.v7.widget.RecyclerView
import android.view.View
import android.view.ViewGroup
import android.widget.PopupMenu
import android.widget.TextView
import butterknife.BindView
import butterknife.ButterKnife
import soutvoid.com.DsrWeatherApp.R
import soutvoid.com.DsrWeatherApp.domain.CurrentWeather
import soutvoid.com.DsrWeatherApp.domain.location.SavedLocation
import soutvoid.com.DsrWeatherApp.ui.screen.main.locations.pager.data.LocationWithWeather
import soutvoid.com.DsrWeatherApp.ui.screen.main.locations.widget.FavoriteButton
import soutvoid.com.DsrWeatherApp.ui.util.UnitsUtils
import soutvoid.com.DsrWeatherApp.ui.util.inflate

/**
 * адаптер для списка сохраненных точек
 * @param [locations] список сохраненных точек и погоды для них
 * @param [onClick] слушатель нажатия на элемент списка
 * @param [favoriteStateChangedListener] слушатель нажатия на кнопку "сердце"
 */
class LocationsRecyclerAdapter(var locations: MutableList<LocationWithWeather> = mutableListOf(),
                               var onClick: (Int) -> Unit,
                               var favoriteStateChangedListener: (position: Int, state: Boolean) -> Unit,
                               var editBtnClickedListener: (position: Int) -> Unit,
                               var removeBtnClickedListener: (position: Int) -> Unit)
    : RecyclerView.Adapter<LocationsRecyclerAdapter.LocationsViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup?, viewType: Int): LocationsViewHolder? {
        val view = parent?.inflate(R.layout.locations_list_item)
        return view?.let {
            LocationsViewHolder(
                    view,
                    onClick,
                    favoriteStateChangedListener,
                    editBtnClickedListener,
                    removeBtnClickedListener)
        }
    }

    override fun onBindViewHolder(holder: LocationsViewHolder?, position: Int) {
        holder?.bind(locations[position].location, locations[position].currentWeather)
    }

    override fun getItemCount(): Int = locations.size


    class LocationsViewHolder(view: View,
                              var onClick: (Int) -> Unit,
                              var stateChangedListener: (position: Int, state: Boolean) -> Unit,
                              var editBtnClickedListener: (position: Int) -> Unit,
                              var removeBtnClickedListener: (position: Int) -> Unit)
        : RecyclerView.ViewHolder(view) {

        @BindView(R.id.locations_item_name)
        lateinit var name: TextView
        @BindView(R.id.locations_item_current_temp)
        lateinit var currentTemp: TextView
        @BindView(R.id.locations_item_current_temp_hint)
        lateinit var currentTempHint: TextView
        @BindView(R.id.locations_item_favorite_btn)
        lateinit var favoriteBtn: FavoriteButton
        @BindView(R.id.locations_item_more_btn)
        lateinit var moreBtn: View

        init {
            ButterKnife.bind(this, itemView)
            itemView.setOnClickListener { onClick(adapterPosition) }
            itemView.setOnLongClickListener {
                showPopup()
                return@setOnLongClickListener true
            }
            favoriteBtn.setOnCheckedChangeListener {
                compoundButton, _ ->
                stateChangedListener(adapterPosition, compoundButton.isChecked)
            }
            moreBtn.setOnClickListener { showPopup() }
        }

        fun bind(savedLocation: SavedLocation, currentWeather: CurrentWeather?) {
            name.text = savedLocation.name
            maybeBindCurrentTemp(currentWeather)
            favoriteBtn.isChecked = savedLocation.isFavorite
        }

        private fun maybeBindCurrentTemp(currentWeather: CurrentWeather?) {
            if (currentWeather != null) {
                val diffHours = Math.abs(currentWeather.timeOfData - System.currentTimeMillis() / 1000) / 60 / 60
                if (diffHours == 0L) {
                    currentTemp.text = "${Math.round(currentWeather.main.temperature)}${UnitsUtils.getDegreesUnits(itemView.context)}"
                    currentTempHint.visibility = View.VISIBLE
                } else currentTempHint.visibility = View.GONE
            } else currentTempHint.visibility = View.GONE
        }

        private fun showPopup() {
            val popup = PopupMenu(itemView.context, moreBtn)
            popup.inflate(R.menu.locations_item_context_menu)
            popup.setOnMenuItemClickListener {
                if (it.itemId == R.id.locations_item_menu_remove) {
                    removeBtnClickedListener(adapterPosition)
                    return@setOnMenuItemClickListener true
                }
                else if (it.itemId == R.id.locations_item_menu_edit) {
                    editBtnClickedListener(adapterPosition)
                    return@setOnMenuItemClickListener true
                }
                return@setOnMenuItemClickListener false
            }
            popup.show()
        }

    }

}