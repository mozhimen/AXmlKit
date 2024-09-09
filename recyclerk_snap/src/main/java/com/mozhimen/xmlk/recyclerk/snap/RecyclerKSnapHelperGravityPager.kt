package com.mozhimen.xmlk.recyclerk.snap

import java.lang.Deprecated

/**
 * @ClassName RecyclerKSnapHelperGravityPager
 * @Description TODO
 * @Author Mozhimen / Kolin Zhao
 * @Date 2024/9/8 20:00
 * @Version 1.0
 */
/**
 * A {@link GravitySnapHelper} that sets a default max scroll distance
 * of the size of the RecyclerView by setting
 * {@link GravitySnapHelper#setMaxFlingSizeFraction(float)} to 1.0f by default
 *
 * @deprecated Use {@link GravitySnapHelper} instead
 */
@Deprecated
class RecyclerKSnapHelperGravityPager : RecyclerKSnapHelperGravity {
    constructor(gravity: Int) : this(gravity, null)
    constructor(gravity: Int, snapListener: SnapListener?) : super(gravity, false, snapListener) {
        setMaxFlingSizeFraction(1f)
        setScrollMsPerInch(50f)
    }
}