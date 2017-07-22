package soutvoid.com.DsrWeatherApp.ui.screen.newLocation

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.support.v4.content.ContextCompat
import com.agna.ferro.mvp.component.ScreenComponent
import kotlinx.android.synthetic.main.activity_new_location.*
import soutvoid.com.DsrWeatherApp.R
import soutvoid.com.DsrWeatherApp.ui.base.activity.BaseActivityView
import soutvoid.com.DsrWeatherApp.ui.base.activity.BasePresenter
import soutvoid.com.DsrWeatherApp.ui.screen.newLocation.stepper.StepperAdapter
import javax.inject.Inject

class NewLocationActivityView : BaseActivityView() {

    companion object {
        fun start(context: Context) {
            context.startActivity(Intent(context, NewLocationActivityView::class.java))
        }
    }

    @Inject
    lateinit var presenter: NewLocationActivityPresenter

    override fun getPresenter(): BasePresenter<*> = presenter

    override fun getName(): String = "NewLocation"

    override fun getContentView(): Int = R.layout.activity_new_location

    override fun createScreenComponent(): ScreenComponent<*> {
        return DaggerNewLocationActivityComponent.builder()
                .activityModule(activityModule)
                .appComponent(appComponent)
                .build()
    }

    override fun onCreate(savedInstanceState: Bundle?, viewRecreated: Boolean) {
        super.onCreate(savedInstanceState, viewRecreated)

        initToolbar()
        initStepper()
    }

    private fun initToolbar() {
        setSupportActionBar(new_location_toolbar)
        title = getString(R.string.new_location)
        new_location_toolbar.navigationIcon = ContextCompat.getDrawable(this, R.drawable.ic_arrow_back_light)
        new_location_toolbar.setNavigationOnClickListener { onBackPressed() }
    }

    private fun initStepper() {
        new_location_stepper.adapter = StepperAdapter(supportFragmentManager, this)
    }
}