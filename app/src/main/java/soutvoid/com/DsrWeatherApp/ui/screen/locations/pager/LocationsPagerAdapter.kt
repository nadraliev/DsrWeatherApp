package soutvoid.com.DsrWeatherApp.ui.screen.locations.pager

import android.content.Context
import android.support.v4.app.Fragment
import android.support.v4.app.FragmentManager
import android.support.v4.app.FragmentPagerAdapter
import soutvoid.com.DsrWeatherApp.R

class LocationsPagerAdapter(fragmentManager: FragmentManager,
                            val context: Context)
    : FragmentPagerAdapter(fragmentManager) {

    override fun getItem(position: Int): Fragment {
        when (position) {
            0 -> return LocationsFragmentView.newInstance()
            1 -> return LocationsFragmentView.newInstance(true)
        }
        return LocationsFragmentView.newInstance()
    }

    override fun getCount(): Int = 2

    override fun getPageTitle(position: Int): CharSequence {
        when (position) {
            0 -> return context.getString(R.string.all_locations)
            1 -> return context.getString(R.string.favorite)
        }
        return context.getString(R.string.all_locations)
    }
}