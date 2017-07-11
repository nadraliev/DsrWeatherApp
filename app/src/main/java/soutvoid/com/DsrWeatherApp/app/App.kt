package soutvoid.com.DsrWeatherApp.app

import android.app.Application
import com.crashlytics.android.Crashlytics
import io.fabric.sdk.android.Fabric
import com.uphyca.stetho_realm.RealmInspectorModulesProvider
import com.facebook.stetho.Stetho
import com.github.anrwatchdog.ANRWatchDog
import io.realm.Realm
import soutvoid.com.DsrWeatherApp.app.dagger.AppComponent
import soutvoid.com.DsrWeatherApp.app.dagger.AppModule
import soutvoid.com.DsrWeatherApp.app.dagger.DaggerAppComponent
import soutvoid.com.DsrWeatherApp.app.log.Logger
import soutvoid.com.DsrWeatherApp.app.log.RemoteLogger


class App : Application() {

    lateinit var appComponent : AppComponent

    override fun onCreate() {
        super.onCreate()

        initFabric()
        initAnrWatchDog()
        initLogger()
        initInjector()
        initStetho()
        initRealm()
    }

    fun initFabric() {
        Fabric.with(this, Crashlytics())
    }

    private fun initAnrWatchDog() {
        ANRWatchDog().setReportMainThreadOnly().setANRListener( RemoteLogger::logError ).start()
    }

    fun initLogger() {
        Logger.init()
    }

    fun initInjector() {
        appComponent = DaggerAppComponent.builder()
                .appModule(AppModule(this))
                .build()
    }

    fun initStetho() {
        Stetho.initialize(
                Stetho.newInitializerBuilder(this)
                        .enableDumpapp(Stetho.defaultDumperPluginsProvider(this))
                        .enableWebKitInspector(RealmInspectorModulesProvider.builder(this).build())
                        .build()
        )
    }

    fun initRealm() {
        Realm.init(this)
    }
}