package com.mozhimen.xmlk.test.popwink

import android.view.View
import com.mozhimen.bindk.bases.activity.databinding.BaseActivityVDB
import com.mozhimen.kotlin.utilk.kotlin.UtilKLazyJVM.lazy_ofNone
import com.mozhimen.xmlk.test.databinding.ActivityPopwinkBinding
import com.mozhimen.xmlk.test.popwink.temps.PopwinKAnim
import com.mozhimen.xmlk.test.popwink.temps.PopwinKSelector
import com.mozhimen.xmlk.test.popwink.temps.PopwinKTest


/**
 * @ClassName PopwinKActivity
 * @Description TODO
 * @Author Mozhimen & Kolin Zhao
 * @Version 1.0
 */
class PopwinKActivity : BaseActivityVDB<ActivityPopwinkBinding>() {

    private val _popwinK by lazy_ofNone { PopwinKAnim(this) }
    fun showPopwinK(view: View) {
        _popwinK.showPopupWindow()
    }

    private val _popwinKTest by lazy_ofNone { PopwinKTest(this, "inited") }
    fun showPopwinKTest(view: View) {
        _popwinKTest.showPopupWindow()
    }

    private val _popwinKSelector by lazy_ofNone { PopwinKSelector(this, mutableListOf("1", "2", "3")) }
    fun showPopwinKSelectorNormal(view: View) {
        if (_popwinKSelector.isShowing) _popwinKSelector.dismiss()
        _popwinKSelector.setItems(listOf("Java", "Kotlin", "ObjectC", "Swift", "Dart", "C#", "C++", "C", "Python"))
        _popwinKSelector.showPopupWindow()
    }

    fun showPopwinKSelectorName(view: View) {
        if (_popwinKSelector.isShowing) _popwinKSelector.dismiss()
        _popwinKSelector.setItems(listOf("上海", "北京", "广州", "深圳", "苏州", "无锡", "杭州", "常州", "南通", "嘉兴"))
        _popwinKSelector.showPopupWindow()
    }
}