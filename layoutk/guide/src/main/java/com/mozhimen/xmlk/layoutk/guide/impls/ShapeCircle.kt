package com.mozhimen.xmlk.layoutk.guide.impls

import android.animation.TimeInterpolator
import android.graphics.Canvas
import android.graphics.Paint
import android.graphics.PointF
import android.view.animation.DecelerateInterpolator
import com.mozhimen.xmlk.layoutk.guide.commons.Shape
import java.util.concurrent.TimeUnit

/**
 * [Shape] of Circle with customizable radius.
 */
class ShapeCircle @JvmOverloads constructor(
    private val radius: Float,
    override val duration: Long = TimeUnit.MILLISECONDS.toMillis(500),
    override val timeInterpolator: TimeInterpolator = DecelerateInterpolator(2f),
) : Shape {

    override fun draw(canvas: Canvas, point: PointF, value: Float, paint: Paint) {
        canvas.drawCircle(point.x, point.y, value * radius, paint)
    }
}
