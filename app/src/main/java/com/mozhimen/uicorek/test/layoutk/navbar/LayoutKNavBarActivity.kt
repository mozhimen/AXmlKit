package com.mozhimen.uicorek.test.layoutk.navbar

import android.graphics.Color
import android.os.Bundle
import com.mozhimen.basick.elemk.androidx.appcompat.bases.databinding.BaseActivityVB
import com.mozhimen.basick.utilk.android.util.dp2px
import com.mozhimen.basick.utilk.android.widget.showToast
import com.mozhimen.basick.utilk.android.content.UtilKRes
import com.mozhimen.basick.utilk.android.util.sp2px
import com.mozhimen.uicorek.test.R
import com.mozhimen.uicorek.test.databinding.ActivityLayoutkNavbarBinding

class LayoutKNavBarActivity : BaseActivityVB<ActivityLayoutkNavbarBinding>() {

    override fun initView(savedInstanceState: Bundle?) {
        vb.layoutkNavbar3.addLeftImage(UtilKRes.gainDrawable(R.drawable.layoutk_navbar_back)!!, 40f.dp2px().toInt(), 2f.dp2px().toInt()) {
            setOnClickListener {
                finish()
            }
        }
        vb.layoutkNavbar3.addLeftBtnKIconFont(
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