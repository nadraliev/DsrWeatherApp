package soutvoid.com.DsrWeatherApp.ui.screen.newTrigger.widgets.conditionDialog

import android.app.AlertDialog
import android.app.Dialog
import android.app.DialogFragment
import android.os.Bundle
import android.support.design.widget.TextInputLayout
import android.widget.ListView
import soutvoid.com.DsrWeatherApp.R
import soutvoid.com.DsrWeatherApp.domain.triggers.condition.Condition
import soutvoid.com.DsrWeatherApp.domain.triggers.condition.ConditionExpression
import soutvoid.com.DsrWeatherApp.domain.triggers.condition.ConditionName
import soutvoid.com.DsrWeatherApp.ui.screen.newTrigger.widgets.list.SelectableListItemAdapter
import soutvoid.com.DsrWeatherApp.ui.util.*

class ChooseConditionDialog() : DialogFragment() {

    var conditionChosenListener: ((Condition?) -> Unit)? = null

    private lateinit var conditions: List<ConditionName>
    private lateinit var conditionsNames: List<String>

    private lateinit var factors: List<ConditionExpression>
    private lateinit var factorsNames: List<String>

    private var inputField: TextInputLayout? = null
    private var factorsAdapter: SelectableListItemAdapter? = null

    private var chosenCondition = -1    //выбранное условие. (-1 - не выбрано)

    constructor(conditionChosenListener: ((Condition?) -> Unit)?) : this() {
        this.conditionChosenListener = conditionChosenListener
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
        conditions = ConditionName.values()
                .filter { it != ConditionName.wind_direction }
        conditionsNames = conditions.map { activity.getString(it.getNiceNameStringId()).capitalize() }

        factors = listOf(ConditionExpression.gt, ConditionExpression.lt, ConditionExpression.eq)
        factorsNames = factors.map { activity.getString(it.getNiceStringId()) }
    }

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        if (chosenCondition < 0) { //показать диалог с именами условий
            val items = conditionsNames.plusElementFront(getString(R.string.no_condition))
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
        if (position == 0) {    //первая опция - без состояния
            conditionChosenListener?.invoke(null)
            dismiss()
        } else {
            chosenCondition = position - 1
            dismiss()
            show(fragmentManager, "")
        }
    }

    private fun createSecondDialog(): Dialog {
        val builder = AlertDialog.Builder(activity)
        val view = activity.layoutInflater.inflate(R.layout.condition_factors_dialog, null)
        builder.setView(view)

        val list = view.findViewById<ListView>(R.id.conditions_factors_dialog_list)
        factorsAdapter = SelectableListItemAdapter(activity, factorsNames.map { "${conditionsNames[chosenCondition]} $it" })
        list.adapter = factorsAdapter

        inputField = view.findViewById<TextInputLayout>(R.id.conditions_factors_dialog_input_field)

        builder.setPositiveButton(R.string.ok) { _, _ -> }

        return builder.create()
    }

    private fun onOkPressed() {
        ifNotNull(inputField, factorsAdapter)
        { input, adapter ->
            if (input.editText?.text == null || input.editText?.text.toString() == "")
                input.error = activity.getString(R.string.enter_value)
            else {
                val value = input.editText?.text.toString().toDouble()
                val condition = Condition(
                        conditions[chosenCondition],
                        factors[adapter.selectedPosition],
                        UnitsUtils.preferredUnitToKelvin(activity, value)
                )
                conditionChosenListener?.invoke(condition)
                dismiss()
            }
        }
    }
}