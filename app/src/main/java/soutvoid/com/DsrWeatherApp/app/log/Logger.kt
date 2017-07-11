package soutvoid.com.DsrWeatherApp.app.log

import timber.log.Timber


object Logger {

    fun init() {
        Timber.plant(LoggerTree())
    }

    /**
     * Log a verbose message with optional format args.
     */
    fun v(message: String, vararg args: Any) {
        setClickableLink()
        Timber.v(message, *args)
    }

    /**
     * Log a verbose exception and a message with optional format args.
     */
    fun v(t: Throwable, message: String, vararg args: Any) {
        setClickableLink()
        Timber.v(t, message, *args)
    }

    /**
     * Log a debug message with optional format args.
     */
    fun d(message: String, vararg args: Any) {
        setClickableLink()
        Timber.d(message, *args)
    }

    /**
     * Log a debug exception and a message with optional format args.
     */
    fun d(t: Throwable, message: String, vararg args: Any) {
        setClickableLink()
        Timber.d(t, message, *args)
    }

    /**
     * Log an info message with optional format args.
     */
    fun i(message: String, vararg args: Any) {
        setClickableLink()
        Timber.i(message, *args)
    }

    /**
     * Log an info exception and a message with optional format args.
     */
    fun i(t: Throwable, message: String, vararg args: Any) {
        setClickableLink()
        Timber.i(t, message, *args)
    }

    /**
     * используется для ожидаемых ошибок
     * Логгирует только сообщение ошибки

     * @param e
     */
    fun w(e: Throwable) {
        i(String.format("Error: %s", e.message))
    }

    /**
     * Log a warning message with optional format args.
     */
    fun w(message: String, vararg args: Any) {
        setClickableLink()
        Timber.w(message, *args)
    }

    /**
     * Log a warning exception and a message with optional format args.
     */
    fun w(t: Throwable, message: String, vararg args: Any) {
        setClickableLink()
        Timber.w(t, message, *args)
    }

    /**
     * Log an error message with optional format args.
     */
    fun e(message: String, vararg args: Any) {
        setClickableLink()
        Timber.e(message, *args)
    }

    /**
     * Log an error exception and a message with optional format args.
     */
    fun e(t: Throwable, message: String, vararg args: Any) {
        setClickableLink()
        Timber.e(t, message, *args)
    }

    fun e(t: Throwable) {
        setClickableLink()
        Timber.e(t, "Error")
    }

    /**
     * утсановки ссылки на месонахождение лога
     */
    private fun setClickableLink() {
        val stackTraceElements = Throwable().stackTrace
        if (stackTraceElements.size < 2) {
            throw IllegalStateException("Synthetic stacktrace didn't have enough elements: are you using proguard?")
        }
        val element = stackTraceElements[2]
        val tagMessage = String.format("%s:%s", element.fileName, element.lineNumber)
        Timber.tag(tagMessage)
    }

}
