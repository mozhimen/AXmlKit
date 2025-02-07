package com.mozhimen.xmlk.layoutk.guide.impls

import android.animation.ObjectAnimator
import android.animation.TimeInterpolator
import android.graphics.Canvas
import android.graphics.Paint
import android.graphics.PointF
import android.view.animation.LinearInterpolator
import androidx.annotation.ColorInt
import com.mozhimen.xmlk.layoutk.guide.commons.Effect
import java.util.concurrent.TimeUnit

/**
 * Draws an flicker effects.
 */
class EffectFlicker @JvmOverloads constructor(
    private val radius: Float,
    @ColorInt private val color: Int,
    override val duration: Long = TimeUnit.MILLISECONDS.toMillis(1000),
    override val timeInterpolator: TimeInterpolator = LinearInterpolator(),
    override val repeatMode: Int = ObjectAnimator.REVERSE
) : Effect {
  override fun draw(canvas: Canvas, point: PointF, value: Float, paint: Paint) {
    paint.color = color
    paint.alpha = (value * 255).toInt()
    canvas.drawCircle(point.x, point.y, radius, paint)
  }
}