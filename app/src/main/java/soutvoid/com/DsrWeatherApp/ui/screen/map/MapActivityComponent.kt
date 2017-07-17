package soutvoid.com.DsrWeatherApp.ui.screen.map

import com.agna.ferro.mvp.component.ScreenComponent
import com.agna.ferro.mvp.component.scope.PerScreen
import dagger.Component
import soutvoid.com.DsrWeatherApp.app.dagger.AppComponent
import soutvoid.com.DsrWeatherApp.ui.common.dagger.ActivityViewModule

@PerScreen
@Component(dependencies = arrayOf(AppComponent::class), modules = arrayOf(ActivityViewModule::class))
interface MapActivityComponent: ScreenComponent<MapActivityView>