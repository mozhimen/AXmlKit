package com.mozhimen.xmlk.textk.test

import android.os.Bundle
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.mozhimen.kotlin.elemk.androidx.appcompat.bases.BaseActivity
import com.mozhimen.uik.databinding.bases.viewbinding.activity.BaseActivityVB
import com.mozhimen.xmlk.textk.test.databinding.ActivityTextkSwitcherBinding

class TextKSwitcherActivity : BaseActivityVB<ActivityTextkSwitcherBinding>() {
    override fun initView(savedInstanceState: Bundle?) {
        val strs = listOf("a", "b", "c")
        vb.textkSwitcher.setDataList(strs)
    }

    override fun onResume() {
        super.onResume()
        vb.textkSwitcher.startScroll()
    }

    override fun onPause() {
        vb.textkSwitcher.stopScroll()
        super.onPause()
    }
}