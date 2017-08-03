package soutvoid.com.DsrWeatherApp.ui.screen.newLocation

import android.annotation.SuppressLint
import android.content.Context
import android.content.Intent
import android.graphics.Point
import android.os.Bundle
import android.support.v4.content.ContextCompat
import android.util.TypedValue
import android.view.View
import com.agna.ferro.mvp.component.ScreenComponent
import kotlinx.android.synthetic.main.activity_new_location.*
import soutvoid.com.DsrWeatherApp.R
import soutvoid.com.DsrWeatherApp.ui.base.activity.BasePresenter
import soutvoid.com.DsrWeatherApp.ui.common.activity.TranslucentStatusActivityView
import soutvoid.com.DsrWeatherApp.ui.screen.main.MainActivityView
import soutvoid.com.DsrWeatherApp.ui.screen.main.locations.LocationsFragmentView
import soutvoid.com.DsrWeatherApp.ui.screen.newLocation.stepper.StepperAdapter
import soutvoid.com.DsrWeatherApp.ui.util.AnimationEndedListener
import soutvoid.com.DsrWeatherApp.ui.util.createFullScreenCircularReveal
import soutvoid.com.DsrWeatherApp.util.SdkUtil
import javax.inject.Inject

class NewLocationActivityView : TranslucentStatusActivityView() {

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
        val typedValue = TypedValue()
        theme.resolveAttribute(R.attr.themedBackDrawable, typedValue, true)
        new_location_toolbar.navigationIcon = ContextCompat.getDrawable(this, typedValue.resourceId)
        new_location_toolbar.setNavigationOnClickListener { onBackPressed() }
    }

    private fun initStepper() {
        new_location_stepper.adapter = StepperAdapter(supportFragmentManager, this)
    }


    /**
     * возвратиться на домашний экран. с api 21 с анимацией в центре в точке @param [animationCenter]
     */
    @SuppressLint("NewApi")
    fun returnToHome(animationCenter: Point = Point()) {
        if (SdkUtil.supportsLollipop()) {
            try {
                val animator = new_location_reveal_view.createFullScreenCircularReveal(
                        animationCenter.x,
                        new_location_stepper.top + animationCenter.y)
                animator.addListener(AnimationEndedListener {
                    startMainActivity()
                    overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out)
                })
                new_location_reveal_view.visibility = View.VISIBLE
                animator.start()
            } catch (e: NoClassDefFoundError) {
                startMainActivity()
            }
        } else startMainActivity()
    }

    private fun startMainActivity() {
        val intent = Intent(this, MainActivityView::class.java)
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP)
        startActivity(intent)
    }
}