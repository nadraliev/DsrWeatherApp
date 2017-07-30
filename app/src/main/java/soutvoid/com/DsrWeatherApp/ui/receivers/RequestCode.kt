package soutvoid.com.DsrWeatherApp.ui.receivers

import io.realm.RealmObject
import java.io.Serializable

open class RequestCode(var value: Int = RequestCode.newCode()): RealmObject(), Serializable {

    companion object {
        fun newCode(): Int {
            return System.currentTimeMillis().hashCode()
        }
    }
}