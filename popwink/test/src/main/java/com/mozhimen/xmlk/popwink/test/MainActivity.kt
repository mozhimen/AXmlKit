package com.mozhimen.xmlk.popwink.test

import android.os.Bundle
import android.view.View
import com.mozhimen.uik.databinding.bases.viewbinding.activity.BaseActivityVB
import com.mozhimen.xmlk.popwink.builder.PopwinK
import com.mozhimen.xmlk.popwink.test.databinding.ActivityMainBinding

class MainActivity : BaseActivityVB<ActivityMainBinding>() {
    override fun initView(savedInstanceState: Bundle?) {
        vb.mainView.setOnClickListener {
            showPopwin(it)
        }
    }

    private var _popwin: PopwinK? = null

    private fun showPopwin(view: View) {
        if (_popwin == null) {
            _popwin = PopwinK.Builder(this)
                .view(R.layout.layout_popwin)
                .build()
        }
        _popwin!!.showAsDropDown(view)
    }
}