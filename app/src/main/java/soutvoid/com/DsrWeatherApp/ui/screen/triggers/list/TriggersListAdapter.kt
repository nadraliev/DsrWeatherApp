package soutvoid.com.DsrWeatherApp.ui.screen.triggers.list

import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Switch
import android.widget.TextView
import butterknife.BindView
import butterknife.ButterKnife
import com.google.android.flexbox.FlexboxLayout
import soutvoid.com.DsrWeatherApp.R
import soutvoid.com.DsrWeatherApp.domain.location.SavedLocation
import soutvoid.com.DsrWeatherApp.domain.triggers.SavedTrigger
import soutvoid.com.DsrWeatherApp.domain.triggers.condition.ConditionExpression
import soutvoid.com.DsrWeatherApp.domain.triggers.condition.ConditionName
import soutvoid.com.DsrWeatherApp.domain.triggers.condition.SavedCondition
import soutvoid.com.DsrWeatherApp.ui.util.UnitsUtils
import soutvoid.com.DsrWeatherApp.ui.util.getNiceNameStringId
import soutvoid.com.DsrWeatherApp.ui.util.inflate

class TriggersListAdapter(
        var triggers: MutableList<SavedTrigger> = mutableListOf(),
        var onItemClickListener: (position: Int) -> Unit,
        var onSwitchClickListener: (position: Int, state: Boolean) -> Unit,
        var onDeleteBtnClickListener: (position: Int) -> Unit
)
    : RecyclerView.Adapter<TriggersListAdapter.TriggerViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup?, viewType: Int): TriggerViewHolder? {
        val view = parent?.inflate(R.layout.triggers_list_item)
        return view?.let { TriggerViewHolder(it, onItemClickListener, onSwitchClickListener, onDeleteBtnClickListener) }
    }

    override fun getItemCount(): Int = triggers.size

    override fun onBindViewHolder(holder: TriggerViewHolder?, position: Int) {
        holder?.bind(triggers[position])
    }

    class TriggerViewHolder(view: View,
                            onItemClickListener: (position: Int) -> Unit,
                            onSwitchClickListener: (position: Int, state: Boolean) -> Unit,
                            var onDeleteBtnClickListener: (position: Int) -> Unit)
        : RecyclerView.ViewHolder(view) {

        @BindView(R.id.triggers_list_item_name)
        lateinit var nameTv: TextView
        var switch: Switch //for some reason butterknife can't bind this switch in time
        @BindView(R.id.triggers_list_item_details)
        lateinit var details: FlexboxLayout
        @BindView(R.id.triggers_list_item_delete)
        lateinit var delete: View

        init {
            ButterKnife.bind(this, itemView)
            switch = itemView.findViewById(R.id.triggers_list_item_switch)
            itemView.setOnClickListener { onItemClickListener(adapterPosition) }
            switch.setOnCheckedChangeListener { compoundButton, b -> onSwitchClickListener(adapterPosition, compoundButton.isChecked) }
            delete.setOnClickListener { onDeleteBtnClickListener(adapterPosition) }
        }

        fun bind(trigger: SavedTrigger) {
            with(trigger) {
                nameTv.text = name
                switch.isChecked = enabled
                details.removeAllViews()
                details.addView(getLocationDetailVew(location))
                conditions.forEach { details.addView(getConditionDetailView(it)) }
            }
        }

        fun getLocationDetailVew(location: SavedLocation): View {
            val view = LayoutInflater.from(itemView.context).inflate(R.layout.detail_view, null, false)
            view.findViewById<TextView>(R.id.detail_view_text).text = location.name
            return view
        }

        fun getConditionDetailView(savedCondition: SavedCondition): View {
            val view = LayoutInflater.from(itemView.context).inflate(R.layout.detail_view, null, false)
            val name = itemView.context.getString(ConditionName.valueOf(savedCondition.name).getNiceNameStringId()).capitalize()
            val symbol = ConditionExpression.valueOf(savedCondition.expression).symbol
            var value = savedCondition.amount.toInt()
            if (ConditionName.valueOf(savedCondition.name) == ConditionName.temp)
                value = Math.round(UnitsUtils.kelvinToPreferredUnit(itemView.context, savedCondition.amount)).toInt()
            view.findViewById<TextView>(R.id.detail_view_text).text = "$name $symbol $value"
            return view
        }

    }

}