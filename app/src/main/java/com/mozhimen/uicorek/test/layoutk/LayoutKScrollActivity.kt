package com.mozhimen.uicorek.test.layoutk

import android.os.Bundle
import android.util.Log
import com.mozhimen.basick.elemk.androidx.appcompat.bases.databinding.BaseActivityVDB
import com.mozhimen.uicorek.test.databinding.ActivityLayoutkScrollBinding

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
            Log.d(TAG, "initView: setOnCollapsedListener $it")
        }
    }
}