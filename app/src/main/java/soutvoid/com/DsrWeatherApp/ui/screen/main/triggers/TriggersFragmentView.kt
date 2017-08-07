package soutvoid.com.DsrWeatherApp.ui.screen.main.triggers

import android.os.Bundle
import android.support.v4.content.ContextCompat
import android.support.v7.widget.DividerItemDecoration
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.helper.ItemTouchHelper
import android.util.TypedValue
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.agna.ferro.mvp.component.ScreenComponent
import soutvoid.com.DsrWeatherApp.R
import soutvoid.com.DsrWeatherApp.ui.base.activity.BaseActivityView
import soutvoid.com.DsrWeatherApp.ui.base.activity.BasePresenter
import javax.inject.Inject
import kotlinx.android.synthetic.main.fragment_triggers.*
import soutvoid.com.DsrWeatherApp.domain.triggers.SavedTrigger
import soutvoid.com.DsrWeatherApp.ui.base.fragment.BaseFragmentView
import soutvoid.com.DsrWeatherApp.ui.screen.newTrigger.NewTriggerActivityView
import soutvoid.com.DsrWeatherApp.ui.screen.main.triggers.list.TriggersListAdapter
import soutvoid.com.DsrWeatherApp.ui.util.SimpleItemSwipeCallback
import soutvoid.com.DsrWeatherApp.ui.util.inflate

class TriggersFragmentView : BaseFragmentView() {

    @Inject
    lateinit var presenter: TriggersFragmentPresenter

    lateinit var adapter: TriggersListAdapter

    override fun getPresenter(): BasePresenter<*> = presenter

    override fun getName(): String = "Triggers"

    override fun createScreenComponent(): ScreenComponent<*> {
        return DaggerTriggersFragmentComponent.builder()
                .appComponent(getAppComponent())
                .fragmentModule(getFragmentModule())
                .build()
    }

    override fun onCreateView(inflater: LayoutInflater?, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return container?.inflate(R.layout.fragment_triggers)
    }

    override fun onViewCreated(view: View?, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        initToolbar()
        initList()
        initAddBtn()
    }

    private fun initToolbar() {
        activity.title = getString(R.string.notifications)
    }

    private fun initList() {
        adapter = TriggersListAdapter(
                onItemClickListener = { presenter.onTriggerClicked(adapter.triggers[it]) },
                onSwitchClickListener = { presenter.onSwitchClicked(adapter.triggers[it], it) },
                onDeleteBtnClickListener = { triggerRemoveRequest(it) }
        )
        triggers_list.adapter = adapter
        triggers_list.layoutManager = LinearLayoutManager(activity, LinearLayoutManager.VERTICAL, false)
        triggers_list.addItemDecoration(DividerItemDecoration(activity, DividerItemDecoration.VERTICAL))
        val simpleItemTouchCallback = SimpleItemSwipeCallback(ItemTouchHelper.LEFT or ItemTouchHelper.RIGHT,
                {viewHolder, direction -> triggerRemoveRequest(viewHolder.adapterPosition)})
        ItemTouchHelper(simpleItemTouchCallback).attachToRecyclerView(triggers_list)
    }

    private fun initAddBtn() {
        triggers_add.setOnClickListener { NewTriggerActivityView.start(activity) }
    }

    fun showData(triggers: List<SavedTrigger>) {
        adapter.triggers = triggers.toMutableList()
        adapter.notifyDataSetChanged()
    }

    fun triggerRemoveRequest(position: Int) {
        presenter.onTriggerRemoveRequested(adapter.triggers[position], position)
        adapter.triggers.removeAt(position)
        adapter.notifyItemRemoved(position)
    }

    fun addLocationToPosition(savedTrigger: SavedTrigger, position: Int) {
        adapter.triggers.add(position, savedTrigger)
        adapter.notifyItemInserted(position)
    }

    fun showNoNotificationsMessage() {
        triggers_placeholder.show(
                getString(R.string.no_notifications_message),
                getEmptyImageId())
    }

    fun hidePlaceholder() {
        triggers_placeholder.hide()
    }

    fun getEmptyImageId(): Int {
        val typedValue = TypedValue()
        context.theme.resolveAttribute(R.attr.themedEmptyDrawable, typedValue, true)
        return typedValue.resourceId
    }

}