package soutvoid.com.DsrWeatherApp.interactor.common.network.error

abstract class NetworkException : RuntimeException {

    constructor()

    constructor(message: String) : super(message)

    constructor(cause: Throwable) : super(cause)

    constructor(message: String, cause: Throwable) : super(message, cause)
}