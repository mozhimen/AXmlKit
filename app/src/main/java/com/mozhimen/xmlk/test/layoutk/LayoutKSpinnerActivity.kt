package com.mozhimen.xmlk.test.layoutk

import android.os.Bundle
import android.view.View
import android.widget.AdapterView
import android.widget.AdapterView.OnItemSelectedListener
import com.mozhimen.kotlin.elemk.androidx.appcompat.bases.databinding.BaseActivityVDB
import com.mozhimen.kotlin.utilk.android.widget.showToast
import com.mozhimen.xmlk.test.databinding.ActivityLayoutkSpinnerBinding

/**
 * @ClassName AdapterKActivity
 * @Description TODO
 * @Author Mozhimen & Kolin Zhao
 * @Version 1.0
 */
class LayoutKSpinnerActivity : BaseActivityVDB<ActivityLayoutkSpinnerBinding>() {
    override fun initView(savedInstanceState: Bundle?) {
        vdb.layoutkSpinner.setEntries(listOf("GTA6", "唱跳", "Rap")).onItemSelectedListener = object : OnItemSelectedListener {
            override fun onItemSelected(parent: AdapterView<*>?, view: View?, position: Int, id: Long) {
                vdb.layoutkSpinner.getSelectItem().toString().showToast()
            }

            override fun onNothingSelected(parent: AdapterView<*>?) {

            }
        }
    }
}