package soutvoid.com.DsrWeatherApp.domain.triggers.condition

import com.google.gson.annotations.SerializedName

data class Condition(
        @SerializedName("name")
        val name: ConditionName,
        @SerializedName("expression")
        val expression: ConditionExpression,
        @SerializedName("amount")
        val amount: Double
)