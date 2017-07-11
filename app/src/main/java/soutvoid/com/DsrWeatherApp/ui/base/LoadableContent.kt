package soutvoid.com.DsrWeatherApp.ui.base


/**
 * интерфейс для view, которые содержат загружаемый контент (в которых нужно отображать индикатор загрузки)
 */
interface LoadableContent {

    fun showTransparentPlaceholder()

    fun hidePlaceholder()

    fun showPlaceholderWithBackground()

}
