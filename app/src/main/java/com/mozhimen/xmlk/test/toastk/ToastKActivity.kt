package com.mozhimen.xmlk.test.toastk

import android.os.Bundle
import com.mozhimen.uik.databinding.bases.viewdatabinding.activity.BaseActivityVDB
import com.mozhimen.kotlin.lintk.optins.OApiDeprecated_Official_AfterV_30_11_R
import com.mozhimen.kotlin.utilk.android.widget.showToast
import com.mozhimen.xmlk.toastk.builder.ToastKViewBuilder
import com.mozhimen.xmlk.test.databinding.ActivityToastkBinding

class ToastKActivity : BaseActivityVDB<ActivityToastkBinding>() {
    @OptIn(OApiDeprecated_Official_AfterV_30_11_R::class)
    override fun initView(savedInstanceState: Bundle?) {
        vdb.toastkBtnNormal.setOnClickListener {
            "这是原生Toast".showToast()
        }
        vdb.toastkBtnCustom.setOnClickListener {
            ToastKViewBuilder.makeText("这是自定义Toast").show()
        }
    }
}