package com.mozhimen.xmlk.viewk

import android.content.Context
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Paint
import android.graphics.Path
import android.graphics.PathMeasure
import android.graphics.Rect
import android.graphics.Region
import android.os.Build
import android.text.TextPaint
import android.util.AttributeSet
import android.util.Log
import com.mozhimen.kotlin.utilk.android.util.UtilKLogWrapper
import androidx.annotation.RequiresApi
import com.mozhimen.kotlin.elemk.android.os.cons.CVersCode
import com.mozhimen.kotlin.utilk.android.util.dp2px
import com.mozhimen.xmlk.bases.BaseViewK


/**
 * @ClassName ViewKRegion
 * @Description TODO
 * @Author Mozhimen & Kolin Zhao
 * @Date 2024/3/27
 * @Version 1.0
 */
class ViewKRegion @JvmOverloads constructor(context: Context, attrs: AttributeSet? = null, defStyleAttr: Int = 0) : BaseViewK(context, attrs, defStyleAttr) {
    init {
        initPaint()
        isClickable = true
    }

    private lateinit var _textPaint: TextPaint

    override fun initPaint() {
        //否则提供给外部纹理绘制
        _textPaint = TextPaint(Paint.ANTI_ALIAS_FLAG or Paint.FILTER_BITMAP_FLAG)
        _textPaint!!.apply {
            setAntiAlias(true)
            setStyle(Paint.Style.FILL)
            setStrokeCap(Paint.Cap.ROUND)
            setFilterBitmap(true)
            setDither(true)
            setStrokeWidth(20f.dp2px())
        }
    }

    override fun onMeasure(widthMeasureSpec: Int, heightMeasureSpec: Int) {
        val widthMode = MeasureSpec.getMode(widthMeasureSpec)
        var widthSize = MeasureSpec.getSize(widthMeasureSpec)
        if (widthMode != MeasureSpec.EXACTLY) {
            widthSize = resources.displayMetrics.widthPixels / 2
        }
        val heightMode = MeasureSpec.getMode(heightMeasureSpec)
        var heightSize = MeasureSpec.getSize(heightMeasureSpec)
        if (heightMode != MeasureSpec.EXACTLY) {
            heightSize = widthSize / 2
        }
        setMeasuredDimension(widthSize, heightSize)
    }

    private var x = 0f
    private var y = 0f

    //所有形状
    var objectPaths: Array<Path?> = arrayOfNulls(7)

    //形状区域检测
    var objectRegion = Region()

    //小圆球区域
    var circleRegion = Region()

    //小圆
    var circlePath: Path = Path()

    //绘制区域
    var mainRegion = Region()

    var circleRect = Rect()
    var objectRect = Rect()

    var pos = FloatArray(2)
    var tan = FloatArray(2)

    var pathMeasure = PathMeasure()

    var tmp = Path()

    override fun onDraw(canvas: Canvas) {
        super.onDraw(canvas)

        val width = width
        val height = height
        if (width < 1 || height < 1) {
            return
        }
        val save = canvas.save()
        canvas.translate(width / 2f, height / 2f)

        val radius = Math.min(width / 2f, height / 2f)
        mainRegion[-radius.toInt(), -radius.toInt(), radius.toInt()] = radius.toInt()
        for (i in objectPaths.indices) {
            var path: Path? = objectPaths[i]
            if (path == null) {
                path = Path()
                objectPaths[i] = path
            } else {
                path.reset()
            }
        }
        var path: Path = objectPaths[0]!!
        path.moveTo(radius / 2, -radius / 2)
        path.lineTo(0f, -radius)
        path.lineTo(radius / 2, -radius)
        path.close()

        path = objectPaths[1]!!
        path.moveTo(-radius / 2, radius / 2)
        path.lineTo(-radius / 2 - 100, radius / 2)
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP)
            path.arcTo(-radius / 2 - 100, radius / 2, -radius / 2, radius / 2 + 100, 0f, 180f, false)
        path.lineTo(-radius / 2, radius / 2)
        path.close()

        path = objectPaths[2]!!
        path.addCircle(-radius + 200f, -radius + 200f, 50f, Path.Direction.CCW)

        path = objectPaths[3]!!
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP)
            path.addRoundRect(-radius + 50, -radius / 2, -radius + 90, 0f, 10f, 10f, Path.Direction.CCW)

        path = objectPaths[4]!!
        path.addRect(120f, 120f, 200f, 200f, Path.Direction.CCW)

        path = objectPaths[5]!!
        path.addCircle(250f, 0f, 100f, Path.Direction.CCW)

        tmp.reset()
        tmp.addCircle(250f, -80f, 80f, Path.Direction.CCW)
        path.op(tmp, Path.Op.DIFFERENCE)
        tmp.reset()
        path = objectPaths[6]!!
        path.addCircle(0f, 0f, 100f, Path.Direction.CCW)
        tmp.addCircle(0f, 0f, 80f, Path.Direction.CCW)
        path.op(tmp, Path.Op.DIFFERENCE)
        circlePath.reset()
        circlePath.addCircle(x - width / 2f, y - height / 2f, 20f, Path.Direction.CCW)
        circleRegion.setPath(circlePath, mainRegion)
        _textPaint.setColor(Color.CYAN)
        for (i in objectPaths.indices) {
            objectRegion.setPath(objectPaths[i]!!, mainRegion)
            if (!objectRegion.quickReject(circleRegion)) {
                if (circleRegion.getBounds(circleRect)
                    && objectRegion.getBounds(objectRect)
                ) {
                    var regionChecker: Region? = null
                    regionChecker = if (circleRect.width() * circleRect.height() > objectRect.width() * objectRect.height()) {
                        pathMeasure.setPath(objectPaths[i], false)
                        circleRegion
                    } else {
                        pathMeasure.setPath(circlePath, false)
                        objectRegion
                    }
                    var len = 0
                    while (len < pathMeasure.length) {
                        pathMeasure.getPosTan(len.toFloat(), pos, tan)
                        if (regionChecker.contains(pos[0].toInt(), pos[1].toInt())) {
                            UtilKLogWrapper.d("RegionView", " 可能发生了碰撞")
                            _textPaint.setColor(Color.YELLOW)
                        }
                        len++
                    }
                }
            } else {
                _textPaint.setColor(Color.CYAN)
            }
            canvas.drawPath(objectPaths[i]!!, _textPaint)
        }
        _textPaint.setColor(Color.WHITE)
        canvas.drawPath(circlePath, _textPaint)
        canvas.restoreToCount(save)
    }

    override fun dispatchDrawableHotspotChanged(x: Float, y: Float) {
        super.dispatchDrawableHotspotChanged(x, y)
        this.x = x
        this.y = y
        postInvalidate()
    }

    override fun dispatchSetPressed(pressed: Boolean) {
        super.dispatchSetPressed(pressed)
        postInvalidate()
    }

    fun argb(red: Float, green: Float, blue: Float): Int {
        return (1 * 255.0f + 0.5f).toInt() shl 24 or
                ((red * 255.0f + 0.5f).toInt() shl 16) or
                ((green * 255.0f + 0.5f).toInt() shl 8) or (blue * 255.0f + 0.5f).toInt()
    }
}