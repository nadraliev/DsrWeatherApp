package soutvoid.com.DsrWeatherApp.ui.common.dagger

import dagger.Module
import soutvoid.com.DsrWeatherApp.ui.base.activity.ActivityModule
import soutvoid.com.DsrWeatherApp.ui.common.error.ErrorHandlerModule

@Module(includes = arrayOf(
        ActivityModule::class,
        ErrorHandlerModule::class
))
class ActivityViewModule