package com.mozhimen.layoutk.marquee

import android.content.Context
import android.content.res.TypedArray
import android.util.AttributeSet
import android.view.Gravity
import android.view.View
import android.view.animation.Animation
import android.view.animation.AnimationUtils
import android.widget.LinearLayout
import android.widget.ViewFlipper
import androidx.annotation.IntRange
import com.mozhimen.kotlin.elemk.android.view.commons.IAnimation_AnimationListener
import com.mozhimen.kotlin.elemk.android.view.cons.CGravity
import com.mozhimen.kotlin.lintk.optins.OApiCall_BindLifecycle
import com.mozhimen.kotlin.lintk.optins.OApiCall_BindViewLifecycle
import com.mozhimen.kotlin.lintk.optins.OApiInit_ByLazy
import com.mozhimen.kotlin.utilk.android.animation.applyAnimateTranslationYToTopHide
import com.mozhimen.kotlin.utilk.android.util.UtilKLogWrapper
import com.mozhimen.kotlin.utilk.android.widget.UtilKFrameLayout_LayoutParamsGet
import com.mozhimen.kotlin.utilk.kotlin.ranges.constraint
import com.mozhimen.layoutk.marquee.bases.BaseMarqueeAdapter
import com.mozhimen.layoutk.marquee.commons.ILayoutKMarqueeAnimListener
import com.mozhimen.xmlk.basic.commons.ILayoutK
import java.util.LinkedList

/**
 * @ClassName LayoutKMarqueeVertical
 * @Description TODO
 * @Author mozhimen
 * @Date 2024/12/17
 * @Version 1.0
 */
@OptIn(OApiInit_ByLazy::class, OApiCall_BindLifecycle::class, OApiCall_BindViewLifecycle::class)
class LayoutKMarqueeText @JvmOverloads constructor(context: Context, attrs: AttributeSet? = null) :
    ViewFlipper(context, attrs), ILayoutK, BaseMarqueeAdapter.OnDataChangedListener, ILayoutKMarqueeAnimListener {
    private var _interval = 3000//轮播间隔
    private var _animDuration = 1000//动画时间
    private var _itemCountPerLine = 1//一次性显示item数目
    private var _marqueeAdapter: BaseMarqueeAdapter<*>? = null

    //    private var _animRunning = false
    private var _viewQueue = LinkedList<View>()

    /**
     * 顺序
     * onItemAnimOutStart: index 0
     * onItemAnimInStart: index 1
     * onItemAnimOutEnd: index 0
     * onItemAnimInEnd: index 1
     */
    private var _animInListener: Animation.AnimationListener = object : IAnimation_AnimationListener {
        override fun onAnimationStart(animation: Animation?) {
            onItemAnimInStart(displayedChild, currentView)
        }

        override fun onAnimationEnd(animation: Animation?) {
            onItemAnimInEnd(displayedChild, currentView)
        }
    }

    private var _animOutListener: Animation.AnimationListener = object : IAnimation_AnimationListener {
        override fun onAnimationStart(animation: Animation?) {
            val index = getDisplayedChildIndex(displayedChild - 1)
            onItemAnimOutStart(index, getChildAt(index))
        }

        override fun onAnimationEnd(animation: Animation?) {
            val index = getDisplayedChildIndex(displayedChild - 1)
            onItemAnimOutEnd(index, getChildAt(index))
        }
    }

    private fun getDisplayedChildIndex(displayedChild: Int): Int =
        if (displayedChild >= childCount)
            0
        else if (displayedChild < 0) {
            (childCount - 1).constraint(0, childCount)
        } else
            displayedChild

    //    private var _layoutKMarqueeAnimListener: ILayoutKMarqueeAnimListener? = null

    //////////////////////////////////////////////////////////////////////////////////////////////

    init {
        initAttrs(attrs)
        initView()
    }

    //////////////////////////////////////////////////////////////////////////////////////////////

    fun setMarqueeAdapter(adapter: BaseMarqueeAdapter<*>) {
        _marqueeAdapter = adapter
        _marqueeAdapter!!.setOnDataChangedListener(this)
    }

    fun setItemCountPerLine(@IntRange(from = 1) itemCount: Int) {
        _itemCountPerLine = itemCount
    }

    //////////////////////////////////////////////////////////////////////////////////////////////

    override fun onItemAnimOutStart(index: Int, view: View?) {
//        UtilKLogWrapper.d(TAG, "onItemAnimOutStart: ------------------------>")
//        UtilKLogWrapper.d(TAG, "onItemAnimOutStart: index $index")
    }

    override fun onItemAnimInStart(index: Int, view: View?) {
//        UtilKLogWrapper.d(TAG, "onItemAnimInStart: index $index")
    }

    override fun onItemAnimOutEnd(index: Int, view: View?) {
//        UtilKLogWrapper.d(TAG, "onItemAnimOutEnd: index $index")
    }

    override fun onItemAnimInEnd(index: Int, view: View?) {
//        UtilKLogWrapper.d(TAG, "onItemAnimInEnd: index $index")
        if (index == childCount - 1 && _viewQueue.isNotEmpty()) {//最后一个显示了
            view?.applyAnimateTranslationYToTopHide(_animDuration.toLong(), -(height / 2f + view.height), onStart = null) {
                removeAllViews()
            }
            for (i in _viewQueue.indices) {
                addView(_viewQueue.poll(), childCount, UtilKFrameLayout_LayoutParamsGet.get_MATCH_WRAP().apply { gravity = CGravity.CENTER })
            }
        }
    }

    override fun initAttrs(attrs: AttributeSet?) {
        attrs ?: return
        var typedArray: TypedArray? = null
        try {
            typedArray = context.obtainStyledAttributes(attrs, R.styleable.LayoutKMarqueeText)
            _interval =
                typedArray.getInt(R.styleable.LayoutKMarqueeText_layoutKMarqueeText_interval, _interval)
            _animDuration =
                typedArray.getInt(R.styleable.LayoutKMarqueeText_layoutKMarqueeText_animDuration, _animDuration)
            _itemCountPerLine =
                typedArray.getInt(R.styleable.LayoutKMarqueeText_layoutKMarqueeText_itemCountPerLine, _itemCountPerLine)
        } finally {
            typedArray?.recycle()
        }
    }

    override fun initView() {
        // 动画
        val animIn = AnimationUtils.loadAnimation(context, R.anim.anim_marquee_in).apply {
            duration = _animDuration.toLong()
            setAnimationListener(_animInListener)
        }
        val animOut = AnimationUtils.loadAnimation(context, R.anim.anim_marquee_out).apply {
            duration = _animDuration.toLong()
            setAnimationListener(_animOutListener)
        }
        this.inAnimation = animIn// 设置切换View的进入动画
        this.outAnimation = animOut// 设置切换View的退出动画
        this.flipInterval = _interval// 设置View之间切换的时间间隔
        this.measureAllChildren = false// 设置在测量时是考虑所有子项，还是只考虑可见或不可见状态的子项。
    }

    override fun onDataChanged() {
        if (childCount == 0) {
            addDatas()
        } else if (_viewQueue.isEmpty()) {
            resetDatas()
        } /*else {
            _viewQueue.clear()
            resetDatas()
        }*/
    }

    override fun onVisibilityChanged(changedView: View, visibility: Int) {
        super.onVisibilityChanged(changedView, visibility)
        if (visibility == VISIBLE) {
            startFlipping()
        } else if (visibility == GONE || visibility == INVISIBLE) {
            stopFlipping()
        }
    }

    override fun onAttachedToWindow() {
        super.onAttachedToWindow()
        startFlipping()
    }

    override fun onDetachedFromWindow() {
        super.onDetachedFromWindow()
        stopFlipping()
    }


    //////////////////////////////////////////////////////////////////////////////////////////////

    private fun resetDatas() {
        _marqueeAdapter ?: return
        val marqueeAdapter = _marqueeAdapter!!
        var currentIndex = 0
        // 计算数据展示完毕需要几页，根据总条目%每页条目计算得出
        val loopCount: Int = if (marqueeAdapter.getItemCount() % _itemCountPerLine == 0) marqueeAdapter.getItemCount() / _itemCountPerLine else marqueeAdapter.getItemCount() / _itemCountPerLine + 1
        // 遍历动态添加每页的View
        for (i in 0 until loopCount) {
            if (_itemCountPerLine <= 1) {
                val view: View = marqueeAdapter.onCreateView(this)
                if (currentIndex < marqueeAdapter.getItemCount()) { // 绑定View
                    marqueeAdapter.onBindView(view, currentIndex)
                }
                currentIndex += 1
                _viewQueue.add(view)
            } else {
                val parentView = LinearLayout(context).apply {
                    orientation = LinearLayout.VERTICAL
                    gravity = Gravity.CENTER
                }
                // 每页显示多少条，就遍历添加几个子View
                for (j in 0 until _itemCountPerLine) {
                    currentIndex = getRealPosition(j, currentIndex)
                    if (currentIndex in 0 until marqueeAdapter.getItemCount()) {
                        val view: View = marqueeAdapter.onCreateView(this)
                        parentView.addView(view)
                        if (currentIndex < marqueeAdapter.getItemCount()) {
                            marqueeAdapter.onBindView(view, currentIndex)
                        }
                    }
                }
                _viewQueue.add(parentView)
            }
        }
    }

    private fun addDatas() {
        _marqueeAdapter ?: return
        val marqueeAdapter = _marqueeAdapter!!
        removeAllViews()
        var currentIndex = 0
        // 计算数据展示完毕需要几页，根据总条目%每页条目计算得出
        val loopCount: Int = if (marqueeAdapter.getItemCount() % _itemCountPerLine == 0) marqueeAdapter.getItemCount() / _itemCountPerLine else marqueeAdapter.getItemCount() / _itemCountPerLine + 1
        // 遍历动态添加每页的View
        for (i in 0 until loopCount) {
            if (_itemCountPerLine <= 1) {
                val view: View = marqueeAdapter.onCreateView(this)
                if (currentIndex < marqueeAdapter.getItemCount()) { // 绑定View
                    marqueeAdapter.onBindView(view, currentIndex)
                }
                currentIndex += 1
                addView(view, childCount, UtilKFrameLayout_LayoutParamsGet.get_MATCH_WRAP().apply { gravity = CGravity.CENTER })
            } else {
                val parentView = LinearLayout(context).apply {
                    orientation = LinearLayout.VERTICAL
                    gravity = Gravity.CENTER
                }
                // 每页显示多少条，就遍历添加几个子View
                for (j in 0 until _itemCountPerLine) {
                    currentIndex = getRealPosition(j, currentIndex)
                    if (currentIndex in 0 until marqueeAdapter.getItemCount()) {
                        val view: View = marqueeAdapter.onCreateView(this)
                        parentView.addView(view)
                        if (currentIndex < marqueeAdapter.getItemCount()) {
                            marqueeAdapter.onBindView(view, currentIndex)
                        }
                    }
                }
                addView(parentView, childCount, UtilKFrameLayout_LayoutParamsGet.get_MATCH_WRAP().apply { gravity = CGravity.CENTER })
            }
        }
    }

    private fun getRealPosition(index: Int, currentIndex: Int): Int =
        if ((index == 0 && currentIndex == 0) /*|| (currentIndex == adapter.getItemCount() - 1)*/) {
            0
        } else {
            currentIndex + 1
        }
}
