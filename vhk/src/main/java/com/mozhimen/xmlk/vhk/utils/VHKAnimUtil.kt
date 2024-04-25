package com.mozhimen.xmlk.vhk.utils

import android.animation.Animator
import android.animation.AnimatorListenerAdapter
import android.view.View
import androidx.recyclerview.widget.RecyclerView
import com.mozhimen.basick.animk.builder.AnimKBuilder
import com.mozhimen.basick.animk.builder.utils.AnimKTypeUtil
import com.mozhimen.basick.elemk.android.animation.BaseViewHolderAnimatorListenerAdapter
import com.mozhimen.basick.utilk.android.view.applyGone
import com.mozhimen.basick.utilk.android.view.applyVisible

/**
 * @ClassName VHKAnimUtil
 * @Description TODO
 * @Author Mozhimen & Kolin Zhao
 * @Date 2024/4/25
 * @Version 1.0
 */
object VHKAnimUtil {
    //参数介绍：1、holder对象 2、展开部分的View，由holder.getExpandView()方法获取 3、animate参数为true，则有动画效果
    @JvmStatic
    fun expandViewHolder(viewHolder: RecyclerView.ViewHolder, expandView: View, animate: Boolean) {
        if (animate) {
            expandView.applyVisible()
            val animatorIntType =
                AnimKTypeUtil.get_ofHeight_viewHolder(viewHolder)//改变高度的动画
            val animatorAlphaViewType =
                AnimKTypeUtil.get_ofAlphaView(expandView, 0f, 1f)
                    .addAnimatorListener(BaseViewHolderAnimatorListenerAdapter(viewHolder))//扩展的动画，透明度动画开始
            AnimKBuilder.asAnimator().combine(animatorIntType).combine(animatorAlphaViewType).build().start()
        } else { //为false时直接显示
            expandView.applyVisible()
            expandView.alpha = 1f
        }
    }

    //类似于打开的方法
    @JvmStatic
    fun foldViewHolder(viewHolder: RecyclerView.ViewHolder, expandView: View, animate: Boolean) {
        if (animate) {
            expandView.applyGone()
            val animatorIntType = AnimKTypeUtil.get_ofHeight_viewHolder(viewHolder).addAnimatorListener(object : AnimatorListenerAdapter() {
                override fun onAnimationEnd(animation: Animator) {
                    expandView.applyGone()
                    expandView.alpha = 0f
                }

                override fun onAnimationCancel(animation: Animator) {
                    expandView.applyGone()
                    expandView.alpha = 0f
                }
            })
            expandView.applyVisible()
            animatorIntType.build().start()
        } else {
            expandView.applyGone()
            expandView.alpha = 0f
        }
    }
}