package soutvoid.com.DsrWeatherApp.app.dagger

import android.app.job.JobService
import android.content.Context
import com.agna.ferro.mvp.component.scope.PerApplication
import com.birbit.android.jobqueue.JobManager
import dagger.Component
import soutvoid.com.DsrWeatherApp.interactor.common.jobs.JobManagerModule
import soutvoid.com.DsrWeatherApp.interactor.common.network.NetworkModule
import soutvoid.com.DsrWeatherApp.interactor.common.network.OkHttpModule
import soutvoid.com.DsrWeatherApp.interactor.common.network.cache.CacheModule
import soutvoid.com.DsrWeatherApp.interactor.currentWeather.CurrentWeatherModule
import soutvoid.com.DsrWeatherApp.interactor.currentWeather.CurrentWeatherRepository
import soutvoid.com.DsrWeatherApp.interactor.forecast.ForecastModule
import soutvoid.com.DsrWeatherApp.interactor.forecast.ForecastRepository
import soutvoid.com.DsrWeatherApp.interactor.network.NetworkConnectionChecker
import soutvoid.com.DsrWeatherApp.interactor.triggers.TriggersModule
import soutvoid.com.DsrWeatherApp.interactor.triggers.TriggersRepository
import soutvoid.com.DsrWeatherApp.interactor.uvi.UviModule
import soutvoid.com.DsrWeatherApp.interactor.uvi.UviRepository
import soutvoid.com.DsrWeatherApp.ui.base.activity.ActivityModule
import soutvoid.com.DsrWeatherApp.ui.widget.SmallWidgetProvider
import soutvoid.com.DsrWeatherApp.interactor.triggers.jobs.BaseTriggerJob
import soutvoid.com.DsrWeatherApp.ui.receivers.TriggerReEnabler

@PerApplication
@Component(modules = arrayOf(
        AppModule::class,
        OkHttpModule::class,
        NetworkModule::class,
        CacheModule::class,
        ActivityModule::class,
        CurrentWeatherModule::class,
        ForecastModule::class,
        UviModule::class,
        TriggersModule::class,
        JobManagerModule::class
))
interface AppComponent {
    fun context() : Context
    fun networkConnectionChecker() : NetworkConnectionChecker
    fun currentWeatherRepository() : CurrentWeatherRepository
    fun forecastRepository() : ForecastRepository
    fun uviRepository() : UviRepository
    fun triggersRepository() : TriggersRepository
    fun jobManager(): JobManager
    fun inject(addTriggerJob: BaseTriggerJob)
    fun triggerReEnabler(triggerReEnabler: TriggerReEnabler)
}
