package soutvoid.com.DsrWeatherApp.ui.screen.cities

import com.agna.ferro.mvp.component.ScreenComponent
import soutvoid.com.DsrWeatherApp.R
import soutvoid.com.DsrWeatherApp.ui.base.activity.BaseActivityView
import soutvoid.com.DsrWeatherApp.ui.base.activity.BasePresenter
import javax.inject.Inject

class CitiesActivityView: BaseActivityView() {

    @Inject
    lateinit var presenter: CitiesActivityPresenter

    override fun getPresenter(): BasePresenter<*> = presenter

    override fun createScreenComponent(): ScreenComponent<*> {
        return DaggerCitiesActivityComponent.builder()
                .activityModule(activityModule)
                .appComponent(appComponent)
                .build()
    }

    override fun getContentView(): Int = R.layout.activity_cities

    override fun getName(): String = "CitiesActivity"
}