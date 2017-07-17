package soutvoid.com.DsrWeatherApp.ui.screen.locations.pager

import android.content.Context
import android.support.v4.app.Fragment
import android.support.v4.app.FragmentManager
import android.support.v4.app.FragmentStatePagerAdapter
import android.util.SparseArray
import android.view.View
import android.view.ViewGroup
import soutvoid.com.DsrWeatherApp.R
import java.lang.ref.WeakReference

class LocationsPagerAdapter(fragmentManager: FragmentManager,
                            val context: Context)
    : FragmentStatePagerAdapter(fragmentManager) {

    val registeredFragments: SparseArray<WeakReference<LocationsFragmentView>> = SparseArray()

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

    override fun instantiateItem(container: ViewGroup?, position: Int): Any {
        val fragment = super.instantiateItem(container, position) as LocationsFragmentView
        registeredFragments.put(position, WeakReference(fragment))
        return fragment
    }

    override fun destroyItem(container: ViewGroup?, position: Int, `object`: Any?) {
        registeredFragments.remove(position)
        super.destroyItem(container, position, `object`)
    }

    fun notifyOtherFragmentDataSetChanged(currentPosition: Int) {
        var otherFragmentPos : Int = 0
        if (currentPosition == 0)
            otherFragmentPos = 1
        val otherFragment = registeredFragments[otherFragmentPos]
        otherFragment.get()?.onDataSetChanged()
    }
}