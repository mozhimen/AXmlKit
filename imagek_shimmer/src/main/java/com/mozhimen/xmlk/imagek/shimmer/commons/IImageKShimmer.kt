package com.mozhimen.xmlk.imagek.shimmer.commons

import com.mozhimen.xmlk.drawablek.DrawableKShimmer

/**
 * @ClassName IImageKShimmer
 * @Description TODO
 * @Author mozhimen
 * @Date 2025/1/7
 * @Version 1.0
 */
interface IImageKShimmer<T> {
    fun setShimmerConfig(shimmerConfig: DrawableKShimmer.ShimmerConfig):T
    fun startShimmer()
    fun stopShimmer()
    fun showShimmer(startShimmer: Boolean)
    fun hideShimmer()
    fun setStaticAnimationProgress(value: Float)
    fun clearStaticAnimationProgress()
}