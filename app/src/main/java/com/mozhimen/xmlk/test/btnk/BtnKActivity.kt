package com.mozhimen.xmlk.test.btnk

import android.os.Bundle
import com.mozhimen.uik.databinding.bases.viewdatabinding.activity.BaseActivityVDB
import com.mozhimen.xmlk.test.databinding.ActivityBtnkBinding

class BtnKActivity : BaseActivityVDB<ActivityBtnkBinding>() {
    override fun initView(savedInstanceState: Bundle?) {
        vdb.btnkPwdVisible.bindEditText(vdb.btnkPwdVisibleEdit)
    }
}