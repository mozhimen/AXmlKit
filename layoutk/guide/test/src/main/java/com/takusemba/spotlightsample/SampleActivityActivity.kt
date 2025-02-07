package com.takusemba.spotlightsample

import android.os.Bundle
import android.view.View
import android.view.animation.DecelerateInterpolator
import android.widget.FrameLayout
import com.mozhimen.kotlin.utilk.android.widget.showToast
import com.mozhimen.uik.databinding.bases.viewbinding.activity.BaseActivityVB
import com.mozhimen.xmlk.layoutk.guide.GuideProcess
import com.mozhimen.xmlk.layoutk.guide.GuideStep
import com.mozhimen.xmlk.layoutk.guide.commons.IOnProcessEventListener
import com.mozhimen.xmlk.layoutk.guide.commons.IOnStepEventListener
import com.mozhimen.xmlk.layoutk.guide.impls.ShapeCircle
import com.takusemba.spotlightsample.databinding.ActivityActivitySampleBinding

class SampleActivityActivity : BaseActivityVB<ActivityActivitySampleBinding>() {


    override fun initView(savedInstanceState: Bundle?) {
        vb.start.setOnClickListener { startButton ->
            val guideSteps = ArrayList<GuideStep>()

            // first target
            val view1 = layoutInflater.inflate(R.layout.layout_target, FrameLayout(this))
            val guideStep1 = GuideStep.Builder()
                .setAnchor(vb.one)
                .setShape(ShapeCircle(100f))
                .setOverlay(view1)
                .setOnStepEventListener(object : IOnStepEventListener {
                    override fun onStart() {
                        "first target is started".showToast()
                    }

                    override fun onEnd() {
                        "first target is ended".showToast()
                    }
                })
                .build()
            guideSteps.add(guideStep1)

            // second target
            val view2 = layoutInflater.inflate(R.layout.layout_target, FrameLayout(this))
            val guideStep2 = GuideStep.Builder()
                .setAnchor(vb.two)
                .setShape(ShapeCircle(150f))
                .setOverlay(view2)
                .setOnStepEventListener(object : IOnStepEventListener {
                    override fun onStart() {
                        "second target is started".showToast()
                    }

                    override fun onEnd() {
                        "second target is ended".showToast()
                    }
                })
                .build()
            guideSteps.add(guideStep2)

            // third target
            val view3=layoutInflater.inflate(R.layout.layout_target, FrameLayout(this))
            val guideStep3 = GuideStep.Builder()
                .setAnchor(vb.three)
                .setShape(ShapeCircle(200f))
                .setOverlay(view3)
                .setOnStepEventListener(object : IOnStepEventListener {
                    override fun onStart() {
                        "third target is started".showToast()
                    }

                    override fun onEnd() {
                        "third target is ended".showToast()
                    }
                })
                .build()
            guideSteps.add(guideStep3)

            // create spotlight
            val guideProcess = GuideProcess.Builder()
                .setSteps(guideSteps)
                .setBackgroundColorRes(R.color.spotlightBackground, this)
                .setDuration(1000L)
                .setTimeInterpolator(DecelerateInterpolator(2f))
                .setOnProcessEventListener(object : IOnProcessEventListener {
                    override fun onStart() {
                        "spotlight is started".showToast()
                        startButton.isEnabled = false
                    }

                    override fun onEnd() {
                        "spotlight is ended".showToast()
                        startButton.isEnabled = true
                    }
                })
                .build(this)
            guideProcess.start()

            val nextTarget = View.OnClickListener { guideProcess.next() }
            val closeSpotlight = View.OnClickListener { guideProcess.finish() }

            view1.findViewById<View>(R.id.close_target).setOnClickListener(nextTarget)
            view2.findViewById<View>(R.id.close_target).setOnClickListener(nextTarget)
            view3.findViewById<View>(R.id.close_target).setOnClickListener(nextTarget)

            view1.findViewById<View>(R.id.close_spotlight).setOnClickListener(closeSpotlight)
            view2.findViewById<View>(R.id.close_spotlight).setOnClickListener(closeSpotlight)
            view3.findViewById<View>(R.id.close_spotlight).setOnClickListener(closeSpotlight)
        }

    }
}
