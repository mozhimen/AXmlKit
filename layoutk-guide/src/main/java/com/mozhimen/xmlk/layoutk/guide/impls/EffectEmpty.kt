package com.mozhimen.xmlk.layoutk.guide.impls

import android.animation.ObjectAnimator
import android.animation.TimeInterpolator
import android.view.animation.LinearInterpolator
import com.mozhimen.xmlk.layoutk.guide.commons.Effect
import java.util.concurrent.TimeUnit

/**
 * [Effect] that does not do anything.
 */
class EffectEmpty @JvmOverloads constructor(
    override val duration: Long = TimeUnit.MILLISECONDS.toMillis(0),
    override val timeInterpolator: TimeInterpolator = LinearInterpolator(),
    override val repeatMode: Int = ObjectAnimator.REVERSE
) : Effect