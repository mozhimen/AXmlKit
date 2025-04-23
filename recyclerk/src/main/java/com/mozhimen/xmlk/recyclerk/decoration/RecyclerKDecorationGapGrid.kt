package com.mozhimen.xmlk.recyclerk.decoration

import android.graphics.Rect
import android.view.View
import androidx.recyclerview.widget.RecyclerView
import com.mozhimen.kotlin.utilk.androidx.recyclerview.UtilKRecyclerViewWrapper
import com.mozhimen.xmlk.recyclerk.decoration.bases.BaseRecyclerKDecoration

/**
 * @ClassName RecyclerDecorationGap2
 * @Description TODO
 * @Author Mozhimen & Kolin Zhao
 * @Version 1.0
 */
class RecyclerKDecorationGapGrid : BaseRecyclerKDecoration {

    constructor(gapInner: Int) : this(gapInner, gapInner)

    constructor(gapInnerHorizontal: Int, gapInnerVertical: Int) {
        _gapInnerHorizontal = gapInnerHorizontal
        _gapInnerVertical = gapInnerVertical
    }

    ////////////////////////////////////////////////////////////////

    private var _gapInnerHorizontal: Int = 0
    private var _gapInnerVertical: Int = 0

    ////////////////////////////////////////////////////////////////

    override fun getItemOffsets(outRect: Rect, view: View, parent: RecyclerView, state: RecyclerView.State) {
        super.getItemOffsets(outRect, view, parent, state)
        UtilKRecyclerViewWrapper.equilibriumAssignment_ofGridLayoutManager(parent, view, outRect, _gapInnerHorizontal, _gapInnerVertical)
    }
}