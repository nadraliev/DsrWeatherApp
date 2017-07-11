package soutvoid.com.DsrWeatherApp.app.log

import com.crashlytics.android.Crashlytics


object RemoteLogger {

    fun setUser(id: String, username: String, email: String) {
        try {
            Crashlytics.getInstance().core.setUserName(username)
            Crashlytics.getInstance().core.setUserEmail(email)
            Crashlytics.getInstance().core.setUserIdentifier(id)
        } catch (e: Exception) {
            //ignore exception
        }

    }

    fun clearUser() {
        try {
            Crashlytics.getInstance().core.setUserName("")
            Crashlytics.getInstance().core.setUserEmail("")
            Crashlytics.getInstance().core.setUserIdentifier("")
        } catch (e: Exception) {
            //ignore exception
        }

    }

    fun setCustomKey(key: String, value: String) {
        try {
            Crashlytics.getInstance().core.setString(key, value)
        } catch (e: Exception) {
            //ignore exception
        }

    }

    fun logError(t: Throwable) {
        try {
            Crashlytics.getInstance().core.logException(t)
        } catch (e: Exception) {
            //ignore exception
        }

    }

    fun logMessage(message: String) {
        try {
            Crashlytics.getInstance().core.log(message)
        } catch (e: Exception) {
            //ignore exception
        }

    }

}
