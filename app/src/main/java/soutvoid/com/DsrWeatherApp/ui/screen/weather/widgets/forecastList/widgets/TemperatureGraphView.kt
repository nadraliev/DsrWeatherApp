package soutvoid.com.DsrWeatherApp.ui.screen.weather.widgets.forecastList.widgets

import android.content.Context
import android.graphics.Canvas
import android.graphics.Paint
import android.graphics.Typeface
import android.util.AttributeSet
import android.view.View
import soutvoid.com.DsrWeatherApp.app.log.Logger
import soutvoid.com.DsrWeatherApp.ui.util.*

/**
 * рисует точку с текстом температуры и линии до соседних точек
 */
class TemperatureGraphView: View {

    var prevTemp: Double? = null
    var currentTemp: Double? = null
    var nextTemp: Double? = null

    private var maxTemp: Double = .0
    private var minTemp: Double = .0

    private var dotPaint: Paint
    private var linePaint: Paint
    private var textPaint: Paint

    private val themedColor by lazy {
        Logger.d("shit")
        context.getThemeColor(android.R.attr.textColorPrimary)
    }

    var isBold: Boolean = false
    set(value) {
        field = value
        if (isBold)
            textPaint.typeface = Typeface.DEFAULT_BOLD
        else textPaint.typeface = Typeface.DEFAULT
    }

    init {
        dotPaint = initDotPaint()
        linePaint = initLinePaint()
        textPaint = initTextPaint()
    }

    constructor(context: Context?) : super(context)
    constructor(context: Context?, attrs: AttributeSet?) : super(context, attrs)
    constructor(context: Context?, attrs: AttributeSet?, defStyleAttr: Int) : super(context, attrs, defStyleAttr)
    constructor(context: Context?, attrs: AttributeSet?, defStyleAttr: Int, defStyleRes: Int) : super(context, attrs, defStyleAttr, defStyleRes)

    override fun draw(canvas: Canvas?) {
        super.draw(canvas)

        canvas?.let {
            drawCurrentTempDot(it, dotPaint)
            drawCurrentTemperature(it, textPaint)
            drawLineToNext(it, linePaint)
            drawLineToPrev(it, linePaint)
        }
    }

    private fun initDotPaint(): Paint {
        val dotPaint = Paint()
        dotPaint.color = themedColor
        dotPaint.flags = Paint.ANTI_ALIAS_FLAG
        return dotPaint
    }

    private fun initLinePaint() : Paint {
        val linePaint = Paint()
        linePaint.color = themedColor
        linePaint.flags = Paint.ANTI_ALIAS_FLAG
        linePaint.style = Paint.Style.FILL_AND_STROKE
        linePaint.strokeWidth = dpToPx(2.7).toFloat()
        return linePaint
    }

    private fun initTextPaint(): Paint {
        val textPaint = Paint()
        textPaint.color = themedColor
        textPaint.flags = Paint.ANTI_ALIAS_FLAG
        textPaint.textSize = spToPx(14f).toFloat()
        return textPaint
    }

    private fun drawCurrentTempDot(canvas: Canvas, paint: Paint) {
        currentTemp?.let { canvas.drawCircle(measuredWidth.toFloat() / 2, dpToPx(calculateY(it)).toFloat(), dpToPx(4.0).toFloat(), paint) }
    }

    private fun drawCurrentTemperature(canvas: Canvas, paint: Paint) {
        currentTemp?.let { canvas.drawText(
                "${Math.round(it)}${UnitsUtils.getDegreesUnits(context)}",
                measuredWidth.toFloat() / 2 - dpToPx(9.0).toFloat(),
                dpToPx(calculateY(it) - 10).toFloat(),
                paint
                ) }
    }

    private fun drawLineToNext(canvas: Canvas, paint: Paint) {
        ifNotNull(currentTemp, nextTemp) { current, next ->
            canvas.drawLine(
                    measuredWidth.toFloat() / 2, dpToPx(calculateY(current)).toFloat(),
                    measuredWidth.toFloat() * 3 / 2, dpToPx(calculateY(next)).toFloat(),
                    paint
            )
        }
    }

    private fun drawLineToPrev(canvas: Canvas, paint: Paint) {
        ifNotNull(currentTemp, prevTemp) { current, prev ->
            canvas.drawLine(
                    measuredWidth.toFloat() / 2, dpToPx(calculateY(current)).toFloat(),
                    -measuredWidth.toFloat() / 2, dpToPx(calculateY(prev)).toFloat(),
                    paint
            )
        }
    }

    /**
     * посчитать координату y, считая сверху
     * @return координата y в dp
     */
    private fun calculateY(temperature: Double): Double {
        return (maxTemp - temperature) * 5 + 30
    }

    private fun resizeSelfToTemperature() {
        val height = maxOf(70, calculateTempGraphHeight())
        layoutParams.height = dpToPx(height.toDouble()).toInt()
    }

    /**
     * высота графика в зависимости от разброса температуры
     * @return высота в dp
     */
    private fun calculateTempGraphHeight(): Int {
        return (maxTemp - minTemp).toInt() * 5 + 50
    }

    /**
     * установить разброс температуры
     */
    fun setMinMaxTemp(minTemp: Double, maxTemp: Double) {
        this.minTemp = minTemp
        this.maxTemp = maxTemp
        resizeSelfToTemperature()
    }
}