package com.takusemba.spotlightsample

import android.os.Bundle
import android.widget.ArrayAdapter
import com.mozhimen.kotlin.utilk.android.content.startContext
import com.mozhimen.uik.databinding.bases.viewbinding.activity.BaseActivityVB
import com.takusemba.spotlightsample.databinding.ActivityChooserBinding

class MainActivity : BaseActivityVB<ActivityChooserBinding>() {

    private val samples: Array<String> = arrayOf(
        SAMPLE_SPOTLIGHT_ON_ACTIVITY,
        SAMPLE_SPOTLIGHT_ON_FRAGMENT
    )

    override fun initView(savedInstanceState: Bundle?) {
        vb.sampleList.adapter = ArrayAdapter(this, android.R.layout.simple_list_item_1, samples)
        vb.sampleList.setOnItemClickListener { _, _, position, _ ->
            when (samples[position]) {
                SAMPLE_SPOTLIGHT_ON_ACTIVITY -> {
                    startContext<SampleActivityActivity>()
                }

                SAMPLE_SPOTLIGHT_ON_FRAGMENT -> {
                    startContext<SampleFragmentActivity>()
                }
            }
        }
    }

    companion object {
        private const val SAMPLE_SPOTLIGHT_ON_ACTIVITY = "Spotlight on Activity"
        private const val SAMPLE_SPOTLIGHT_ON_FRAGMENT = "Spotlight on Fragment"
    }
}
