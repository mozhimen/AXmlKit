package com.mozhimen.xmlk.recyclerk.manager

import android.content.Context
import androidx.recyclerview.widget.RecyclerView
import kotlin.math.max
import kotlin.math.roundToInt

/**
 * @ClassName RecyclerKDynamicGridLayoutManager
 * @Description TODO
 * @Author Mozhimen & Kolin Zhao
 * @Date 2024/5/10
 * @Version 1.0
 */
class RecyclerKDynamicGridLayoutManager @JvmOverloads constructor(context: Context,
                                                                  private val scaling: Int = 2) : RecyclerKCatchGridLayoutManager(context, 1, VERTICAL, false) {

    //////////////////////////////////////////////////////////////

    private val density: Float = context.resources.displayMetrics.density

    override fun onLayoutChildren(recycler: RecyclerView.Recycler, state: RecyclerView.State) {
        updateSpanCount(width)
        super.onLayoutChildren(recycler, state)
    }

    private fun columnsForWidth(widthPx: Int): Int {
        val widthDp = dpFromPx(widthPx.toFloat())
        val columns = when {
            widthDp >= 840 -> 12
            widthDp >= 600 -> 8
            else -> 4
        }
        return max(columns / scaling, 1)
    }

    private fun dpFromPx(px: Float): Int {
        return (px / density).roundToInt()
    }

    private fun updateSpanCount(width: Int) {
        spanCount = columnsForWidth(width)
    }
}