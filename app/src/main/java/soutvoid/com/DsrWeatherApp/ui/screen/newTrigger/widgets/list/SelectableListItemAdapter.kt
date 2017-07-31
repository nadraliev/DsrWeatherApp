package soutvoid.com.DsrWeatherApp.ui.screen.newTrigger.widgets.list

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.BaseAdapter
import android.widget.ImageView
import android.widget.TextView
import soutvoid.com.DsrWeatherApp.R
import soutvoid.com.DsrWeatherApp.ui.util.ifNotNullOr

class SelectableListItemAdapter(private val context: Context,
                                val entries: List<String>): BaseAdapter() {

    var selectedPosition = 0

    override fun getView(position: Int, convertView: View?, parent: ViewGroup?): View {
        var view = convertView.ifNotNullOr(
                LayoutInflater.from(context).inflate(R.layout.selectable_list_item, parent, false)
        )

        view.findViewById<TextView>(R.id.selectable_list_item_text).text = entries[position]

        val checkIv = view.findViewById<ImageView>(R.id.factors_list_item_check)

        if (position == selectedPosition)
            checkIv.visibility = View.VISIBLE
        else
            checkIv.visibility = View.INVISIBLE

        view.findViewById<View>(R.id.selectable_list_item_container).setOnClickListener {
            selectedPosition = position
            notifyDataSetChanged()
        }

        return view
    }

    override fun getItem(p0: Int): Any = entries[p0]

    override fun getItemId(p0: Int): Long = p0.toLong()

    override fun getCount(): Int = entries.size


}