package com.mozhimen.xmlk.layoutk.refresh.impls

import android.content.Context
import android.util.AttributeSet
import android.view.LayoutInflater
import com.airbnb.lottie.LottieAnimationView
import com.mozhimen.xmlk.layoutk.refresh.R
import com.mozhimen.xmlk.layoutk.refresh.commons.RefreshOverView

/**
 * @ClassName LottieOverView
 * @Description TODO
 * @Author mozhimen / Kolin Zhao
 * @Date 2022/4/18 23:14
 * @Version 1.0
 */
class LottieOverView @JvmOverloads constructor(context: Context, attrs: AttributeSet? = null, defStyleAttr: Int = 0) :
    RefreshOverView(context, attrs, defStyleAttr) {
    private lateinit var _lottieOverview: LottieAnimationView
    override fun init() {
        LayoutInflater.from(context).inflate(R.layout.layoutk_refresh_overview_lottie, this, true)
        _lottieOverview = findViewById(R.id.layoutk_refresh_overview_lottie)
        _lottieOverview.setAnimation("layoutk_refresh_loading_wave.json")
    }

    override fun onScroll(scrollY: Int, pullRefreshHeight: Int) {
    }

    override fun onVisible() {
    }

    override fun onOverflow() {
    }

    override fun onStartRefresh() {
        _lottieOverview.speed = 2f
        _lottieOverview.playAnimation()
    }

    override fun onFinish() {
        _lottieOverview.progress = 0f
        _lottieOverview.cancelAnimation()
    }
}