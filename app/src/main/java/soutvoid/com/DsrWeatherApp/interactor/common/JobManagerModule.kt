package soutvoid.com.DsrWeatherApp.interactor.common

import android.content.Context
import com.agna.ferro.mvp.component.scope.PerApplication
import com.birbit.android.jobqueue.JobManager
import com.birbit.android.jobqueue.config.Configuration
import dagger.Module
import dagger.Provides
import soutvoid.com.DsrWeatherApp.app.App
import soutvoid.com.DsrWeatherApp.ui.service.BaseTriggerJob

@Module
class JobManagerModule() {

    @Provides
    @PerApplication
    fun provideJobManager(context: Context): JobManager {
        val builder = Configuration.Builder(context)
                .minConsumerCount(1)
                .maxConsumerCount(3)
                .loadFactor(3)
                .consumerKeepAlive(120)
                .injector( {
                    (it as BaseTriggerJob).inject((context as App).appComponent)
                })
        return JobManager(builder.build())
    }

}