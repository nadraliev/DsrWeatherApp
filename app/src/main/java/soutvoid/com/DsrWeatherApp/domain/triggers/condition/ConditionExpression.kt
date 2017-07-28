package soutvoid.com.DsrWeatherApp.domain.triggers.condition

enum class ConditionExpression(val str: String) {
    gt("\$gt"),
    lt("\$lt"),
    gte("\$gte"),
    lte("\$lte"),
    eq("\$eq"),
    ne("\$ne");

    override fun toString(): String {
        return str
    }
}