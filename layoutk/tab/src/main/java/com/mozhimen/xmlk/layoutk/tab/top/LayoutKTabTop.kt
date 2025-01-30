package com.mozhimen.xmlk.layoutk.tab.top

import android.content.Context
import android.graphics.Rect
import android.util.AttributeSet
import android.view.ViewGroup
import android.widget.HorizontalScrollView
import android.widget.LinearLayout
import androidx.annotation.Px
import com.mozhimen.kotlin.lintk.optins.permission.OPermission_INTERNET
import com.mozhimen.kotlin.utilk.android.util.dp2px
import com.mozhimen.kotlin.utilk.wrapper.UtilKScreen
import com.mozhimen.xmlk.layoutk.tab.commons.ILayoutKTab
import com.mozhimen.xmlk.layoutk.tab.commons.ITabSelectedListener
import com.mozhimen.xmlk.layoutk.tab.top.mos.MTabTop
import kotlin.math.abs

/**
 * @ClassName TabTopLayout
 * @Description TODO
 * @Author mozhimen / Kolin Zhao
 * @Date 2022/4/15 23:36
 * @Version 1.0
 */
@OPermission_INTERNET
class LayoutKTabTop @JvmOverloads constructor(context: Context, attrs: AttributeSet? = null, defStyleAttr: Int = 0) : HorizontalScrollView(context, attrs, defStyleAttr),
    ILayoutKTab<TabTopItem, MTabTop> {

    private val _tabSelectedChangeListeners: ArrayList<ITabSelectedListener<MTabTop>> = ArrayList()
    private var _selectedMo: MTabTop? = null
    private var _itemList: List<MTabTop>? = null
    private var _tabTopWidth: Int = 0
    private var _tabTopHeight = 40f.dp2px()//TabBottom高度

    init {
        isVerticalScrollBarEnabled = false
    }

    /**
     * 设置顶部高度 px ,在inflate之前有效
     * @param tabHeight Int
     */
    fun setTabTopHeight(@Px tabHeight: Float) {
        _tabTopHeight = tabHeight
    }

    /**
     * 设置背景颜色
     * @param color Int
     */
    fun setTabTopBackground(color: Int) {
        this.setBackgroundColor(color)
    }

    override fun findTabItem(item: MTabTop): TabTopItem? {
        val rootView: ViewGroup = getRootLayout(false)
        for (i in 0 until rootView.childCount) {
            val child = rootView.getChildAt(i)
            if (child is TabTopItem) {
                if (child.getTabInfo() == item) {
                    return child
                }
            }
        }
        return null
    }

    override fun addTabItemSelectedListener(listener: ITabSelectedListener<MTabTop>) {
        _tabSelectedChangeListeners.add(listener)
    }

    override fun defaultSelected(defaultItem: MTabTop) {
        onSelected(defaultItem)
    }

    override fun inflateTabItem(itemList: List<MTabTop>) {
        if (itemList.isEmpty()) {
            return
        }
        _itemList = itemList
        val container = getRootLayout(true)
        _selectedMo = null
        //清除之前添加的TabTop listener,Tips: Java foreach remove问题
        val iterator: MutableIterator<ITabSelectedListener<MTabTop>> =
            _tabSelectedChangeListeners.iterator()
        while (iterator.hasNext()) {
            if (iterator.next() is TabTopItem) {
                iterator.remove()
            }
        }
        for (i in itemList.indices) {
            val info = itemList[i]
            val tab = TabTopItem(context)
            _tabSelectedChangeListeners.add(tab)
            tab.setTabItem(info)
            container.addView(tab)
            tab.setOnClickListener { onSelected(info) }
        }
    }

    @Throws(Exception::class)
    private fun onSelected(nextMo: MTabTop) {
        requireNotNull(_itemList) { "$TAG _itemList must not be null!" }
        for (listener in _tabSelectedChangeListeners) {
            listener.onTabItemSelected(_itemList!!.indexOf(nextMo), _selectedMo, nextMo)
        }
        _selectedMo = nextMo
        autoScroll(nextMo)
    }

    /**
     * 自动滚动,实现点击的位置能够自动滚动以展示前后2个
     * @param nextMo MTabTop
     */
    private fun autoScroll(nextMo: MTabTop) {
        val tabTop = findTabItem(nextMo) ?: return
        val index: Int = _itemList!!.indexOf(nextMo)
        val location = IntArray(2)
        //获取点击控件在屏幕的位置
        tabTop.getLocationInWindow(location)
        if (_tabTopWidth == 0) {
            _tabTopWidth = tabTop.width
        }
        //判断点击了屏幕左侧还是右侧
        val scrollWidth: Int = if ((location[0] + _tabTopWidth / 2) > UtilKScreen.getWidth() / 2) {
            rangeScrollWidth(index, 2)
        } else {
            rangeScrollWidth(index, -2)
        }
        smoothScrollTo(scrollX + scrollWidth, 0)
    }

    /**
     * 获取可滚动的范围
     * @param index Int 从第几个开始
     * @param range Int 向前后的范围
     * @return Int
     */
    private fun rangeScrollWidth(index: Int, range: Int): Int {
        var scrollWidth = 0
        for (i in 0..abs(range)) {
            val next: Int = if (range < 0) {
                range + i + index
            } else {
                range - i + index
            }
            if (next >= 0 && next < _itemList!!.size) {
                if (range < 0) {
                    scrollWidth -= scrollWidth(next, false)
                } else {
                    scrollWidth += scrollWidth(next, true)
                }
            }
        }
        return scrollWidth
    }

    /**
     * 指定位置的控件可滚动的距离
     * @param index Int 指定位置的控件
     * @param toRight Boolean 是否是点击了屏幕右侧
     * @return Int 可滚动的距离
     */
    private fun scrollWidth(index: Int, toRight: Boolean): Int {
        val target = findTabItem(_itemList!![index]) ?: return 0
        val rect = Rect()
        target.getLocalVisibleRect(rect)
        return if (toRight) { //点击了屏幕右侧
            if (rect.right > _tabTopWidth) { //right坐标大于控件的宽度时,说明完全没有显示
                _tabTopWidth
            } else { //显示部分,减去已显示的宽度
                _tabTopWidth - rect.right
            }
        } else {
            if (rect.left <= -_tabTopWidth) { //left坐标小于等于-控件的宽度,说明完全没有显示
                return _tabTopWidth
            } else if (rect.left > 0) { //显示部分
                return rect.left
            }
            0
        }
    }

    private fun getRootLayout(clear: Boolean): LinearLayout {
        var rootView: LinearLayout? = getChildAt(0) as LinearLayout?
        if (rootView == null) {
            rootView = LinearLayout(context)
            rootView.orientation = LinearLayout.HORIZONTAL
            val params = LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.MATCH_PARENT,
                _tabTopHeight.toInt()
            )
            addView(rootView, params)
        } else if (clear) {
            rootView.removeAllViews()
        }
        return rootView
    }
}