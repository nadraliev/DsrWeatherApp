package soutvoid.com.DsrWeatherApp.interactor.util

import android.annotation.SuppressLint
import android.text.Html
import soutvoid.com.DsrWeatherApp.util.SdkUtil


object TransformUtil {

    @SuppressLint("NewApi")
            /**
     * Заменяет форматирующие символы Html на нормальные (например &quot; -> ")
     */
    fun sanitizeHtmlString(string: String): CharSequence {
        if (SdkUtil.supportsVersionCode(android.os.Build.VERSION_CODES.N)) {
            return Html.fromHtml(string, 0).toString()
        } else {
            @Suppress("DEPRECATION")
            return Html.fromHtml(string).toString()
        }
    }

}
