package soutvoid.com.DsrWeatherApp.ui.screen.cities.pager

import android.os.Bundle
import android.support.v4.app.Fragment

class LocationsFragment : Fragment() {

    companion object {
        private const val ONLY_FAVORITE_KEY = "only_favorite"

        fun newInstance(showOnlyFavorite: Boolean = false) : LocationsFragment {
            val citiesFragment = LocationsFragment()
            citiesFragment.arguments = Bundle()
            citiesFragment.arguments.putBoolean(ONLY_FAVORITE_KEY, showOnlyFavorite)
            return citiesFragment
        }
    }


}