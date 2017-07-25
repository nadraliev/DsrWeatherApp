package soutvoid.com.DsrWeatherApp.ui.common.dagger

import dagger.Module
import soutvoid.com.DsrWeatherApp.ui.base.fragment.FragmentModule
import soutvoid.com.DsrWeatherApp.ui.common.error.ErrorHandlerModule

@Module(includes = arrayOf(
        FragmentModule::class,
        ErrorHandlerModule::class
))
class FragmentViewModule