package com.mozhimen.uicorek.test.layoutk

import android.os.Bundle
import android.util.Log
import com.mozhimen.basick.elemk.androidx.appcompat.bases.databinding.BaseActivityVB
import com.mozhimen.uicorek.test.databinding.ActivityLayoutkScrollBinding

/**
 * @ClassName LayoutKScrollActivity
 * @Description TODO
 * @Author Mozhimen & Kolin Zhao
 * @Date 2024/1/5
 * @Version 1.0
 */
class LayoutKScrollActivity : BaseActivityVB<ActivityLayoutkScrollBinding>() {
    override fun initView(savedInstanceState: Bundle?) {
        vb.scrollCollapsing.setOnCollapsedListener {
            Log.d(TAG, "initView: setOnCollapsedListener $it")
        }
    }
}