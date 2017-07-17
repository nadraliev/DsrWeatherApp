package soutvoid.com.DsrWeatherApp.ui.base.fragment

import com.agna.ferro.core.PersistentScreenScope
import com.agna.ferro.mvp.component.provider.ActivityProvider
import com.agna.ferro.mvp.component.provider.FragmentProvider
import com.agna.ferro.mvp.component.scope.PerScreen
import dagger.Module
import dagger.Provides

@Module
class FragmentModule(private var persistentScreenScope: PersistentScreenScope) {

    @Provides
    @PerScreen
    fun getActivityProvider() : ActivityProvider = ActivityProvider(persistentScreenScope)

    @Provides
    @PerScreen
    fun getFragmentProvider() : FragmentProvider = FragmentProvider(persistentScreenScope)

}