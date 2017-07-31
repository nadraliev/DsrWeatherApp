package soutvoid.com.DsrWeatherApp.domain.triggers.condition

import com.google.gson.annotations.SerializedName
import io.realm.RealmObject
import io.realm.annotations.PrimaryKey
import soutvoid.com.DsrWeatherApp.ui.util.getRandomString
import java.io.Serializable

open class SavedCondition(
        var name: String = ConditionName.temp.toString(),
        var expression: String = ConditionExpression.gt.symbol,
        var amount: Double = .0,
        @PrimaryKey
        var id: String = getRandomString()
) : RealmObject(), Serializable