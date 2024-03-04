package com.mozhimen.uicorek.test.toastk

import android.os.Bundle
import com.mozhimen.basick.elemk.androidx.appcompat.bases.databinding.BaseActivityVDB
import com.mozhimen.basick.lintk.optins.OApiDeprecated_Official_AfterV_30_11_R
import com.mozhimen.basick.utilk.android.widget.showToast
import com.mozhimen.uicorek.toastk.builder.ToastKViewBuilder
import com.mozhimen.uicorek.test.databinding.ActivityToastkBinding

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