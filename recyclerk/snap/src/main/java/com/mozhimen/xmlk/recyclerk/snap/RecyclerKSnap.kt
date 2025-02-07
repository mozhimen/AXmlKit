package com.mozhimen.xmlk.recyclerk.snap

import android.content.Context
import android.content.res.TypedArray
import android.util.AttributeSet
import android.view.Gravity
import android.view.View
import com.mozhimen.kotlin.lintk.optins.OApiCall_BindLifecycle
import com.mozhimen.kotlin.lintk.optins.OApiCall_BindViewLifecycle
import com.mozhimen.xmlk.commons.ILayoutK
import com.mozhimen.xmlk.recyclerk.RecyclerKLifecycleNested

/**
 * @ClassName RecyclerKSnap
 * @Description TODO
 * @Author Mozhimen / Kolin Zhao
 * @Date 2024/9/8 20:01
 * @Version 1.0
 */
/**
 * An {@link OrientationAwareRecyclerView} that uses a default {@link GravitySnapHelper}
 */
@OApiCall_BindLifecycle
@OApiCall_BindViewLifecycle
class RecyclerKSnap @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null,
    defStyleAttr: Int = 0,
) : RecyclerKLifecycleNested(context, attrs, defStyleAttr), ILayoutK {

    private lateinit var _gravityLinearSnapHelper: RecyclerKSnapHelperGravity
    private var _snapGravity: Int = 0
    private var _snapToPadding: Boolean = false
    private var _enableSnapLastItem: Boolean = false
    private var _snapMaxFlingSizeFraction: Float = RecyclerKSnapHelperGravity.FLING_SIZE_FRACTION_DISABLE
    private var _snapScrollMsPerInch: Float = 100f
    private var _snapEnabled: Boolean = true

    ////////////////////////////////////////////////////////////////

    init {
        initAttrs(attrs, defStyleAttr)
        initView()
    }

    ////////////////////////////////////////////////////////////////

    override fun initAttrs(attrs: AttributeSet?, defStyleAttr: Int) {
        attrs ?: return
        var typedArray: TypedArray? = null
        try {
            typedArray = context.obtainStyledAttributes(attrs, R.styleable.RecyclerKSnap, defStyleAttr, 0)
            _snapGravity =
                typedArray.getInt(R.styleable.RecyclerKSnap_recyclerKSnap_snapGravity, _snapGravity)
            _snapToPadding =
                typedArray.getBoolean(R.styleable.RecyclerKSnap_recyclerKSnap_snapToPadding, _snapToPadding)
            _enableSnapLastItem =
                typedArray.getBoolean(R.styleable.RecyclerKSnap_recyclerKSnap_enableSnapLastItem, _enableSnapLastItem)
            _snapMaxFlingSizeFraction =
                typedArray.getFloat(R.styleable.RecyclerKSnap_recyclerKSnap_snapMaxFlingSizeFraction, _snapMaxFlingSizeFraction)
            _snapScrollMsPerInch =
                typedArray.getFloat(R.styleable.RecyclerKSnap_recyclerKSnap_snapScrollMsPerInch, _snapScrollMsPerInch)
            _snapEnabled =
                typedArray.getBoolean(R.styleable.RecyclerKSnap_recyclerKSnap_snapEnabled, _snapEnabled)
        } finally {
            typedArray?.recycle()
        }
    }

    override fun initView() {
        when (_snapGravity) {
            0 -> _gravityLinearSnapHelper = RecyclerKSnapHelperGravity(Gravity.START)
            1 -> _gravityLinearSnapHelper = RecyclerKSnapHelperGravity(Gravity.TOP)
            2 -> _gravityLinearSnapHelper = RecyclerKSnapHelperGravity(Gravity.END)
            3 -> _gravityLinearSnapHelper = RecyclerKSnapHelperGravity(Gravity.BOTTOM)
            4 -> _gravityLinearSnapHelper = RecyclerKSnapHelperGravity(Gravity.CENTER)
            else -> throw IllegalArgumentException(
                "Invalid gravity value. Use START " +
                        "| END | BOTTOM | TOP | CENTER constants"
            )
        }
        _gravityLinearSnapHelper.setSnapToPadding(_snapToPadding)
        _gravityLinearSnapHelper.setEnableSnapLastItem(_enableSnapLastItem)
        _gravityLinearSnapHelper.setMaxFlingSizeFraction(_snapMaxFlingSizeFraction)
        _gravityLinearSnapHelper.setScrollMsPerInch(_snapScrollMsPerInch)
        snapEnabled(_snapEnabled)
    }

    //////////////////////////////////////////////////////////

    override fun smoothScrollToPosition(position: Int) {
        if (!_snapEnabled || !_gravityLinearSnapHelper.smoothScrollToPosition(position))
            super.smoothScrollToPosition(position)
    }

    override fun scrollToPosition(position: Int) {
        if (!_snapEnabled || !_gravityLinearSnapHelper.scrollToPosition(position)) {
            super.scrollToPosition(position)
        }
    }

    //////////////////////////////////////////////////////////

    fun getGravityLinearSnapHelper(): RecyclerKSnapHelperGravity =
        _gravityLinearSnapHelper

    fun snapEnabled(enable: Boolean) {
        _snapEnabled = enable
        if (enable) {
            _gravityLinearSnapHelper.attachToRecyclerView(this)
        } else {
            _gravityLinearSnapHelper.attachToRecyclerView(null)
        }
    }

    fun snapEnabled(): Boolean =
        _snapEnabled

    fun snapToNext(smooth: Boolean) {
        snapTo(true, smooth)
    }

    fun snapToPrevious(smooth: Boolean) {
        snapTo(false, smooth)
    }

    private fun snapTo(next: Boolean, smooth: Boolean) {
        val lm = layoutManager ?: return
        val snapView: View = _gravityLinearSnapHelper.findSnapView(lm, false) ?: return
        val position = getChildAdapterPosition(snapView)
        if (next) {
            if (smooth) {
                smoothScrollToPosition(position + 1)
            } else {
                scrollToPosition(position + 1)
            }
        } else if (position > 0) {
            if (smooth) {
                smoothScrollToPosition(position - 1)
            } else {
                scrollToPosition(position - 1)
            }
        }
    }
}