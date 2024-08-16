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
class RecyclerKDecorationGapGrid : BaseRecyclerKDecoration {

    constructor(gapOuter: Int) : this(gapOuter, gapOuter / 2)

    constructor(gapOuter: Int, gapInner: Int) : this(gapOuter, gapInner, gapOuter)

    constructor(gapOuter: Int, gapInner: Int, gapOther: Int) : this(gapOuter, gapInner, gapInner, gapOther)

    constructor(gapOuter: Int, gapInnerHorizontal: Int, gapInnerVertical: Int, gapOther: Int) {
        _gapOuter = gapOuter
        _gapInnerHorizontal = gapInnerHorizontal
        _gapInnerVertical = gapInnerVertical
        _gapOther = gapOther
    }

    ////////////////////////////////////////////////////////////////

    private var _gapOuter: Int = 0
    private var _gapInnerHorizontal: Int = 0
    private var _gapInnerVertical: Int = 0
    private var _gapOther: Int = 0

    ////////////////////////////////////////////////////////////////

    override fun getItemOffsets(outRect: Rect, view: View, parent: RecyclerView, state: RecyclerView.State) {
        super.getItemOffsets(outRect, view, parent, state)
        UtilKRecyclerViewWrapper.equilibriumAssignment_ofGridLayoutManager(parent, view, outRect, _gapOuter, _gapInnerHorizontal, _gapInnerVertical, _gapOther)
    }
}