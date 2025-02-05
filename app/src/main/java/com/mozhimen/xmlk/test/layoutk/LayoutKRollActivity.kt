package com.mozhimen.xmlk.test.layoutk

import android.graphics.Color
import android.os.Bundle
import com.mozhimen.uik.databinding.bases.viewdatabinding.activity.BaseActivityVDB
import com.mozhimen.kotlin.lintk.annors.ADigitPlace
import com.mozhimen.kotlin.lintk.optins.OApiCall_BindLifecycle
import com.mozhimen.kotlin.lintk.optins.OApiInit_ByLazy
import com.mozhimen.taskk.temps.TaskKPoll
import com.mozhimen.kotlin.utilk.android.util.UtilKLogWrapper
import com.mozhimen.kotlin.utilk.kotlin.UtilKLazyJVM.lazy_ofNone
import com.mozhimen.xmlk.layoutk.roll.annors.AAnimatorMode
import com.mozhimen.xmlk.test.databinding.ActivityLayoutkRollBinding
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

class LayoutKRollActivity : BaseActivityVDB<ActivityLayoutkRollBinding>() {
    @OptIn(OApiCall_BindLifecycle::class, OApiInit_ByLazy::class)
    private val _taskKPoll by lazy_ofNone { TaskKPoll().apply { bindLifecycle(this@LayoutKRollActivity) } }
    private val list = "我喜欢唱跳RAP篮球!"

    @OptIn(OApiCall_BindLifecycle::class, OApiInit_ByLazy::class)
    override fun initView(savedInstanceState: Bundle?) {
        vdb.rollTextDigit.setTextViewStyle(20f, Color.WHITE)
        vdb.rollTextDigit.post {
            _taskKPoll.start(2000L, 10, task = { index ->
                withContext(Dispatchers.Main) {
                    UtilKLogWrapper.d(TAG,"index $index")
                    vdb.rollTextDigit.setCurrentValue(index, ADigitPlace.PLACE_UNIT, AAnimatorMode.DOWN)
                    vdb.rollTextTxt.setCurrentValue(list[10 - index].toString().also { UtilKLogWrapper.d(TAG,"setCurrentValue $it") },animatorDuration = 700)
                }
            })
        }
    }
}