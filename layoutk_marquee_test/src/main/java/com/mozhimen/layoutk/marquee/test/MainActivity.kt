package com.mozhimen.layoutk.marquee.test

import android.os.Bundle
import android.view.View
import android.widget.TextView
import com.mozhimen.bindk.bases.viewdatabinding.activity.BaseActivityVDB
import com.mozhimen.kotlin.lintk.optins.OApiCall_BindLifecycle
import com.mozhimen.kotlin.lintk.optins.OApiCall_BindViewLifecycle
import com.mozhimen.kotlin.lintk.optins.OApiInit_ByLazy
import com.mozhimen.layoutk.marquee.bases.BaseMarqueeAdapter
import com.mozhimen.layoutk.marquee.test.databinding.ActivityMainBinding

class MainActivity : BaseActivityVDB<ActivityMainBinding>() {
    @OptIn(OApiInit_ByLazy::class, OApiCall_BindLifecycle::class, OApiCall_BindViewLifecycle::class)
    override fun initView(savedInstanceState: Bundle?) {
        vdb.mainMarquee.setItemCountPerLine(1)
        val adapter = TextsAdapter().apply {
            bindLifecycle(this@MainActivity)
        }
        vdb.mainMarquee.setMarqueeAdapter(adapter)
        adapter.setDatas(listOf("aaaaaaaaaaaa", "bbbbbbbbbbbb", "vvvvvvvvvvv"),true)
    }

    @OptIn(OApiInit_ByLazy::class, OApiCall_BindLifecycle::class, OApiCall_BindViewLifecycle::class)
    class TextsAdapter : BaseMarqueeAdapter<String>(R.layout.layout_txt) {
        override fun onBindView(view: View, position: Int, data: String?) {
            (view as? TextView?)?.text = data ?: ""
        }
    }
}