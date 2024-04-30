package com.mozhimen.xmlk.demo

import android.os.Bundle
import com.mozhimen.basick.utilk.android.util.UtilKLogWrapper
import androidx.recyclerview.widget.RecyclerView
import com.mozhimen.adaptk.systembar.annors.AAdaptKSystemBarProperty
import com.mozhimen.adaptk.systembar.cons.CProperty
import com.mozhimen.adaptk.systembar.initAdaptKSystemBar
import com.mozhimen.basick.elemk.androidx.appcompat.bases.databinding.BaseActivityVDB
import com.mozhimen.xmlk.demo.databinding.ActivityMainBinding
import com.mozhimen.xmlk.demo.databinding.ItemDemoListBinding
import com.mozhimen.xmlk.recyclerk.quick.AdapterKQuickRecyclerStuffedVB
import kotlin.math.abs

@AAdaptKSystemBarProperty(property = CProperty.IMMERSED_HARD_STICKY)
class MainActivity : BaseActivityVDB<ActivityMainBinding>() {
    private var _scrollY = 0
    private var _alpha = 0

    override fun initFlag() {
        initAdaptKSystemBar()
    }

    override fun initView(savedInstanceState: Bundle?) {
        val list = arrayListOf(
            Astro("白羊座", "晴天", 90),
            Astro("天蝎座", "雨天", 70),
            Astro("金牛座", "晴天", 90),
            Astro("水瓶座", "雷阵雨", 80),
            Astro("处女座", "晴天", 90),
            Astro("双子座", "晴天", 90),
            Astro("射手座", "晴天", 90),
        )
        vdb.demoRecycler.layoutManager = LinearLayoutManager(this)
        vdb.demoRecycler.adapter =
            AdapterKQuickRecyclerStuffedVB<Astro, ItemDemoListBinding>(list, R.layout.item_demo_list, R.layout.item_demo_header, null, BR.itemAstro) { holder, itemData, position, _ ->
                if (position in 1 until list.size) {
                    holder.vdb.demoItemListBtn.setOnClickListener {
                        UtilKLogWrapper.i(TAG, itemData.name)
                    }
                }
            }
        vdb.demoRecycler.addOnScrollListener(object : RecyclerView.OnScrollListener() {
            override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {
                super.onScrolled(recyclerView, dx, dy)
                _scrollY += dy
                _alpha = if (abs(_scrollY) > 1000) {
                    vdb.demoBg.setBlurOffset(100)
                    100
                } else {
                    vdb.demoBg.setBlurOffset(_scrollY / 10)
                    abs(_scrollY) / 10
                }
                vdb.demoBg.setBlurLevel(_alpha)
            }
        })
    }
}