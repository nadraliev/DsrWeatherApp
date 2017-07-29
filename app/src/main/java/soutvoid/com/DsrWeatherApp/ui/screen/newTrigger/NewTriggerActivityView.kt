package soutvoid.com.DsrWeatherApp.ui.screen.newTrigger

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.support.annotation.StringRes
import android.support.v4.content.ContextCompat
import android.util.TypedValue
import android.widget.Toast
import com.agna.ferro.mvp.component.ScreenComponent
import soutvoid.com.DsrWeatherApp.R
import soutvoid.com.DsrWeatherApp.ui.base.activity.BasePresenter
import soutvoid.com.DsrWeatherApp.ui.common.activity.TranslucentStatusActivityView
import kotlinx.android.synthetic.main.activity_new_trigger.*
import soutvoid.com.DsrWeatherApp.ui.screen.newLocation.NewLocationActivityView
import soutvoid.com.DsrWeatherApp.ui.screen.newTrigger.widgets.conditionDialog.ChooseConditionDialog
import soutvoid.com.DsrWeatherApp.ui.screen.newTrigger.widgets.timeDialog.ChooseTimeDialog
import soutvoid.com.DsrWeatherApp.ui.screen.newTrigger.widgets.timeDialog.data.NotificationTime
import soutvoid.com.DsrWeatherApp.ui.util.DialogUtils
import soutvoid.com.DsrWeatherApp.ui.util.UnitsUtils
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
        initConditionsList()
        initTimeList()
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

    private fun initConditionsList() {
        new_trigger_conditions_container.addLineToTop(
                strId = R.string.add_condition,
                textSize = 18f,
                textColor = theme.getThemeColor(android.R.attr.textColorSecondary))
        { presenter.onAddConditionClicked() }
    }
    
    private fun initTimeList() {
        new_trigger_time_container.addLineToTop(
                strId = R.string.add_time,
                textSize = 18f,
                textColor = theme.getThemeColor(android.R.attr.textColorSecondary)
        )
        { presenter.onAddTimeClicked() }
    }

    private fun initListeners() {
        new_trigger_location.setOnClickListener { presenter.onLocationClicked() }
        new_trigger_check_btn.setOnClickListener { presenter.onCheckClicked() }
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

    fun showNewConditionsDialog() {
        ChooseConditionDialog(
                {presenter.onNewConditionChosen(it)}
        ).show(fragmentManager, "")
    }

    fun showEditConditionDialog(position: Int) {
        ChooseConditionDialog(
                {presenter.onConditionEdited(position, it)}
        ).show(fragmentManager, "")
    }

    fun showNewCondition(@StringRes name: Int, @StringRes expression: Int, amount: Int, isTemp: Boolean) {
        var value = amount
        if (isTemp)
            value = Math.round(UnitsUtils.kelvinToPreferredUnit(this, amount.toDouble())).toInt()
        val str = "${getString(name).capitalize()} ${getString(expression)} $value"
        new_trigger_conditions_container.addLineToIndex(new_trigger_conditions_container.size() - 1, str, textSize = 18f)
        { presenter.onConditionClicked(it) }
    }

    fun editCondition(position: Int, @StringRes name: Int, @StringRes expression: Int, amount: Int, isTemp: Boolean) {
        var value = amount
        if (isTemp)
            value = Math.round(UnitsUtils.kelvinToPreferredUnit(this, amount.toDouble())).toInt()
        val str = "${getString(name).capitalize()} ${getString(expression)} $value"
        new_trigger_conditions_container.getLine(position).text = str
    }

    fun removeCondition(position: Int) {
        new_trigger_conditions_container.removeLine(position)
    }

    fun showNewTimeDialog() {
        ChooseTimeDialog(
                {presenter.onNewTimeChosen(it)}
        ).show(fragmentManager, "")
    }

    fun showEditTimeDialog(position: Int) {
        ChooseTimeDialog(
                {presenter.onTimeEdited(position, it)}
        ).show(fragmentManager, "")
    }

    fun showNewTime(time: NotificationTime) {
        val str = "${time.getNiceString(this)} ${getString(R.string.before)}"
        new_trigger_time_container.addLineToIndex(new_trigger_time_container.size() - 1, str, textSize = 18f)
        { presenter.onTimeClicked(it) }
    }

    fun editTime(position: Int, time: NotificationTime) {
        val str = "${time.getNiceString(this)} ${getString(R.string.before)}"
        new_trigger_time_container.getLine(position).text = str
    }

    fun removeTime(position: Int) {
        new_trigger_time_container.removeLine(position)
    }

    fun getTypedName(): String {
        return new_trigger_name_edt.text.toString()
    }
}