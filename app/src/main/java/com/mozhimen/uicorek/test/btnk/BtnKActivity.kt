package com.mozhimen.uicorek.test.btnk

import android.os.Bundle
import com.mozhimen.basick.elemk.androidx.appcompat.bases.databinding.BaseActivityVB
import com.mozhimen.uicorek.test.databinding.ActivityBtnkBinding

class BtnKActivity : BaseActivityVB<ActivityBtnkBinding>() {
    override fun initView(savedInstanceState: Bundle?) {
        vb.btnkPwdVisible.bindEditText(vb.btnkPwdVisibleEdit)
    }
}