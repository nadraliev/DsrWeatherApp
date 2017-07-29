package soutvoid.com.DsrWeatherApp.ui.screen.newTrigger

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.support.v4.content.ContextCompat
import android.support.v7.app.AlertDialog
import android.util.TypedValue
import android.widget.ArrayAdapter
import android.widget.Toast
import com.agna.ferro.mvp.component.ScreenComponent
import soutvoid.com.DsrWeatherApp.R
import soutvoid.com.DsrWeatherApp.ui.base.activity.BasePresenter
import soutvoid.com.DsrWeatherApp.ui.common.activity.TranslucentStatusActivityView
import kotlinx.android.synthetic.main.activity_new_trigger.*
import soutvoid.com.DsrWeatherApp.ui.screen.newLocation.NewLocationActivityView
import soutvoid.com.DsrWeatherApp.ui.util.DialogUtils
import soutvoid.com.DsrWeatherApp.ui.util.getThemeColor
import javax.inject.Inject

class NewTriggerActivityView: TranslucentStatusActivityView() {

    companion object {
        fun start(context: Context) {
            context.startActivity(Intent(context, NewTriggerActivityView::class.java))
        }
    }

    @Inject
    lateinit var presenter: NewTriggerActivityPresenter

    override fun getPresenter(): BasePresenter<*> = presenter

    override fun getContentView(): Int = R.layout.activity_new_trigger

    override fun getName(): String = "New trigger"

    override fun createScreenComponent(): ScreenComponent<*> {
        return DaggerNewTriggerActivityComponent.builder()
                .appComponent(appComponent)
                .activityModule(activityModule)
                .build()
    }

    override fun onCreate(savedInstanceState: Bundle?, viewRecreated: Boolean) {
        super.onCreate(savedInstanceState, viewRecreated)

        initToolbar()
        initListeners()
    }

    private fun initToolbar() {
        setSupportActionBar(new_trigger_toolbar)
        title = getString(R.string.new_notification)
        val typedValue = TypedValue()
        theme.resolveAttribute(R.attr.themedBackDrawable, typedValue, true)
        new_trigger_toolbar.navigationIcon = ContextCompat.getDrawable(this, typedValue.resourceId)
        new_trigger_toolbar.setNavigationOnClickListener { onBackPressed() }
    }

    private fun initListeners() {
        new_trigger_location.setOnClickListener { presenter.onLocationClicked() }
    }

    fun setLocationName(locationName: String) {
        new_trigger_location.text = locationName
    }
    
    fun setLocationNameChoosable() {
        new_trigger_location.setTextColor(this.theme.getThemeColor(android.R.attr.textColorSecondary))
        new_trigger_location.text = getString(R.string.choose_location)
    }

    fun showNoLocationsDialog() {
        DialogUtils.showNoLocationsDialog(this) { NewLocationActivityView.start(this) }
    }

    fun showLocationsDialog(locationsNames: List<String>) {
        DialogUtils.showLocationsDialog(this, locationsNames) { presenter.onLocationChosen(it) }
    }
}