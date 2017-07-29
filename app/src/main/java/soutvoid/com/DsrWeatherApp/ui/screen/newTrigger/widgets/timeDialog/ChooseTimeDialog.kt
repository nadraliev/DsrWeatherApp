package soutvoid.com.DsrWeatherApp.ui.screen.newTrigger.widgets.timeDialog

import android.app.AlertDialog
import android.app.Dialog
import android.app.DialogFragment
import android.os.Bundle
import android.support.design.widget.TextInputLayout
import android.widget.ListView
import soutvoid.com.DsrWeatherApp.R
import soutvoid.com.DsrWeatherApp.domain.triggers.condition.Condition
import soutvoid.com.DsrWeatherApp.ui.screen.newTrigger.widgets.list.SelectableListItemAdapter
import soutvoid.com.DsrWeatherApp.ui.screen.newTrigger.widgets.timeDialog.data.NotificationTime
import soutvoid.com.DsrWeatherApp.ui.util.*

class ChooseTimeDialog(): DialogFragment() {

    var timeChosenListener: ((NotificationTime?) -> Unit)? = null

    private lateinit var quickOptions: List<NotificationTime>

    private var inputField: TextInputLayout? = null
    private var unitsAdapter: SelectableListItemAdapter? = null

    private var customChosen = false

    private val maxDays = 5
    private val maxHours = 24

    constructor(conditionChosenListener: ((NotificationTime?) -> Unit)?) : this() {
        this.timeChosenListener = conditionChosenListener
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        initData()
    }

    override fun onResume() {
        super.onResume()

        (dialog as? AlertDialog)?.let {
            it.getButton(Dialog.BUTTON_POSITIVE).setOnClickListener {
                onOkPressed()
            }
        }
    }

    private fun initData() {
        quickOptions = listOf(
                NotificationTime(5, NotificationTime.Unit.days),
                NotificationTime(2, NotificationTime.Unit.days),
                NotificationTime(1, NotificationTime.Unit.days),
                NotificationTime(10, NotificationTime.Unit.hours),
                NotificationTime(2, NotificationTime.Unit.hours)
        )
    }

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        if (!customChosen) { //показать диалог с быстрыми опциями
            val items = quickOptions
                    .map { "${it.getNiceString(activity)} ${activity.getString(R.string.before)}" }
                    .plusElementFront(activity.getString(R.string.no_time))
                    .plusElement(activity.getString(R.string.custom))
            return DialogUtils.createSimpleListDialog(
                    activity,
                    items = items,
                    listener = { showSecondDialog(it) }
            )
        } else {    //показать диалог с выбором фактора
            return createSecondDialog()
        }
    }

    private fun showSecondDialog(position: Int) {
        if (position == 0) {    //первая опция - без времени
            timeChosenListener?.invoke(null)
            dismiss()
        } else if(position == quickOptions.size + 1) {  //выбрана опция своего времени
            customChosen = true
            dismiss()
            show(fragmentManager, "")
        } else {    //выбрана одна из быстрых опций
            val index = position - 1
            timeChosenListener?.invoke(quickOptions[index])
            dismiss()
        }
    }

    private fun createSecondDialog(): Dialog {
        val builder = AlertDialog.Builder(activity)
        val view = activity.layoutInflater.inflate(R.layout.time_custom_dialog, null)
        builder.setView(view)

        val list = view.findViewById<ListView>(R.id.time_custom_list)
        unitsAdapter = SelectableListItemAdapter(activity,
                NotificationTime.Unit.values().map { "${it.getNiceString(activity)} ${activity.getString(R.string.before)}" })
        list.adapter = unitsAdapter

        inputField = view.findViewById<TextInputLayout>(R.id.time_custom_input_field)

        builder.setPositiveButton(R.string.ok) { _, _ -> }

        return builder.create()
    }

    private fun onOkPressed() {
        ifNotNull(inputField, unitsAdapter)
        { input, adapter ->
            if (input.editText?.text == null || input.editText?.text.toString() == "")
                input.error = activity.getString(R.string.enter_value)
            else {
                val value = input.editText?.text.toString().toInt()
                val unit = NotificationTime.Unit.values()[adapter.selectedPosition]

                if (value > maxDays && unit == NotificationTime.Unit.days)
                    input.error =
                            "${activity.getString(R.string.max)} $maxDays ${activity.resources.getQuantityString(R.plurals.days, maxDays)}"
                else if (value > maxHours && unit == NotificationTime.Unit.hours)
                    input.error =
                            "${activity.getString(R.string.max)} $maxHours ${activity.resources.getQuantityString(R.plurals.hours, maxHours)}"
                else {
                    val notificationTime = NotificationTime(value, unit)
                    timeChosenListener?.invoke(notificationTime)
                    dismiss()
                }
            }
        }
    }

}