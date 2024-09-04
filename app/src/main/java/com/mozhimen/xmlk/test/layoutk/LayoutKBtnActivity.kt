package com.mozhimen.xmlk.test.layoutk

import android.os.Bundle
import android.util.Log
import com.mozhimen.kotlin.utilk.android.util.UtilKLogWrapper
import com.mozhimen.mvvmk.bases.activity.databinding.BaseActivityVDB
import com.mozhimen.xmlk.layoutk.btn.LayoutKBtnSwitch
import com.mozhimen.xmlk.test.databinding.ActivityLayoutkBtnBinding

/**
 * @ClassName LayoutKBtnActivity
 * @Description TODO
 * @Author mozhimen / Kolin Zhao
 * @Date 2022/11/6 22:52
 * @Version 1.0
 */
class LayoutKBtnActivity : BaseActivityVDB<ActivityLayoutkBtnBinding>() {
    private lateinit var _layoutKBtnSwitch: LayoutKBtnSwitch

    override fun initView(savedInstanceState: Bundle?) {
        _layoutKBtnSwitch = vdb.layoutkBtnSwitch
        _layoutKBtnSwitch.setDefaultStatus(false)
        _layoutKBtnSwitch.setOnSwitchListener { status ->
            UtilKLogWrapper.i(TAG, "btnk_switch status $status")
        }
    }
}
