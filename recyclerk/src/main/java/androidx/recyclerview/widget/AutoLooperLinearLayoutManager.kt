package androidx.recyclerview.widget

import android.content.Context
import android.view.View
import androidx.recyclerview.widget.RecyclerView.OnScrollListener
import androidx.recyclerview.widget.RecyclerView.Orientation
import com.mozhimen.basick.elemk.kotlin.properties.VarProperty_Set
import com.mozhimen.basick.utilk.android.util.UtilKLogWrapper
import com.mozhimen.xmlk.recyclerk.R
import java.lang.ref.WeakReference

/**
 * @ClassName AutoLooperLinearLayoutManager
 * @Description TODO
 * @Author Mozhimen & Kolin Zhao
 * @Date 2024/5/6
 * @Version 1.0
 */
class AutoLooperLinearLayoutManager : LooperLinearLayoutManager {
    private val _pixelDistancePer10ms = 3 //每10毫秒移动的像素值
    private var _recyclerViewRef: WeakReference<RecyclerView>? = null
    private var _isLooping = false

    private val _onScrollListener = object : OnScrollListener() {
        private var _isDragging = false

        override fun onScrollStateChanged(recyclerView: RecyclerView, newState: Int) {
            when (newState) {
                RecyclerView.SCROLL_STATE_DRAGGING -> {
                    _isDragging = true
                    stopLooper(recyclerView)
                }

                RecyclerView.SCROLL_STATE_IDLE -> {
                    if (!_isDragging) return
                    _isDragging = false
                    startLooper(recyclerView)
                }
            }
        }
    }

    //当recycleView出现在屏幕上的时候自动滚动，脱离屏幕取消消息，避免内存泄漏
    private val _onAttachStateChangeListener: View.OnAttachStateChangeListener = object : View.OnAttachStateChangeListener {
        override fun onViewAttachedToWindow(v: View) {
            _recyclerViewRef?.get()?.let {
                startLooper(it)
            }
        }

        override fun onViewDetachedFromWindow(v: View) {
            _recyclerViewRef?.get()?.let {
                stopLooper(it)
            }
        }
    }

    private var _autoLooperRunnable: Runnable = object : Runnable {
        override fun run() {
            _recyclerViewRef?.get()?.let {
                if (orientation == RecyclerView.HORIZONTAL) {
                    it.scrollBy(_pixelDistancePer10ms, 0)
                } else {
                    it.scrollBy(0, _pixelDistancePer10ms)
                }
                val tag: Any? = it.getTag(R.id.recyclerk_auto_looper_runnable) as? Runnable?
                if (tag != null && tag == this) {
                    _recyclerViewRef?.get()?.postDelayed(this, 40)
                }
            }
        }
    }

    /////////////////////////////////////////////////////////////////////////////////

    constructor(recyclerView: RecyclerView, context: Context) : this(recyclerView, context, RecyclerView.HORIZONTAL, false)

    constructor(recyclerView: RecyclerView, context: Context, @Orientation orientation: Int, reverseLayout: Boolean) : super(context, orientation, reverseLayout) {
        _recyclerViewRef = WeakReference(recyclerView)
        clearRecyclerViewLooperInfo(recyclerView)
        val looperInfo = LooperInfo().apply {
            onScrollListener = _onScrollListener.also { recyclerView.addOnScrollListener(it) }
            onAttachStateChangeListener = _onAttachStateChangeListener.also { recyclerView.addOnAttachStateChangeListener(it) }
            autoLooperRunnable = _autoLooperRunnable
        }
        setRecyclerViewLooperInfo(recyclerView, looperInfo)
    }

    /////////////////////////////////////////////////////////////////////////////////

    fun clearRecyclerViewLooperInfo(recyclerView: RecyclerView) {
        val tag = recyclerView.getTag(R.id.recyclerk_auto_looper_info)
        if (tag is LooperInfo) {
            tag.onScrollListener?.let { recyclerView.removeOnScrollListener(it) }
            tag.onAttachStateChangeListener?.let { recyclerView.removeOnAttachStateChangeListener(it) }
            tag.autoLooperRunnable?.let { recyclerView.removeCallbacks(it) }
            recyclerView.setTag(R.id.recyclerk_auto_looper_info, null)
        }
    }

    fun setRecyclerViewLooperInfo(recyclerView: RecyclerView, looperInfo: LooperInfo) {
        recyclerView.setTag(R.id.recyclerk_auto_looper_info, looperInfo)
    }

    fun startLooper(recyclerView: RecyclerView) {
        if (_isLooping) return
        _isLooping = true
        recyclerView.apply {
            removeCallbacks(_autoLooperRunnable)//Dragging
            setTag(R.id.recyclerk_auto_looper_runnable, _autoLooperRunnable)
            postDelayed(_autoLooperRunnable, 1000)
        }
    }

    fun stopLooper(recyclerView: RecyclerView) {
        if (!_isLooping) return
        _isLooping = false
        recyclerView.apply {
            removeCallbacks(_autoLooperRunnable)//Dragging
            setTag(R.id.recyclerk_auto_looper_runnable, null)
        }
    }

    /////////////////////////////////////////////////////////////////////////////////

    override fun onDetachedFromWindow(view: RecyclerView?, recycler: RecyclerView.Recycler?) {
        _recyclerViewRef?.get()?.let {
            UtilKLogWrapper.w(TAG, "stopLooper & clearRecyclerViewLooperInfo")
            stopLooper(it)
            clearRecyclerViewLooperInfo(it)
        }
        super.onDetachedFromWindow(view, recycler)
    }

    /////////////////////////////////////////////////////////////////////////////////

    class LooperInfo {
        var onScrollListener: OnScrollListener? = null
        var onAttachStateChangeListener: View.OnAttachStateChangeListener? = null
        var autoLooperRunnable: Runnable? = null
    }
}