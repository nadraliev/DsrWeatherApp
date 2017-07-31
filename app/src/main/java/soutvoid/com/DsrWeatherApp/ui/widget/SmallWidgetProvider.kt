package soutvoid.com.DsrWeatherApp.ui.widget

import android.app.PendingIntent
import android.appwidget.AppWidgetManager
import android.appwidget.AppWidgetProvider
import android.content.Context
import android.content.Intent
import android.graphics.Color
import android.widget.RemoteViews
import android.widget.Toast
import com.agna.ferro.mvp.component.scope.PerScreen
import com.mikepenz.iconics.IconicsDrawable
import io.realm.Realm
import rx.android.schedulers.AndroidSchedulers
import rx.schedulers.Schedulers
import soutvoid.com.DsrWeatherApp.R
import soutvoid.com.DsrWeatherApp.app.App
import soutvoid.com.DsrWeatherApp.domain.location.SavedLocation
import soutvoid.com.DsrWeatherApp.interactor.currentWeather.CurrentWeatherRepository
import soutvoid.com.DsrWeatherApp.ui.util.UnitsUtils
import soutvoid.com.DsrWeatherApp.ui.util.WeatherIconsHelper
import soutvoid.com.DsrWeatherApp.ui.util.getDefaultPreferences
import soutvoid.com.DsrWeatherApp.ui.util.ifNotNull
import javax.inject.Inject

@PerScreen
class SmallWidgetProvider : AppWidgetProvider() {

    @Inject
    lateinit var currentWeatherRepository: CurrentWeatherRepository

    override fun onUpdate(context: Context?, appWidgetManager: AppWidgetManager?, appWidgetIds: IntArray?) {
        context?.let { satisfyDependencies(it) }
        val realm = Realm.getDefaultInstance()
        val location = realm.copyFromRealm(realm.where(SavedLocation::class.java).findFirst())
        realm.close()

        val remoteViews = RemoteViews(context?.packageName, R.layout.small_widget_layout)
        ifNotNull(location, context) { loc, ctx ->
            remoteViews.setTextViewText(R.id.small_widget_location, loc.name)
            currentWeatherRepository.getByCoordinates(loc.latitude, loc.longitude, UnitsUtils.getPreferredUnits(ctx))
                    .subscribeOn(Schedulers.io())
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribe {
                        remoteViews.setImageViewBitmap(R.id.small_widget_icon,
                                IconicsDrawable(ctx)
                                        .icon(WeatherIconsHelper.getWeatherIcon(it.weather.first().id, it.timeOfData))
                                        .sizeDp(24)
                                        .color(Color.BLACK).toBitmap())
                        remoteViews.setTextViewText(R.id.small_widget_temperature, "${Math.round(it.main.temperature)} ${UnitsUtils.getDegreesUnits(ctx)}")
                        updateWidgets(appWidgetManager, appWidgetIds, remoteViews)
                    }
        }
    }

    override fun onReceive(context: Context?, intent: Intent?) {
        super.onReceive(context, intent)
    }

    private fun satisfyDependencies(context: Context) {
        val appComponent = (context.applicationContext as? App)?.appComponent
        appComponent?.let { DaggerSmallWidgetComponent.builder().appComponent(it).build().inject(this) }
    }

    private fun updateWidgets(appWidgetManager: AppWidgetManager?, appWidgetIds: IntArray?, remoteViews: RemoteViews) {
        ifNotNull(appWidgetManager, appWidgetIds) {
            widgetManager, ints -> widgetManager.updateAppWidget(ints, remoteViews)
        }
    }

}