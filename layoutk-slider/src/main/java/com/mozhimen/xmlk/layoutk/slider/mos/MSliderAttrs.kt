package com.mozhimen.xmlk.layoutk.slider.mos

/**
 * @ClassName LayoutKSliderAttr
 * @Description TODO
 * @Author mozhimen / Kolin Zhao
 * @Version 1.0
 */
internal data class MSliderAttrs(
    var sliderHeight: Float,
    var sliderRodLeftColor: Int,
    var sliderRodLeftGradientColor: Int,
    var sliderRodRightColor: Int,
    var rodScrollEnable: Boolean,
    var rodColor: Int,
    var rodColorInside: Int,
    var rodIsInside: Boolean,
    var rodRadius: Float,
    var rodRadiusInside: Float,
    var rodMinVal: Float,
    var rodMaxVal: Float,
    var rodDefaultPercent: Float
)