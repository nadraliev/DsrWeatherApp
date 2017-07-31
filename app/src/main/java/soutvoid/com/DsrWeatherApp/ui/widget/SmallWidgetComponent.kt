package soutvoid.com.DsrWeatherApp.ui.widget

import com.agna.ferro.mvp.component.scope.PerScreen
import dagger.Component
import soutvoid.com.DsrWeatherApp.app.dagger.AppComponent

@PerScreen
@Component(dependencies = arrayOf(AppComponent::class))
interface SmallWidgetComponent {
    fun inject(smallWidgetProvider: SmallWidgetProvider)
}