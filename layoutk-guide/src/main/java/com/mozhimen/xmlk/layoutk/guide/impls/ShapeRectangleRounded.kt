package com.mozhimen.xmlk.layoutk.guide.impls

import android.animation.TimeInterpolator
import android.graphics.Canvas
import android.graphics.Paint
import android.graphics.PointF
import android.graphics.RectF
import android.view.animation.DecelerateInterpolator
import com.mozhimen.xmlk.layoutk.guide.commons.Shape
import java.util.concurrent.TimeUnit

/**
 * [Shape] of RoundedRectangle with customizable height, width, and radius.
 */
class ShapeRectangleRounded @JvmOverloads constructor(
    private val height: Float,
    private val width: Float,
    private val radius: Float,
    override val duration: Long = TimeUnit.MILLISECONDS.toMillis(500),
    override val timeInterpolator: TimeInterpolator = DecelerateInterpolator(2f),
) : Shape {

    override fun draw(canvas: Canvas, point: PointF, value: Float, paint: Paint) {
        val halfWidth = width / 2 * value
        val halfHeight = height / 2 * value
        val left = point.x - halfWidth
        val top = point.y - halfHeight
        val right = point.x + halfWidth
        val bottom = point.y + halfHeight
        val rect = RectF(left, top, right, bottom)
        canvas.drawRoundRect(rect, radius, radius, paint)
    }
}

