package soutvoid.com.DsrWeatherApp.ui.service

import com.agna.ferro.mvp.component.scope.PerScreen
import dagger.Component
import soutvoid.com.DsrWeatherApp.app.dagger.AppComponent

@PerScreen
@Component(dependencies = arrayOf(AppComponent::class))
interface NotificationSchedulerServiceComponent {
    fun inject(service: NotificationSchedulerService)
}