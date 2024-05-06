package com.mozhimen.xmlk.recyclerk.decoration

import android.graphics.Rect
import android.view.View
import androidx.recyclerview.widget.RecyclerView
import com.mozhimen.basick.utilk.androidx.recyclerview.UtilKRecyclerViewWrapper
import com.mozhimen.xmlk.recyclerk.decoration.bases.BaseRecyclerKDecoration

/**
 * @ClassName RecyclerDecorationGap2
 * @Description TODO
 * @Author Mozhimen & Kolin Zhao
 * @Version 1.0
 */
open class RecyclerKDecorationGapLinear : BaseRecyclerKDecoration {

    constructor(gapOuter: Int) : this(gapOuter, gapOuter / 2)

    constructor(gapOuter: Int, gapInner: Int) : this(gapOuter, gapInner, gapOuter)

    constructor(gapOuter: Int, gapInner: Int, gapOther: Int) {
        _gapOuter = gapOuter
        _gapInner = gapInner
        _gapOther = gapOther
    }

    ////////////////////////////////////////////////////////////////

    protected var _gapOuter: Int = 0
    protected var _gapInner: Int = 0
    protected var _gapOther: Int = 0//另外两边的gap距离

    ////////////////////////////////////////////////////////////////

    override fun getItemOffsets(outRect: Rect, view: View, parent: RecyclerView, state: RecyclerView.State) {
        super.getItemOffsets(outRect, view, parent, state)
        equilibriumAssignment(outRect, view, parent, state)
    }

    ////////////////////////////////////////////////////////////////

    protected open fun equilibriumAssignment(outRect: Rect, view: View, parent: RecyclerView, state: RecyclerView.State){
        UtilKRecyclerViewWrapper.equilibriumAssignment_ofLinearLayoutManager(parent, view, outRect, _gapOuter, _gapInner, _gapOther)
    }
}