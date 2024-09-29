package com.mozhimen.xmlk.test.layoutk

import android.os.Bundle
import android.util.Log
import com.mozhimen.kotlin.utilk.android.util.UtilKLogWrapper
import com.mozhimen.bindk.bases.activity.databinding.BaseActivityVDB
import com.mozhimen.xmlk.test.databinding.ActivityLayoutkScrollBinding

/**
 * @ClassName LayoutKScrollActivity
 * @Description TODO
 * @Author Mozhimen & Kolin Zhao
 * @Date 2024/1/5
 * @Version 1.0
 */
class LayoutKScrollActivity : BaseActivityVDB<ActivityLayoutkScrollBinding>() {
    override fun initView(savedInstanceState: Bundle?) {
        vdb.scrollCollapsing.setOnCollapsedListener {
            UtilKLogWrapper.d(TAG, "initView: setOnCollapsedListener $it")
        }
    }
}