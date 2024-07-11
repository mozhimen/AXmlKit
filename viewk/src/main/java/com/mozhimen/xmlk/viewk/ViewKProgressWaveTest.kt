package com.mozhimen.xmlk.viewk

import android.animation.ValueAnimator
import android.content.Context
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.LinearGradient
import android.graphics.Matrix
import android.graphics.Paint
import android.graphics.Path
import android.graphics.RectF
import android.graphics.Shader
import android.graphics.SweepGradient
import android.util.AttributeSet
import android.view.View
import android.view.animation.LinearInterpolator
import com.mozhimen.xmlk.bases.BaseViewK
import kotlin.properties.Delegates

/**
 * @ClassName ViewKProgressWaveTest
 * @Description TODO
 * @Author Mozhimen & Kolin Zhao
 * @Date 2024/7/11
 * @Version 1.0
 */
class WaveView @JvmOverloads constructor(context: Context, attributeSet: AttributeSet? = null, defStyleAttr: Int = 0) : BaseViewK(context, attributeSet, defStyleAttr) {

    companion object {
        const val RESUME = 0x1
        const val STOP = 0x2
        const val DESTROY = 0x3
    }

    ///////////////////////////////////////////////////////////////////////////////

    private var fWaveShader: LinearGradient? = null
    private var sWaveShader: LinearGradient? = null

    private var wavePath = Path()
    private var waveCirclePath = Path()
    private val waveNum = 2

    //波浪的渐变颜色数组
    private val waveColors by lazy {
        arrayListOf(
            //深红色
            intArrayOf(Color.parseColor("#E8E6421A"), Color.parseColor("#E2E96827")),
            intArrayOf(Color.parseColor("#E8E6421A"), Color.parseColor("#E2F19A7F")),
            //橙色
            intArrayOf(Color.parseColor("#E8FDA085"), Color.parseColor("#E2F6D365")),
            intArrayOf(Color.parseColor("#E8FDA085"), Color.parseColor("#E2F5E198")),
            //绿色
            intArrayOf(Color.parseColor("#E8009EFD"), Color.parseColor("#E22AF598")),
            intArrayOf(Color.parseColor("#E8009EFD"), Color.parseColor("#E28EF0C6"))
        )
    }

    //外围圆环的渐变色
    private val circleColors by lazy {
        arrayListOf(
            //深红色
            intArrayOf(Color.parseColor("#FFF83600"), Color.parseColor("#FFF9D423")),
            //橙色
            intArrayOf(Color.parseColor("#FFFDA085"), Color.parseColor("#FFF6D365")),
            //绿色
            intArrayOf(Color.parseColor("#FF2AF598"), Color.parseColor("#FF009EFD"))
        )
    }

    private val wavePaint by lazy {
        val paint = Paint()
        paint.isAntiAlias = true
        paint.strokeWidth = 1f
        paint
    }

    //波浪高度比例
    private var waveWaterLevelRatio = 0f

    //波浪的振幅
    private var waveAmplitude = 0f

    //波浪最大振幅高度
    private var maxWaveAmplitude = 0f

    //外围圆圈的画笔
    private val outerCirclePaint by lazy {
        val paint = Paint()
        paint.strokeWidth = 20f
        paint.strokeCap = Paint.Cap.ROUND
        paint.style = Paint.Style.STROKE
        paint.isAntiAlias = true
        paint
    }

    private val outerNormalCirclePaint by lazy {
        val paint = Paint()
        paint.strokeWidth = 20f
        paint.color = Color.parseColor("#FFF2F3F3")
        paint.style = Paint.Style.STROKE
        paint.isAntiAlias = true
        paint
    }

    private val ringPaint by lazy {
        val paint = Paint()
        paint.style = Paint.Style.STROKE
        paint.color = Color.WHITE
        paint.isAntiAlias = true
        paint
    }

    //外围圆圈的颜色渐变器矩阵，用于从90度开启渐变，由于线条头部有个小圆圈会导致显示差异，因此从88度开始绘制
    private val sweepMatrix by lazy {
        val matrix = Matrix()
        matrix.setRotate(88f, centerX, centerY)
        matrix
    }

    //进度 0-100
    var percent = 0
        set(value) {
            field = value
            waveWaterLevelRatio = value / 100f
            //y = -4 * x2 + 4x抛物线计算振幅，水波纹振幅规律更加真实
            waveAmplitude =
                (-4 * (waveWaterLevelRatio * waveWaterLevelRatio) + 4 * waveWaterLevelRatio) * maxWaveAmplitude
//            waveAmplitude = if (value < 50) 2f * waveWaterLevelRatio * maxWaveAmplitude else (-2 * waveWaterLevelRatio + 2) * maxWaveAmplitude
            val shader = when (value) {
                in 0..46 -> {
                    fWaveShader = LinearGradient(
                        0f, heightF.toFloat(), 0f, heightF * (1 - waveWaterLevelRatio),
                        waveColors[0],
                        null, Shader.TileMode.CLAMP
                    )
                    sWaveShader = LinearGradient(
                        0f, heightF.toFloat(), 0f, heightF * (1 - waveWaterLevelRatio),
                        waveColors[1],
                        null, Shader.TileMode.CLAMP
                    )
                    SweepGradient(
                        centerX.toFloat(),
                        centerY.toFloat(),
                        circleColors[0],
                        floatArrayOf(0f, value / 100f)
                    )
                }

                in 47..54 -> {
                    fWaveShader = LinearGradient(
                        0f, heightF.toFloat(), 0f, heightF * (1 - waveWaterLevelRatio),
                        waveColors[2],
                        null, Shader.TileMode.CLAMP
                    )
                    sWaveShader = LinearGradient(
                        0f, heightF.toFloat(), 0f, heightF * (1 - waveWaterLevelRatio),
                        waveColors[3],
                        null, Shader.TileMode.CLAMP
                    )
                    SweepGradient(
                        centerX.toFloat(),
                        centerY.toFloat(),
                        circleColors[1],
                        floatArrayOf(0f, value / 100f)
                    )
                }

                else -> {

                    fWaveShader = LinearGradient(
                        0f, heightF.toFloat(), 0f, heightF * (1 - waveWaterLevelRatio),
                        waveColors[4],
                        null, Shader.TileMode.CLAMP
                    )
                    sWaveShader = LinearGradient(
                        0f, heightF.toFloat(), 0f, heightF * (1 - waveWaterLevelRatio),
                        waveColors[5],
                        null, Shader.TileMode.CLAMP
                    )
                    SweepGradient(
                        centerX.toFloat(),
                        centerY.toFloat(),
                        circleColors[2],
                        floatArrayOf(0f, value / 100f)
                    )
                }
            }
            shader.setLocalMatrix(sweepMatrix)
            outerCirclePaint.shader = shader
            postInvalidate()
        }

    //外围圆圈的画笔大小
    private var outerStrokeWidth = 10f

    private var fAnimatedValue = 0f
    private var sAnimatedValue = 0f

    //动画
    private val fValueAnimator by lazy {
        val valueAnimator = ValueAnimator()
        valueAnimator.duration = 1500
        valueAnimator.repeatCount = ValueAnimator.INFINITE
        valueAnimator.interpolator = LinearInterpolator()
        valueAnimator.setFloatValues(0f, waveWidth)
        valueAnimator.addUpdateListener { animation ->
            fAnimatedValue = animation.animatedValue as Float
            postInvalidate()
        }
        valueAnimator
    }
    private val sValueAnimator by lazy {
        val valueAnimator = ValueAnimator()
        valueAnimator.duration = 2000
        valueAnimator.repeatCount = ValueAnimator.INFINITE
        valueAnimator.interpolator = LinearInterpolator()
        valueAnimator.setFloatValues(0f, waveWidth)
        valueAnimator.addUpdateListener { animation ->
            sAnimatedValue = animation.animatedValue as Float
            postInvalidate()
        }
        valueAnimator
    }

    //一小段完整波浪的宽度
    private var waveWidth = 0f

    var lifeDelegate by Delegates.observable(0) { _, old, new ->
        when (new) {
            RESUME -> onResume()
            STOP -> onPause()
            DESTROY -> onDestroy()
        }
    }

    //设置外围圆圈的宽度
    fun setOuterStrokeWidth(width: Float) {
        outerStrokeWidth = width
        outerCirclePaint.strokeWidth = outerStrokeWidth
        outerNormalCirclePaint.strokeWidth = outerStrokeWidth
        postInvalidate()
    }

    ///////////////////////////////////////////////////////////////////////////////

    override fun onSizeChanged(w: Int, h: Int, oldw: Int, oldh: Int) {
        super.onSizeChanged(w, h, oldw, oldh)
        waveWidth = widthF * 1.8f
        maxWaveAmplitude = heightF * 0.15f
    }

    //当前窗口销毁时，回收动画资源
    override fun onDetachedFromWindow() {
        onDestroy()
        super.onDetachedFromWindow()
    }

    override fun onDraw(canvas: Canvas) {
        drawWave(canvas)
    }

    ///////////////////////////////////////////////////////////////////////////////

    private fun onResume() {
        if (fValueAnimator.isStarted) {
            animatorResume()
        } else {
            fValueAnimator.start()
            sValueAnimator.start()
        }
    }

    private fun animatorResume() {
        if (fValueAnimator.isPaused || !fValueAnimator.isRunning) {
            fValueAnimator.resume()
        }
        if (sValueAnimator.isPaused || !sValueAnimator.isRunning) {
            sValueAnimator.resume()
        }
    }

    private fun onPause() {
        if (fValueAnimator.isRunning) {
            fValueAnimator.pause()
        }
        if (sValueAnimator.isRunning) {
            sValueAnimator.pause()
        }
    }

    private fun onDestroy() {
        fValueAnimator.cancel()
        sValueAnimator.cancel()
    }

    private fun drawWave(canvas: Canvas) {
        //波浪当前高度
        val level = (1 - waveWaterLevelRatio) * realRadius * 2
        //绘制所有波浪
        for (num in 0 until waveNum) {
            //重置path
            wavePath.reset()
            waveCirclePath.reset()
            var startX = if (num == 0) {//第一条波浪的起始位置
                wavePath.moveTo(-waveWidth + fAnimatedValue, level)
                -waveWidth + fAnimatedValue
            } else {//第二条波浪的起始位置
                wavePath.moveTo(-waveWidth + sAnimatedValue, level)
                -waveWidth + sAnimatedValue
            }
            while (startX < widthF + waveWidth) {
                wavePath.quadTo(
                    startX + waveWidth / 4,
                    level + waveAmplitude,
                    startX + waveWidth / 2,
                    level
                )
                wavePath.quadTo(
                    startX + waveWidth / 4 * 3,
                    level - waveAmplitude,
                    startX + waveWidth,
                    level
                )
                startX += waveWidth
            }
            wavePath.lineTo(startX, heightF.toFloat())
            wavePath.lineTo(0f, heightF.toFloat())
            wavePath.close()

            waveCirclePath.addCircle(
                centerX,
                centerY,
                realRadius,
                Path.Direction.CCW
            )
            waveCirclePath.op(wavePath, Path.Op.INTERSECT)
            //绘制波浪渐变色
            wavePaint.shader = if (num == 0) {
                sWaveShader
            } else {
                fWaveShader
            }
            canvas.drawPath(waveCirclePath, wavePaint)
        }

        //Fixme android6设置Path.op存在明显抖动，因此多画一圈圆环
        val ringWidth = realRadius - outerStrokeWidth
        ringPaint.strokeWidth = ringWidth / 2
        canvas.drawCircle(centerX.toFloat(), centerY.toFloat(), realRadius + ringWidth / 4, ringPaint)
    }
}