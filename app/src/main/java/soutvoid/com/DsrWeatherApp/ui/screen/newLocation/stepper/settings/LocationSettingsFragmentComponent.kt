package soutvoid.com.DsrWeatherApp.ui.screen.newLocation.stepper.settings

import com.agna.ferro.mvp.component.ScreenComponent
import com.agna.ferro.mvp.component.scope.PerScreen
import dagger.Component
import soutvoid.com.DsrWeatherApp.app.dagger.AppComponent
import soutvoid.com.DsrWeatherApp.ui.common.dagger.FragmentViewModule

@PerScreen
@Component(dependencies = arrayOf(AppComponent::class), modules = arrayOf(FragmentViewModule::class))
interface LocationSettingsFragmentComponent : ScreenComponent<LocationSettingsFragmentView>