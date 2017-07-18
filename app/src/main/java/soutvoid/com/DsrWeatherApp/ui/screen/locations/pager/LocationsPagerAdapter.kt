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

/**
 * адаптер для табов списка точек
 */
class LocationsPagerAdapter(fragmentManager: FragmentManager,
                            val context: Context)
    : FragmentStatePagerAdapter(fragmentManager) {

    /**
     * храним ссылки на все зарегестрированные фрагменты для оповещения их об изменении данных
     * обернуто в WeakReference для предотвращения memory leak
     */
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

    /**
     * сохраняем состояние фрагментов
     */
    override fun instantiateItem(container: ViewGroup?, position: Int): Any {
        val fragment = super.instantiateItem(container, position) as LocationsFragmentView
        registeredFragments.put(position, WeakReference(fragment))
        return fragment
    }

    /**
     * удаляем запись о фрагменте после его удаления
     */
    override fun destroyItem(container: ViewGroup?, position: Int, `object`: Any?) {
        registeredFragments.remove(position)
        super.destroyItem(container, position, `object`)
    }

    /**
     * этот метод будет вызван, когда в одном фрагменте данные списка поменялись,
     * и нужно обновить данные списка в другом фрагменте
     * @see LocationsFragmentView.tryNotifyPagerDataSetChanged
     * @see LocationsFragmentView.onDataSetChanged
     */
    fun notifyOtherFragmentDataSetChanged(currentPosition: Int) {
        var otherFragmentPos : Int = 0
        if (currentPosition == 0)
            otherFragmentPos = 1
        val otherFragment = registeredFragments[otherFragmentPos]
        otherFragment.get()?.onDataSetChanged()
    }
}