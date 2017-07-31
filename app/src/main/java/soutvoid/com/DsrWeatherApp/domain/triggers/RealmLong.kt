package soutvoid.com.DsrWeatherApp.domain.triggers

import io.realm.RealmObject
import io.realm.annotations.PrimaryKey
import soutvoid.com.DsrWeatherApp.ui.util.getRandomString
import java.io.Serializable

open class RealmLong(
        var value: Long = 0,
        @PrimaryKey
        var id: String = getRandomString()
) : RealmObject(), Serializable