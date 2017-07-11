package soutvoid.com.DsrWeatherApp.ui.common.error

import com.agna.ferro.mvp.component.scope.PerScreen

import dagger.Module
import dagger.Provides


@Module
class ErrorHandlerModule {

    @Provides
    @PerScreen
    internal fun provideNetworkErrorHandler(standardErrorHandler: StandardErrorHandler): ErrorHandler = standardErrorHandler
}
