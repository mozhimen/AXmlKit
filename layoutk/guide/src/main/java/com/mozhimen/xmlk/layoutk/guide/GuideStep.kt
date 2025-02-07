package com.mozhimen.xmlk.layoutk.guide

import android.graphics.PointF
import android.view.View
import com.mozhimen.xmlk.layoutk.guide.commons.Effect
import com.mozhimen.xmlk.layoutk.guide.commons.IOnStepEventListener
import com.mozhimen.xmlk.layoutk.guide.commons.Shape
import com.mozhimen.xmlk.layoutk.guide.impls.EffectEmpty
import com.mozhimen.xmlk.layoutk.guide.impls.ShapeCircle

/**
 * Target represents the spot that Spotlight will cast.
 */
class GuideStep(
    val anchor: PointF,
    val shape: Shape,
    val effect: Effect,
    val overlay: View?,
    val iOnStepEventListener: IOnStepEventListener?,
) {

    /**
     * [Builder] to build a [GuideStep].
     * All parameters should be set in this [Builder].
     */
    class Builder {

        private var _anchor: PointF = PointF(0f, 0f)
        private var _shape: Shape = ShapeCircle(100f)
        private var _effect: Effect = EffectEmpty()
        private var _overlay: View? = null
        private var _iOnStepEventListener: IOnStepEventListener? = null

        //////////////////////////////////////////////////////////////////////////////////////////

        /**
         * Sets a pointer to start a [GuideStep].
         */
        fun setAnchor(view: View): Builder = apply {
            val location = IntArray(2)
            view.getLocationInWindow(location)
            val x = location[0] + view.width / 2f
            val y = location[1] + view.height / 2f
            setAnchor(x, y)
        }

        /**
         * Sets an anchor point to start [GuideStep].
         */
        fun setAnchor(x: Float, y: Float): Builder = apply {
            setAnchor(PointF(x, y))
        }

        /**
         * Sets an anchor point to start [GuideStep].
         */
        fun setAnchor(anchor: PointF): Builder = apply {
            this._anchor = anchor
        }

        /**
         * Sets [shape] of the spot of [GuideStep].
         */
        fun setShape(shape: Shape): Builder = apply {
            this._shape = shape
        }

        /**
         * Sets [effect] of the spot of [GuideStep].
         */
        fun setEffect(effect: Effect): Builder = apply {
            this._effect = effect
        }

        /**
         * Sets [overlay] to be laid out to describe [GuideStep].
         */
        fun setOverlay(overlay: View): Builder = apply {
            this._overlay = overlay
        }

        /**
         * Sets [OnTargetListener] to notify the state of [GuideStep].
         */
        fun setOnStepEventListener(listener: IOnStepEventListener): Builder = apply {
            this._iOnStepEventListener = listener
        }

        //////////////////////////////////////////////////////////////////////////////////////////

        fun build() = GuideStep(
            anchor = _anchor,
            shape = _shape,
            effect = _effect,
            overlay = _overlay,
            iOnStepEventListener = _iOnStepEventListener
        )
    }
}