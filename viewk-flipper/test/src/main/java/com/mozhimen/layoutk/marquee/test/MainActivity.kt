package com.mozhimen.layoutk.marquee.test

import android.os.Bundle
import android.view.View
import android.widget.TextView
import androidx.lifecycle.lifecycleScope
import com.mozhimen.uik.databinding.bases.viewdatabinding.activity.BaseActivityVDB
import com.mozhimen.kotlin.lintk.optins.api.OApiCall_BindLifecycle
import com.mozhimen.kotlin.lintk.optins.api.OApiCall_BindViewLifecycle
import com.mozhimen.kotlin.lintk.optins.api.OApiInit_ByLazy
import com.mozhimen.layoutk.marquee.test.databinding.ActivityMainBinding
import com.mozhimen.xmlk.viewk.flipper.bases.BaseFlipAdapter
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

class MainActivity : BaseActivityVDB<ActivityMainBinding>() {

//    private val _randoms = arrayOf("a", "b", "c", "d", "e", "f")

    private var _i = 0

    @OptIn(OApiInit_ByLazy::class, OApiCall_BindLifecycle::class, OApiCall_BindViewLifecycle::class)
    override fun initView(savedInstanceState: Bundle?) {
//        vdb.mainMarquee.setItemCountPerLine(1)
        val adapter = TextsAdapter().apply {
            bindLifecycle(this@MainActivity)
        }
        vdb.mainMarquee.setFlipAdapter(adapter)
        lifecycleScope.launch {
            while (true) {
                delay(5000)
                _i++
                val list = mutableListOf<String>()
                repeat(3) { i ->
                    list.add((_i.toString()+i.toString()))
                }
                adapter.setDatas(list, true)
            }
        }
    }

    @OptIn(OApiInit_ByLazy::class, OApiCall_BindLifecycle::class, OApiCall_BindViewLifecycle::class)
    class TextsAdapter : BaseFlipAdapter<String>(R.layout.layout_txt) {
        override fun onBindView(view: View, position: Int, data: String?) {
//            UtilKLogWrapper.d(TAG, "onBindView: position $position data $data")

            (view as? TextView?)?.text = data ?: ""
        }
    }
}

private operator fun String.times(i: Int): String {
    val stringBuilder = StringBuilder()
    kotlin.repeat(i) {
        stringBuilder.append(this)
    }
    return stringBuilder.toString()
}
