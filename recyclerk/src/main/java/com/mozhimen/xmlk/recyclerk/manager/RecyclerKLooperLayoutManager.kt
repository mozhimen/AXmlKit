package com.mozhimen.xmlk.recyclerk.manager

import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.RecyclerView.Recycler
import com.mozhimen.basick.utilk.commons.IUtilK

/**
 * @ClassName RecyclerKLooperLayoutManager
 * @Description TODO
 * @Author Mozhimen & Kolin Zhao
 * @Date 2024/4/29
 * @Version 1.0
 */
class RecyclerKLooperLayoutManager : RecyclerView.LayoutManager, IUtilK {

    @RecyclerView.Orientation
    private var _orientation: Int = RecyclerView.VERTICAL

    //////////////////////////////////////////////////////////////////////

    constructor(@RecyclerView.Orientation orientation: Int) : super() {
        _orientation = orientation
    }

    //////////////////////////////////////////////////////////////////////

    override fun generateDefaultLayoutParams(): RecyclerView.LayoutParams {
        return RecyclerView.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT)
    }

    override fun canScrollHorizontally(): Boolean {
        return _orientation == RecyclerView.HORIZONTAL
    }

    override fun canScrollVertically(): Boolean {
        return _orientation == RecyclerView.VERTICAL
    }

    override fun onLayoutChildren(recycler: RecyclerView.Recycler, state: RecyclerView.State) {
        if (getItemCount() <= 0) {
            return
        }

        //标注1.如果当前时准备状态，直接返回
        if (state.isPreLayout()) {
            return
        }

        //标注2.将视图分离放入scrap缓存中，以准备重新对view进行排版
        detachAndScrapAttachedViews(recycler)
        layoutChunk(recycler)
    }

    private fun layoutChunk(recycler: Recycler) {
        if (_orientation == RecyclerView.HORIZONTAL) {
            var tempLeft = paddingLeft
            for (i in 0 until getItemCount()) {
                if (tempLeft > width - paddingRight)
                    break

                val itemView = recycler.getViewForPosition(i % getItemCount())//标注3.初始化，将在屏幕内的view填充//标注4.测量itemView的宽高
                addView(itemView)

                measureChildWithMargins(itemView, 0, 0)

                val top = paddingTop
                val right = getDecoratedMeasuredWidth(itemView) + tempLeft
                val bottom = getDecoratedMeasuredHeight(itemView) + top
                layoutDecorated(itemView, tempLeft, top, right, bottom)//标注5.根据itemView的宽高进行布局

                tempLeft = right
            }
        } else {
            var tempTop = paddingTop
            for (i in 0 until itemCount) {
                if (tempTop > height - paddingBottom)
                    break

                val itemView = recycler.getViewForPosition(i % itemCount)
                addView(itemView)

                measureChildWithMargins(itemView, 0, 0)

                val left = paddingLeft
                val bottom: Int = tempTop + getDecoratedMeasuredHeight(itemView)
                val right = left + getDecoratedMeasuredWidth(itemView)
                layoutDecorated(itemView, left, tempTop, right, bottom)

                tempTop = bottom
            }
        }
    }

    override fun isAutoMeasureEnabled(): Boolean {
        return true
    }

    override fun scrollHorizontallyBy(dx: Int, recycler: RecyclerView.Recycler, state: RecyclerView.State): Int {
        fill_Horizontal(recycler, dx)//标注1.横向滑动的时候，对左右两边按顺序填充itemView
        offsetChildrenHorizontal(-dx)//2.滑动
        recyclerChildView(recycler, dx) //3.回收已经不可见的itemView
        return dx
    }

    override fun scrollVerticallyBy(dy: Int, recycler: Recycler, state: RecyclerView.State): Int {
        fill_Vertical(recycler, dy)
        offsetChildrenVertical(-dy)
        recyclerChildView(recycler, dy)
        return dy
    }

    /**
     * 左右滑动的时候，填充
     */
    private fun fill_Horizontal(recycler: RecyclerView.Recycler, dx: Int) {
        if (childCount == 0) return
        if (dx > 0) {
            //填充尾部
            var anchorView = getChildAt(childCount - 1) ?: return
            val anchorPosition = getPosition(anchorView)

            while (anchorView.right < width - paddingRight) {
                var position = (anchorPosition + 1) % itemCount
                if (position < 0) position += itemCount

                val scrapItem = recycler.getViewForPosition(position)
                addView(scrapItem)
                measureChildWithMargins(scrapItem, 0, 0)
                val left = anchorView.right
                val top = paddingTop
                val right = left + getDecoratedMeasuredWidth(scrapItem)
                val bottom = top + getDecoratedMeasuredHeight(scrapItem)
                layoutDecorated(scrapItem, left, top, right, bottom)

                anchorView = scrapItem
            }
        } else {
            //填充首部
            var anchorView = getChildAt(0) ?: return
            val anchorPosition = getPosition(anchorView)

            while (anchorView.left > paddingLeft) {
                var position = (anchorPosition - 1) % itemCount
                if (position < 0) position += itemCount

                val scrapItem = recycler.getViewForPosition(position)
                addView(scrapItem, 0)
                measureChildWithMargins(scrapItem, 0, 0)
                val right = anchorView.left
                val top = paddingTop
                val left = right - getDecoratedMeasuredWidth(scrapItem)
                val bottom = top + getDecoratedMeasuredHeight(scrapItem)
                layoutDecorated(scrapItem, left, top, right, bottom)

                anchorView = scrapItem
            }
        }
    }

    private fun fill_Vertical(recycler: Recycler, dy: Int) {
        if (childCount == 0) return
        if (dy > 0) {
            //填充尾部
            var anchorView = getChildAt(childCount - 1) ?: return
            val anchorPosition = getPosition(anchorView)

            while (anchorView.bottom < height - paddingBottom) {
                var position = (anchorPosition + 1) % itemCount
                if (position < 0) position += itemCount

                val scrapItem = recycler.getViewForPosition(position)
                addView(scrapItem)
                measureChildWithMargins(scrapItem, 0, 0)
                val left = paddingLeft
                val top = anchorView.bottom
                val right = left + getDecoratedMeasuredWidth(scrapItem)
                val bottom = top + getDecoratedMeasuredHeight(scrapItem)
                layoutDecorated(scrapItem, left, top, right, bottom)
                anchorView = scrapItem
            }
        } else {
            //填充首部
            var anchorView = getChildAt(0) ?: return
            val anchorPosition = getPosition(anchorView)
            while (anchorView.top > paddingTop) {
                var position = (anchorPosition - 1) % itemCount
                if (position < 0) position += itemCount

                val scrapItem = recycler.getViewForPosition(position)
                addView(scrapItem, 0)
                measureChildWithMargins(scrapItem, 0, 0)
                val left = paddingLeft
                val right = left + getDecoratedMeasuredWidth(scrapItem)
                val bottom = anchorView.top
                val top = bottom - getDecoratedMeasuredHeight(scrapItem)
                layoutDecorated(
                    scrapItem, left, top,
                    right, bottom
                )
                anchorView = scrapItem
            }
        }
    }

    /**
     * 回收界面不可见的view
     */
    private fun recyclerChildView(recycler: Recycler, dx_dy: Int) {
        if (dx_dy > 0) {//回收头部
            for (i in 0 until childCount) {
                val view: View = getChildAt(i) ?: return
                val isRecycler =
                    if (_orientation == RecyclerView.HORIZONTAL)
                        view.right < paddingLeft
                    else
                        view.bottom < paddingTop
                if (isRecycler)
                    removeAndRecycleView(view, recycler)
                else
                    return
            }
        } else {//回收尾部
            for (i in (0 until childCount).reversed()) {
                val view = getChildAt(i) ?: return
                val isRecycler =
                    if (_orientation == RecyclerView.HORIZONTAL)
                        view.left > width - paddingRight
                    else
                        view.top > height - paddingBottom
                if (isRecycler)
                    removeAndRecycleView(view, recycler)
                else
                    return
            }
        }
    }
}