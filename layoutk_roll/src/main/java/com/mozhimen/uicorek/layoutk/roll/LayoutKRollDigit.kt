package com.mozhimen.uicorek.layoutk.roll

import android.content.Context
import android.util.AttributeSet
import com.mozhimen.basick.lintk.annors.ADigitPlace
import com.mozhimen.uicorek.layoutk.roll.annors.AAnimatorMode

/**
 * @ClassName LayoutKRollText
 * @Description TODO
 * @Author Mozhimen / Kolin Zhao
 * @Date 2023/9/12 12:48
 * @Version 1.0
 */

class LayoutKRollDigit @JvmOverloads constructor(context: Context, attrs: AttributeSet? = null, defStyleAttr: Int = 0) :
    LayoutKRollText(context, attrs, defStyleAttr) {

    companion object {
        private const val DELAY_DURATION = 200L
    }

    //////////////////////////////////////////////////////////////////////////////////////////////

    /**
     * 设置下一位数字的值
     * @param digitPlace String 个，十，百位
     * @param value Int
     * @param mode Int
     */
    fun setCurrentValue(value: Int, @ADigitPlace digitPlace: Int = ADigitPlace.PLACE_UNIT, @AAnimatorMode mode: Int = AAnimatorMode.DOWN) {
        //判断数字是增加还是减少，进而确定不同的动画效果
        setCurrentValue(value.toString(), getAnimatorDelay(digitPlace), mode)
    }

    /**
     * 计算对应位置的延迟时间
     */
    private fun getAnimatorDelay(@ADigitPlace digitPlace: Int): Long {
        return when (digitPlace) {
            ADigitPlace.PLACE_UNIT -> DELAY_DURATION
            ADigitPlace.PLACE_HUNDRED -> DELAY_DURATION + 100
            ADigitPlace.PLACE_THOUSAND -> DELAY_DURATION * 2
            ADigitPlace.PLACE_TEN_THOUSAND -> DELAY_DURATION * 2 + 100
            ADigitPlace.PLACE_HUNDRED_THOUSAND -> DELAY_DURATION * 3
            else -> DELAY_DURATION * 3 + 100
        }
    }
}