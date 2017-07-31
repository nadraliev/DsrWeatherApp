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
import soutvoid.com.DsrWeatherApp.domain.CurrentWeather
import soutvoid.com.DsrWeatherApp.domain.location.SavedLocation
import soutvoid.com.DsrWeatherApp.interactor.currentWeather.CurrentWeatherRepository
import soutvoid.com.DsrWeatherApp.ui.util.*
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
                        showData(ctx, it, remoteViews)
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

    private fun showData(context: Context, currentWeather: CurrentWeather, remoteViews: RemoteViews) {
        remoteViews.setImageViewBitmap(R.id.small_widget_icon,
                IconicsDrawable(context)
                        .icon(WeatherIconsHelper.getWeatherIcon(currentWeather.weather.first().id, currentWeather.timeOfData))
                        .sizeDp(32)
                        .color(Color.BLACK).toBitmap())
        remoteViews.setTextViewText(R.id.small_widget_temperature, "${Math.round(currentWeather.main.temperature)} ${UnitsUtils.getDegreesUnits(context)}")
    }

    private fun updateWidgets(appWidgetManager: AppWidgetManager?, appWidgetIds: IntArray?, remoteViews: RemoteViews) {
        ifNotNull(appWidgetManager, appWidgetIds) {
            widgetManager, ints -> widgetManager.updateAppWidget(ints, remoteViews)
        }
    }

}