package soutvoid.com.DsrWeatherApp.ui.screen.newLocation.stepper

import android.content.Context
import android.support.v4.app.FragmentManager
import com.stepstone.stepper.Step
import com.stepstone.stepper.adapter.AbstractFragmentStepAdapter
import soutvoid.com.DsrWeatherApp.ui.screen.newLocation.stepper.map.MapFragmentView
import soutvoid.com.DsrWeatherApp.ui.screen.newLocation.stepper.settings.LocationSettingsFragmentView

class StepperAdapter(val fm: FragmentManager, context: Context)
    : AbstractFragmentStepAdapter(fm, context) {

    override fun getCount(): Int = 2

    override fun createStep(position: Int): Step {
        when(position) {
            0 -> return MapFragmentView.newInstance()
            else -> return LocationSettingsFragmentView.newInstance()
        }
    }
}