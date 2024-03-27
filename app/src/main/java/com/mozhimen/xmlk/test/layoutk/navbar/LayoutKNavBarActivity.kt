package com.mozhimen.xmlk.test.layoutk.navbar

import android.graphics.Color
import android.os.Bundle
import com.mozhimen.basick.elemk.androidx.appcompat.bases.databinding.BaseActivityVDB
import com.mozhimen.basick.utilk.android.util.dp2px
import com.mozhimen.basick.utilk.android.widget.showToast
import com.mozhimen.basick.utilk.android.util.sp2px
import com.mozhimen.basick.utilk.wrapper.UtilKRes
import com.mozhimen.xmlk.test.R
import com.mozhimen.xmlk.test.databinding.ActivityLayoutkNavbarBinding

class LayoutKNavBarActivity : BaseActivityVDB<ActivityLayoutkNavbarBinding>() {

    override fun initView(savedInstanceState: Bundle?) {
        vdb.layoutkNavbar3.addLeftImage(UtilKRes.gainDrawable(R.drawable.layoutk_navbar_back)!!, 40f.dp2px().toInt(), 2f.dp2px().toInt()) {
            setOnClickListener {
                finish()
            }
        }
        vdb.layoutkNavbar3.addLeftBtnKIconFont(
            UtilKRes.gainString(R.string.icon_mine),
            40f.dp2px().toInt(),
            30f.sp2px().toInt(),
            Color.BLACK,
            2f.dp2px().toInt(),
            "fonts/iconfont.ttf"
        ) {
            setOnClickListener {
                "你点击了图标".showToast()
            }
        }
    }
}