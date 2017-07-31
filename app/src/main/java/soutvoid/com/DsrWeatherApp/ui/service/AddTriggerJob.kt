package soutvoid.com.DsrWeatherApp.ui.service

import com.birbit.android.jobqueue.Job
import com.birbit.android.jobqueue.Params
import com.birbit.android.jobqueue.RetryConstraint
import soutvoid.com.DsrWeatherApp.app.dagger.AppComponent
import soutvoid.com.DsrWeatherApp.interactor.triggers.TriggersRepository
import javax.inject.Inject

class AddTriggerJob: BaseTriggerJob {

    constructor() : super(Params(1).requireNetwork())

    @Inject
    lateinit var triggerRep: TriggersRepository

    override fun onRun() {
        val a = 5
    }

    override fun shouldReRunOnThrowable(throwable: Throwable, runCount: Int, maxRunCount: Int): RetryConstraint {
        return RetryConstraint.RETRY
    }

    override fun onAdded() {

    }

    override fun onCancel(cancelReason: Int, throwable: Throwable?) {

    }

    override fun inject(appComponent: AppComponent) {
        super.inject(appComponent)
        appComponent.inject(this)
    }
}