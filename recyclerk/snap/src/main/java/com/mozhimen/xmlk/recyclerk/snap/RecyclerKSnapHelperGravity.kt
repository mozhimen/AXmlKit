package com.mozhimen.xmlk.recyclerk.snap

import android.util.DisplayMetrics
import android.view.Gravity
import android.view.View
import android.view.animation.DecelerateInterpolator
import android.widget.Scroller
import androidx.annotation.Px
import androidx.core.text.TextUtilsCompat
import androidx.core.view.ViewCompat
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.LinearSmoothScroller
import androidx.recyclerview.widget.LinearSnapHelper
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.OrientationHelper
import java.util.Locale
import kotlin.math.abs
import kotlin.math.max

/**
 * @ClassName RecyclerKSnapHelperGravity
 * @Description TODO
 * @Author Mozhimen / Kolin Zhao
 * @Date 2024/9/8 19:54
 * @Version 1.0
 */
/**
 * A {@link LinearSnapHelper} that allows snapping to an edge or to the center.
 * <p>
 * Possible snap positions:
 * {@link Gravity#START}, {@link Gravity#TOP}, {@link Gravity#END}, {@link Gravity#BOTTOM},
 * {@link Gravity#CENTER}.
 * <p>
 * To customize the scroll duration, use {@link GravityLinearSnapHelper#setScrollMsPerInch(float)}.
 * <p>
 * To customize the maximum scroll distance during flings,
 * use {@link GravityLinearSnapHelper#setMaxFlingSizeFraction(float)}
 * or {@link GravityLinearSnapHelper#setMaxFlingDistance(int)}
 */
open class RecyclerKSnapHelperGravity : LinearSnapHelper {

    /**
     * A listener that's called when the [RecyclerView] used by [GravityLinearSnapHelper]
     * changes its scroll state to [RecyclerView.SCROLL_STATE_IDLE]
     * and there's a valid snap position.
     */
    interface SnapListener {
        /**
         * @param position last position snapped to
         */
        fun onSnap(position: Int)
    }

    companion object {
        const val FLING_DISTANCE_DISABLE: Int = -1
        const val FLING_SIZE_FRACTION_DISABLE: Float = -1f
    }

    private var _gravity: Int
    private var _enableSnapLastItem: Boolean = false
    private var _snapListener: SnapListener? = null

    private var _recyclerView: RecyclerView? = null
    private var _isRtl = false
    private var _nextSnapPosition = 0

    private var _snapToPadding = false
    private var _scrollMsPerInch = 100f
    private var _maxFlingDistance = FLING_DISTANCE_DISABLE
    private var _maxFlingSizeFraction = FLING_SIZE_FRACTION_DISABLE
    private val _onScrollListener: RecyclerView.OnScrollListener = object : RecyclerView.OnScrollListener() {
        override fun onScrollStateChanged(recyclerView: RecyclerView, newState: Int) {
            super.onScrollStateChanged(recyclerView, newState)
            this@RecyclerKSnapHelperGravity.onScrollStateChanged(newState)
        }
    }

    private var _isScrolling = false
    private var verticalHelper: OrientationHelper? = null
    private var horizontalHelper: OrientationHelper? = null

    ///////////////////////////////////////////////////////////////////

    constructor(gravity: Int) : this(gravity, false, null)

    constructor(gravity: Int, snapListener: SnapListener?) : this(gravity, false, snapListener)

    constructor(gravity: Int, enableSnapLastItem: Boolean) : this(gravity, enableSnapLastItem, null)

    constructor(gravity: Int, enableSnapLastItem: Boolean, snapListener: SnapListener?) {
        require(!(gravity != Gravity.START && gravity != Gravity.END && gravity != Gravity.BOTTOM && gravity != Gravity.TOP && gravity != Gravity.CENTER)) {
            "Invalid gravity value. Use START " +
                    "| END | BOTTOM | TOP | CENTER constants"
        }
        _enableSnapLastItem = enableSnapLastItem
        _gravity = gravity
        _snapListener = snapListener
    }

    ///////////////////////////////////////////////////////////////////

    override fun attachToRecyclerView(recyclerView: RecyclerView?) {
        _recyclerView?.removeOnScrollListener(_onScrollListener)
        if (recyclerView != null) {
            recyclerView.onFlingListener = null
            if (_gravity == Gravity.START || _gravity == Gravity.END) {
                _isRtl = (TextUtilsCompat.getLayoutDirectionFromLocale(Locale.getDefault()) == ViewCompat.LAYOUT_DIRECTION_RTL)
            }
            recyclerView.addOnScrollListener(_onScrollListener)
            _recyclerView = recyclerView
        } else {
            _recyclerView = null
        }
        super.attachToRecyclerView(recyclerView)
    }

    ///////////////////////////////////////////////////////////////////

    override fun findSnapView(lm: RecyclerView.LayoutManager): View? {
        return findSnapView(lm, true)
    }

    override fun calculateDistanceToFinalSnap(layoutManager: RecyclerView.LayoutManager, targetView: View): IntArray {
        if (_gravity == Gravity.CENTER) {
            return super.calculateDistanceToFinalSnap(layoutManager, targetView)!!
        }

        val out = IntArray(2)
        if (layoutManager !is LinearLayoutManager) {
            return out
        }

        val lm: LinearLayoutManager = layoutManager
        if (lm.canScrollHorizontally()) {
            if ((_isRtl && _gravity == Gravity.END) || (!_isRtl && _gravity == Gravity.START)) {
                out[0] = getDistanceToStart(targetView, getHorizontalHelper(lm))
            } else {
                out[0] = getDistanceToEnd(targetView, getHorizontalHelper(lm))
            }
        } else if (lm.canScrollVertically()) {
            if (_gravity == Gravity.TOP) {
                out[1] = getDistanceToStart(targetView, getVerticalHelper(lm))
            } else {
                out[1] = getDistanceToEnd(targetView, getVerticalHelper(lm))
            }
        }
        return out
    }

    override fun calculateScrollDistance(velocityX: Int, velocityY: Int): IntArray {
        if (_recyclerView == null || (verticalHelper == null && horizontalHelper == null)
            || (_maxFlingDistance == RecyclerKSnapHelperGravity.FLING_DISTANCE_DISABLE
                    && _maxFlingSizeFraction == RecyclerKSnapHelperGravity.FLING_SIZE_FRACTION_DISABLE)
        ) {
            return super.calculateScrollDistance(velocityX, velocityY)
        }
        val out = IntArray(2)
        val scroller = Scroller(
            _recyclerView!!.context,
            DecelerateInterpolator()
        )
        val maxDistance: Int = getFlingDistance()
        scroller.fling(
            0, 0, velocityX, velocityY,
            -maxDistance, maxDistance,
            -maxDistance, maxDistance
        )
        out[0] = scroller.finalX
        out[1] = scroller.finalY
        return out
    }

    override fun createScroller(layoutManager: RecyclerView.LayoutManager): RecyclerView.SmoothScroller? {
        if (layoutManager !is RecyclerView.SmoothScroller.ScrollVectorProvider
            || _recyclerView == null
        ) {
            return null
        }
        return object : LinearSmoothScroller(_recyclerView!!.context) {
            override fun onTargetFound(
                targetView: View,
                state: RecyclerView.State,
                action: Action
            ) {
                if (_recyclerView == null || _recyclerView!!.getLayoutManager() == null) {
                    // The associated RecyclerView has been removed so there is no action to take.
                    return
                }
                val snapDistances = calculateDistanceToFinalSnap(
                    _recyclerView!!.getLayoutManager()!!,
                    targetView
                )
                val dx = snapDistances[0]
                val dy = snapDistances[1]
                val time = calculateTimeForDeceleration(max(abs(dx.toDouble()), abs(dy.toDouble())).toInt())
                if (time > 0) {
                    action.update(dx, dy, time, mDecelerateInterpolator)
                }
            }

            override fun calculateSpeedPerPixel(displayMetrics: DisplayMetrics): Float {
                return _scrollMsPerInch / displayMetrics.densityDpi
            }
        }
    }
    ///////////////////////////////////////////////////////////////////

    /**
     * Sets a [SnapListener] to listen for snap events
     *
     * @param listener a [SnapListener] that'll receive snap events or null to clear it
     */
    fun setSnapListener(listener: SnapListener?) {
        _snapListener = listener
    }

    /**
     * Changes the gravity of this [GravityLinearSnapHelper]
     * and dispatches a smooth scroll for the new snap position.
     *
     * @param newGravity one of the following: [Gravity.START], [Gravity.TOP],
     * [Gravity.END], [Gravity.BOTTOM], [Gravity.CENTER]
     * @param smooth     true if we should smooth scroll to new edge, false otherwise
     */
    fun setGravity(newGravity: Int, smooth: Boolean) {
        if (this._gravity != newGravity) {
            this._gravity = newGravity
            updateSnap(smooth, false)
        }
    }

    /**
     * Changes the gravity of this [GravityLinearSnapHelper]
     * and dispatches a smooth scroll for the new snap position.
     *
     * @param newGravity one of the following: [Gravity.START], [Gravity.TOP],
     * [Gravity.END], [Gravity.BOTTOM], [Gravity.CENTER]
     */
    fun setGravity(newGravity: Int) {
        setGravity(newGravity, true)
    }

    /**
     * Get the current gravity being applied
     *
     * @return one of the following: [Gravity.START], [Gravity.TOP], [Gravity.END],
     * [Gravity.BOTTOM], [Gravity.CENTER]
     */
    fun getGravity(): Int {
        return this._gravity
    }

    /**
     * @return true if this SnapHelper should snap to the last item
     */
    fun getEnableSnapLastItem(): Boolean {
        return _enableSnapLastItem
    }

    /**
     * Enable snapping of the last item that's snappable.
     * The default value is false, because you can't see the last item completely
     * if this is enabled.
     *
     * @param snap true if you want to enable snapping of the last snappable item
     */
    fun setEnableSnapLastItem(snap: Boolean) {
        _enableSnapLastItem = snap
    }

    /**
     * @return last distance set through [GravityLinearSnapHelper.setMaxFlingDistance]
     * or [GravityLinearSnapHelper.FLING_DISTANCE_DISABLE] if we're not limiting the fling distance
     */
    fun getMaxFlingDistance(): Int {
        return _maxFlingDistance
    }

    /**
     * Changes the max fling distance in absolute values.
     *
     * @param distance max fling distance in pixels
     * or [GravityLinearSnapHelper.FLING_DISTANCE_DISABLE]
     * to disable fling limits
     */
    fun setMaxFlingDistance(@Px distance: Int) {
        _maxFlingDistance = distance
        _maxFlingSizeFraction = RecyclerKSnapHelperGravity.FLING_SIZE_FRACTION_DISABLE
    }

    /**
     * @return last distance set through [GravityLinearSnapHelper.setMaxFlingSizeFraction]
     * or [GravityLinearSnapHelper.FLING_SIZE_FRACTION_DISABLE]
     * if we're not limiting the fling distance
     */
    fun getMaxFlingSizeFraction(): Float {
        return _maxFlingSizeFraction
    }

    /**
     * Changes the max fling distance depending on the available size of the RecyclerView.
     *
     *
     * Example: if you pass 0.5f and the RecyclerView measures 600dp,
     * the max fling distance will be 300dp.
     *
     * @param fraction size fraction to be used for the max fling distance
     * or [GravityLinearSnapHelper.FLING_SIZE_FRACTION_DISABLE]
     * to disable fling limits
     */
    fun setMaxFlingSizeFraction(fraction: Float) {
        _maxFlingDistance = RecyclerKSnapHelperGravity.FLING_DISTANCE_DISABLE
        _maxFlingSizeFraction = fraction
    }

    /**
     * @return last scroll speed set through [GravityLinearSnapHelper.setScrollMsPerInch]
     * or 100f
     */
    fun getScrollMsPerInch(): Float {
        return _scrollMsPerInch
    }

    /**
     * Sets the scroll duration in ms per inch.
     *
     *
     * Default value is 100.0f
     *
     *
     * This value will be used in
     * [GravityLinearSnapHelper.createScroller]
     *
     * @param ms scroll duration in ms per inch
     */
    fun setScrollMsPerInch(ms: Float) {
        _scrollMsPerInch = ms
    }


    /**
     * @return true if this SnapHelper should snap to the padding. Defaults to false.
     */
    fun getSnapToPadding(): Boolean {
        return _snapToPadding
    }

    /**
     * If true, GravityLinearSnapHelper will snap to the gravity edge
     * plus any amount of padding that was set in the RecyclerView.
     *
     *
     * The default value is false.
     *
     * @param snapToPadding true if you want to snap to the padding
     */
    fun setSnapToPadding(snapToPadding: Boolean) {
        this._snapToPadding = snapToPadding
    }

    /**
     * @return the position of the current view that's snapped
     * or [RecyclerView.NO_POSITION] in case there's none.
     */
    fun getCurrentSnappedPosition(): Int {
        if (_recyclerView != null && _recyclerView!!.getLayoutManager() != null) {
            val snappedView = findSnapView(_recyclerView!!.getLayoutManager()!!)
            if (snappedView != null) {
                return _recyclerView!!.getChildAdapterPosition(snappedView)
            }
        }
        return RecyclerView.NO_POSITION
    }

    /**
     * Updates the current view to be snapped
     *
     * @param smooth          true if we should smooth scroll, false otherwise
     * @param checkEdgeOfList true if we should check if we're at an edge of the list
     * and snap according to [GravityLinearSnapHelper.getSnapLastItem],
     * or false to force snapping to the nearest view
     */
    fun updateSnap(smooth: Boolean, checkEdgeOfList: Boolean?) {
        if (_recyclerView == null || _recyclerView!!.getLayoutManager() == null) {
            return
        }
        val lm: RecyclerView.LayoutManager = _recyclerView!!.getLayoutManager()!!
        val snapView = findSnapView(lm, checkEdgeOfList!!)
        if (snapView != null) {
            val out = calculateDistanceToFinalSnap(lm, snapView)
            if (smooth) {
                _recyclerView!!.smoothScrollBy(out[0], out[1])
            } else {
                _recyclerView!!.scrollBy(out[0], out[1])
            }
        }
    }

    /**
     * This method will only work if there's a ViewHolder for the given position.
     *
     * @return true if scroll was successful, false otherwise
     */
    fun scrollToPosition(position: Int): Boolean {
        if (position == RecyclerView.NO_POSITION) {
            return false
        }
        return scrollTo(position, false)
    }

    /**
     * Unlike [GravityLinearSnapHelper.scrollToPosition],
     * this method will generally always find a snap view if the position is valid.
     *
     *
     * The smooth scroller from [GravityLinearSnapHelper.createScroller]
     * will be used, and so will [GravityLinearSnapHelper.scrollMsPerInch] for the scroll velocity
     *
     * @return true if scroll was successful, false otherwise
     */
    fun smoothScrollToPosition(position: Int): Boolean {
        if (position == RecyclerView.NO_POSITION) {
            return false
        }
        return scrollTo(position, true)
    }

    fun findSnapView(lm: RecyclerView.LayoutManager, checkEdgeOfList: Boolean): View? {
        var snapView: View? = null

        when (_gravity) {
            Gravity.START -> snapView = findView(lm, getHorizontalHelper(lm), Gravity.START, checkEdgeOfList)
            Gravity.END -> snapView = findView(lm, getHorizontalHelper(lm), Gravity.END, checkEdgeOfList)
            Gravity.TOP -> snapView = findView(lm, getVerticalHelper(lm), Gravity.START, checkEdgeOfList)
            Gravity.BOTTOM -> snapView = findView(lm, getVerticalHelper(lm), Gravity.END, checkEdgeOfList)
            Gravity.CENTER -> snapView = if (lm.canScrollHorizontally()) {
                findView(lm, getHorizontalHelper(lm), Gravity.CENTER, checkEdgeOfList)
            } else {
                findView(lm, getVerticalHelper(lm), Gravity.CENTER, checkEdgeOfList)
            }
        }
        _nextSnapPosition = if (snapView != null) {
            _recyclerView!!.getChildAdapterPosition(snapView)
        } else {
            RecyclerView.NO_POSITION
        }
        return snapView
    }

    ///////////////////////////////////////////////////////////////////

    private fun getFlingDistance(): Int {
        return if (_maxFlingSizeFraction != RecyclerKSnapHelperGravity.FLING_SIZE_FRACTION_DISABLE) {
            if (verticalHelper != null) {
                (_recyclerView!!.getHeight() * _maxFlingSizeFraction).toInt()
            } else if (horizontalHelper != null) {
                (_recyclerView!!.getWidth() * _maxFlingSizeFraction).toInt()
            } else {
                Int.MAX_VALUE
            }
        } else if (_maxFlingDistance != RecyclerKSnapHelperGravity.FLING_DISTANCE_DISABLE) {
            _maxFlingDistance
        } else {
            Int.MAX_VALUE
        }
    }

    /**
     * @return true if the scroll will snap to a view, false otherwise
     */
    private fun scrollTo(position: Int, smooth: Boolean): Boolean {
        if (_recyclerView?.getLayoutManager() != null) {
            if (smooth) {
                val smoothScroller = createScroller(_recyclerView!!.getLayoutManager()!!)
                if (smoothScroller != null) {
                    smoothScroller.targetPosition = position
                    _recyclerView!!.getLayoutManager()!!.startSmoothScroll(smoothScroller)
                    return true
                }
            } else {
                val viewHolder: RecyclerView.ViewHolder? = _recyclerView!!.findViewHolderForAdapterPosition(position)
                if (viewHolder != null) {
                    val distances = calculateDistanceToFinalSnap(
                        _recyclerView!!.getLayoutManager()!!,
                        viewHolder.itemView
                    )
                    _recyclerView!!.scrollBy(distances[0], distances[1])
                    return true
                }
            }
        }
        return false
    }


    private fun getDistanceToStart(targetView: View, helper: OrientationHelper): Int {
        val distance: Int
        // If we don't care about padding, just snap to the start of the view
        if (!_snapToPadding) {
            val childStart = helper.getDecoratedStart(targetView)
            distance = if (childStart >= helper.startAfterPadding / 2) {
                childStart - helper.startAfterPadding
            } else {
                childStart
            }
        } else {
            distance = helper.getDecoratedStart(targetView) - helper.startAfterPadding
        }
        return distance
    }

    private fun getDistanceToEnd(targetView: View, helper: OrientationHelper): Int {
        val distance: Int

        if (!_snapToPadding) {
            val childEnd = helper.getDecoratedEnd(targetView)
            distance = if (childEnd >= helper.end - (helper.end - helper.endAfterPadding) / 2) {
                helper.getDecoratedEnd(targetView) - helper.end
            } else {
                childEnd - helper.endAfterPadding
            }
        } else {
            distance = helper.getDecoratedEnd(targetView) - helper.endAfterPadding
        }

        return distance
    }

    /**
     * Returns the first view that we should snap to.
     *
     * @param layoutManager the RecyclerView's LayoutManager
     * @param helper        orientation helper to calculate view sizes
     * @param gravity       gravity to find the closest view
     * @return the first view in the LayoutManager to snap to, or null if we shouldn't snap to any
     */
    private fun findView(
        layoutManager: RecyclerView.LayoutManager,
        helper: OrientationHelper,
        gravity: Int,
        checkEdgeOfList: Boolean
    ): View? {
        if (layoutManager.childCount == 0 || layoutManager !is LinearLayoutManager) {
            return null
        }

        val lm = layoutManager

        // If we're at an edge of the list, we shouldn't snap
        // to avoid having the last item not completely visible.
        if (checkEdgeOfList && (isAtEdgeOfList(lm) && !_enableSnapLastItem)) {
            return null
        }

        var edgeView: View? = null
        var distanceToTarget = Int.MAX_VALUE
        val center = if (layoutManager.getClipToPadding()) {
            helper.startAfterPadding + helper.totalSpace / 2
        } else {
            helper.end / 2
        }

        val snapToStart = ((gravity == Gravity.START && !_isRtl)
                || (gravity == Gravity.END && _isRtl))

        val snapToEnd = ((gravity == Gravity.START && _isRtl)
                || (gravity == Gravity.END && !_isRtl))

        for (i in 0 until lm.childCount) {
            val currentView = lm.getChildAt(i)
            var currentViewDistance = if (snapToStart) {
                if (!_snapToPadding) {
                    abs(helper.getDecoratedStart(currentView).toDouble()).toInt()
                } else {
                    abs(
                        (helper.startAfterPadding
                                - helper.getDecoratedStart(currentView)).toDouble()
                    ).toInt()
                }
            } else if (snapToEnd) {
                if (!_snapToPadding) {
                    abs(
                        (helper.getDecoratedEnd(currentView)
                                - helper.end).toDouble()
                    ).toInt()
                } else {
                    abs(
                        (helper.endAfterPadding
                                - helper.getDecoratedEnd(currentView)).toDouble()
                    ).toInt()
                }
            } else {
                abs(
                    (helper.getDecoratedStart(currentView)
                            + (helper.getDecoratedMeasurement(currentView) / 2) - center).toDouble()
                ).toInt()
            }
            if (currentViewDistance < distanceToTarget) {
                distanceToTarget = currentViewDistance
                edgeView = currentView
            }
        }
        return edgeView
    }

    private fun isAtEdgeOfList(lm: LinearLayoutManager): Boolean {
        return if ((!lm.reverseLayout && _gravity == Gravity.START)
            || (lm.reverseLayout && _gravity == Gravity.END)
            || (!lm.reverseLayout && _gravity == Gravity.TOP)
            || (lm.reverseLayout && _gravity == Gravity.BOTTOM)
        ) {
            lm.findLastCompletelyVisibleItemPosition() == lm.itemCount - 1
        } else if (_gravity == Gravity.CENTER) {
            (lm.findFirstCompletelyVisibleItemPosition() == 0
                    || lm.findLastCompletelyVisibleItemPosition() == lm.itemCount - 1)
        } else {
            lm.findFirstCompletelyVisibleItemPosition() == 0
        }
    }

    /**
     * Dispatches a [SnapListener.onSnap] event if the snapped position
     * is different than [RecyclerView.NO_POSITION].
     *
     *
     * When [GravityLinearSnapHelper.findSnapView] returns null,
     * [GravityLinearSnapHelper.dispatchSnapChangeWhenPositionIsUnknown] is called
     *
     * @param newState the new RecyclerView scroll state
     */
    private fun onScrollStateChanged(newState: Int) {
        if (newState == RecyclerView.SCROLL_STATE_IDLE && _snapListener != null) {
            if (_isScrolling) {
                if (_nextSnapPosition != RecyclerView.NO_POSITION) {
                    _snapListener!!.onSnap(_nextSnapPosition)
                } else {
                    dispatchSnapChangeWhenPositionIsUnknown()
                }
            }
        }
        _isScrolling = newState != RecyclerView.SCROLL_STATE_IDLE
    }

    /**
     * Calls [GravityLinearSnapHelper.findSnapView]
     * without the check for the edge of the list.
     *
     *
     * This makes sure that a position is reported in [SnapListener.onSnap]
     */
    private fun dispatchSnapChangeWhenPositionIsUnknown() {
        val layoutManager = _recyclerView?.layoutManager ?: return
        val snapView = findSnapView(layoutManager, false) ?: return
        val snapPosition = _recyclerView!!.getChildAdapterPosition(snapView)
        if (snapPosition != RecyclerView.NO_POSITION) {
            _snapListener!!.onSnap(snapPosition)
        }
    }

    private fun getVerticalHelper(layoutManager: RecyclerView.LayoutManager): OrientationHelper {
        if (verticalHelper == null || verticalHelper!!.layoutManager !== layoutManager) {
            verticalHelper = OrientationHelper.createVerticalHelper(layoutManager)
        }
        return verticalHelper!!
    }

    private fun getHorizontalHelper(layoutManager: RecyclerView.LayoutManager): OrientationHelper {
        if (horizontalHelper == null || horizontalHelper!!.layoutManager !== layoutManager) {
            horizontalHelper = OrientationHelper.createHorizontalHelper(layoutManager)
        }
        return horizontalHelper!!
    }
}