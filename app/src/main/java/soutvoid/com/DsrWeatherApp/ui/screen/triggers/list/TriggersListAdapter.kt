package soutvoid.com.DsrWeatherApp.ui.screen.triggers.list

import android.support.v7.widget.RecyclerView
import android.view.View
import android.view.ViewGroup
import android.widget.Switch
import android.widget.TextView
import butterknife.BindView
import butterknife.ButterKnife
import com.google.android.flexbox.FlexboxLayout
import kotlinx.android.synthetic.main.triggers_list_item.*
import soutvoid.com.DsrWeatherApp.R
import soutvoid.com.DsrWeatherApp.domain.location.SavedLocation
import soutvoid.com.DsrWeatherApp.domain.triggers.SavedTrigger
import soutvoid.com.DsrWeatherApp.domain.triggers.condition.SavedCondition
import soutvoid.com.DsrWeatherApp.ui.util.inflate

class TriggersListAdapter(
        var triggers: List<SavedTrigger> = emptyList(),
        var onItemClickListener: (position: Int) -> Unit,
        var onSwitchClickListener: (position: Int, state: Boolean) -> Unit
)
    : RecyclerView.Adapter<TriggersListAdapter.TriggerViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup?, viewType: Int): TriggerViewHolder? {
        val view = parent?.inflate(R.layout.triggers_list_item)
        return view?.let { TriggerViewHolder(it, onItemClickListener, onSwitchClickListener) }
    }

    override fun getItemCount(): Int = triggers.size

    override fun onBindViewHolder(holder: TriggerViewHolder?, position: Int) {
        holder?.bind(triggers[position])
    }

    class TriggerViewHolder(view: View,
                            onItemClickListener: (position: Int) -> Unit,
                            onSwitchClickListener: (position: Int, state: Boolean) -> Unit)
        : RecyclerView.ViewHolder(view) {

        @BindView(R.id.triggers_list_item_name)
        lateinit var nameTv: TextView
        var switch: Switch //for some reason butterknife can't bind this switch in time
        @BindView(R.id.triggers_list_item_details)
        lateinit var details: FlexboxLayout

        init {
            ButterKnife.bind(this, itemView)
            switch = itemView.findViewById(R.id.triggers_list_item_switch)
            itemView.setOnClickListener { onItemClickListener(adapterPosition) }
            switch.setOnCheckedChangeListener { compoundButton, b -> onSwitchClickListener(adapterPosition, compoundButton.isChecked) }
        }

        fun bind(trigger: SavedTrigger) {
            with(trigger) {
                nameTv.text = name
                switch.isChecked = enabled
                details.addView(getLocationDetailVew(location))
                conditions.forEach { details.addView(getConditionDetailView(it)) }
            }
        }

        fun getLocationDetailVew(location: SavedLocation): TextView {
            val view = TextView(itemView.context)
            view.text = location.name
            return view
        }

        fun getConditionDetailView(savedCondition: SavedCondition): TextView {
            val view = TextView(itemView.context)
            view.text = "${savedCondition.name} ${savedCondition.expression} ${savedCondition.amount}"
            return view
        }

    }

}