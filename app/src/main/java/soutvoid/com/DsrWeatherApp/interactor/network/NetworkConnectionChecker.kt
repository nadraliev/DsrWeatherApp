package soutvoid.com.DsrWeatherApp.interactor.network

import android.content.Context
import android.net.ConnectivityManager
import com.agna.ferro.mvp.component.scope.PerApplication
import rx.Observable
import java.util.concurrent.TimeUnit
import javax.inject.Inject

@PerApplication
class NetworkConnectionChecker @Inject constructor(val context: Context) {

    fun hasInternetConnection() : Boolean {
        val cm = context.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
        val networkInfo = cm.activeNetworkInfo
        return networkInfo != null && networkInfo.isConnectedOrConnecting
    }

    fun onHaveInternetStatus() : Observable<Boolean> {
        return Observable.interval(0, 10, TimeUnit.SECONDS)
                .map { _ -> hasInternetConnection() }
                .filter { connected -> connected }
                .first()
    }

}