package soutvoid.com.DsrWeatherApp.domain.triggers.condition

import com.google.gson.annotations.SerializedName

enum class ConditionExpression(val symbol: String) {
    @SerializedName("${'$'}gt")
    gt(">"),
    @SerializedName("${'$'}lt")
    lt("<"),
    @SerializedName("${'$'}gte")
    gte("≥"),
    @SerializedName("${'$'}lte")
    lte("≤"),
    @SerializedName("${'$'}eq")
    eq("="),
    @SerializedName("${'$'}ne")
    ne("≠")
}