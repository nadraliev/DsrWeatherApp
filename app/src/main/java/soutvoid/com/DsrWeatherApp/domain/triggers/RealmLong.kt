package soutvoid.com.DsrWeatherApp.domain.triggers

import io.realm.RealmObject
import io.realm.annotations.PrimaryKey
import java.io.Serializable

open class RealmLong(
        var value: Long = 0,
        @PrimaryKey
        var id: Int = RealmLong.getNextKey()
) : RealmObject(), Serializable {

    companion object {
        private fun getNextKey() = System.currentTimeMillis().hashCode()
    }
}