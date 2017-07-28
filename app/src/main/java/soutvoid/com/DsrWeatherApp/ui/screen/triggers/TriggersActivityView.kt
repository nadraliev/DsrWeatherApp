package soutvoid.com.DsrWeatherApp.ui.screen.triggers

import android.os.Bundle
import android.support.v4.content.ContextCompat
import android.support.v7.widget.DividerItemDecoration
import android.support.v7.widget.LinearLayoutManager
import android.util.TypedValue
import com.agna.ferro.mvp.component.ScreenComponent
import soutvoid.com.DsrWeatherApp.R
import soutvoid.com.DsrWeatherApp.ui.base.activity.BaseActivityView
import soutvoid.com.DsrWeatherApp.ui.base.activity.BasePresenter
import javax.inject.Inject
import kotlinx.android.synthetic.main.activity_triggers.*
import soutvoid.com.DsrWeatherApp.domain.triggers.SavedTrigger
import soutvoid.com.DsrWeatherApp.ui.screen.newTrigger.NewTriggerActivityView
import soutvoid.com.DsrWeatherApp.ui.screen.triggers.list.TriggersListAdapter

class TriggersActivityView: BaseActivityView() {

    @Inject
    lateinit var presenter: TriggersActivityPresenter

    lateinit var adapter: TriggersListAdapter

    override fun getPresenter(): BasePresenter<*> = presenter

    override fun getContentView(): Int = R.layout.activity_triggers

    override fun getName(): String = "Triggers"

    override fun createScreenComponent(): ScreenComponent<*> {
        return DaggerTriggersActivityComponent.builder()
                .appComponent(appComponent)
                .activityModule(activityModule)
                .build()
    }

    override fun onCreate(savedInstanceState: Bundle?, viewRecreated: Boolean) {
        super.onCreate(savedInstanceState, viewRecreated)

        initToolbar()
        initList()
        initAddBtn()
    }

    private fun initToolbar() {
        setSupportActionBar(triggers_toolbar)
        title = getString(R.string.notifications)
        val typedValue = TypedValue()
        theme.resolveAttribute(R.attr.themedBackDrawable, typedValue, true)
        triggers_toolbar.navigationIcon = ContextCompat.getDrawable(this, typedValue.resourceId)
        triggers_toolbar.setNavigationOnClickListener { onBackPressed() }
    }

    private fun initList() {
        adapter = TriggersListAdapter(
                onItemClickListener = { presenter.onTriggerClicked(adapter.triggers[it]) },
                onSwitchClickListener = { position, state ->  presenter.onSwitchClicked(adapter.triggers[position], state) }
        )
        triggers_list.adapter = adapter
        triggers_list.layoutManager = LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false)
        triggers_list.addItemDecoration(DividerItemDecoration(this, DividerItemDecoration.VERTICAL))
    }

    private fun initAddBtn() {
        triggers_add.setOnClickListener { NewTriggerActivityView.start(this) }
    }

    fun showData(triggers: List<SavedTrigger>) {
        adapter.triggers = triggers
        adapter.notifyDataSetChanged()
    }

}