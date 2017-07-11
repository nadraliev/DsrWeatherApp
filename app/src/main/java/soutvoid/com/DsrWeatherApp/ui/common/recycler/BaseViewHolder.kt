package soutvoid.com.DsrWeatherApp.ui.common.recycler

import android.support.v7.widget.RecyclerView
import android.view.View

import butterknife.ButterKnife

abstract class BaseViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {

    init {
        ButterKnife.bind(this, itemView)
    }
}
