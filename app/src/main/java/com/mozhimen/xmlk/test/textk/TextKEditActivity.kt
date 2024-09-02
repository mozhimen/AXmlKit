package com.mozhimen.xmlk.test.textk

import android.view.View
import android.view.ViewGroup
import com.mozhimen.kotlin.elemk.androidx.appcompat.bases.databinding.BaseActivityVDB
import com.mozhimen.kotlin.utilk.android.widget.showToast
import com.mozhimen.xmlk.textk.edit.bar.TextKEditBarActivity
import com.mozhimen.xmlk.textk.edit.bar.commons.ITextKEditBarListener
import com.mozhimen.xmlk.test.databinding.ActivityTextkEditBinding

class TextKEditActivity : BaseActivityVDB<ActivityTextkEditBinding>() {
    fun goTextKEditBar(view: View) {
        TextKEditBarActivity.openEditBarDefault(this, object : ITextKEditBarListener {
            override fun onCancel() {
            }

            override fun onIllegal() {
            }

            override fun onSubmit(content: String?) {
                content?.showToast()
            }

            override fun onAttached(rootView: ViewGroup?) {
            }

        }, null)
    }
}