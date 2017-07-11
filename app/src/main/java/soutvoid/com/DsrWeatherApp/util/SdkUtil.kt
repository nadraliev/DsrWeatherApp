package soutvoid.com.DsrWeatherApp.util

import android.os.Build

/**
 * Утилита для проверки поддержки нужного уровня API
 */
object SdkUtil {

    fun supportsN(): Boolean {
        return supportsVersionCode(Build.VERSION_CODES.N)
    }

    fun supportsM(): Boolean {
        return supportsVersionCode(Build.VERSION_CODES.M)
    }

    fun supportsLollipop(): Boolean {
        return supportsVersionCode(Build.VERSION_CODES.LOLLIPOP)
    }

    fun supportsKitkat(): Boolean {
        return supportsVersionCode(Build.VERSION_CODES.KITKAT)
    }

    fun supportsJellybean(): Boolean {
        return supportsVersionCode(Build.VERSION_CODES.JELLY_BEAN)
    }

    fun supportsVersionCode(versionCode: Int): Boolean {
        return Build.VERSION.SDK_INT >= versionCode
    }
}