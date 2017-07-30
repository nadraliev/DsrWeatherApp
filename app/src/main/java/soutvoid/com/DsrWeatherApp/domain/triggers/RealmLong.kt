package soutvoid.com.DsrWeatherApp.domain.triggers

import io.realm.RealmObject
import java.io.Serializable

open class RealmLong(var value: Long = 0): RealmObject(), Serializable