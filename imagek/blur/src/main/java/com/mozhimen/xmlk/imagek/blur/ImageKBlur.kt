package com.mozhimen.xmlk.imagek.blur

import android.animation.Animator
import android.animation.AnimatorListenerAdapter
import android.content.Context
import android.graphics.Bitmap
import android.graphics.Rect
import android.util.AttributeSet
import com.mozhimen.kotlin.utilk.android.util.UtilKLogWrapper
import android.view.View
import android.view.animation.AccelerateDecelerateInterpolator
import android.view.animation.AccelerateInterpolator
import androidx.appcompat.widget.AppCompatImageView
import com.mozhimen.animk.builder.commons.IAnimatorUpdateListener
import com.mozhimen.animk.builder.impls.AnimatorAlphaViewType
import com.mozhimen.taskk.executor.TaskKExecutor
import com.mozhimen.kotlin.utilk.android.util.e
import com.mozhimen.kotlin.utilk.android.view.UtilKViewWrapper
import com.mozhimen.kotlin.utilk.android.view.applyBackgroundNull
import com.mozhimen.kotlin.utilk.java.lang.UtilKThread
import com.mozhimen.blurk.mos.BlurKConfig
import com.mozhimen.blurk.utils.RenderScriptUtil
import com.mozhimen.kotlin.utilk.java.lang.UtilKThreadWrapper
import com.mozhimen.xmlk.commons.IXmlK
import java.util.concurrent.atomic.AtomicBoolean

/**
 * @ClassName ImageKBlur
 * @Description TODO
 * @Author mozhimen / Kolin Zhao
 * @Date 2022/12/24 16:37
 * @Version 1.0
 */
class ImageKBlur @JvmOverloads constructor(context: Context, attrs: AttributeSet? = null, defStyleAttr: Int = 0) :
    AppCompatImageView(context, attrs, defStyleAttr), IXmlK {

    @Volatile
    private var _abortBlur = false
    private var _blurOption: BlurKConfig? = null
    private val _blurFinish = AtomicBoolean(false)

    @Volatile
    private var _isAnimating = false
    private var _startDuration: Long = 0
    private var _cacheAction: CacheAction? = null
    private var _attachedCache: CacheAction? = null
    private var _isAttachedToWindow = false
    private var _cutoutX = 0
    private var _cutoutY = 0

    init {
        isFocusable = false
        isFocusableInTouchMode = false
        scaleType = ScaleType.MATRIX
        applyBackgroundNull()
    }

    fun setCutoutX(cutoutX: Int): ImageKBlur {
        this._cutoutX = cutoutX
        return this
    }

    fun setCutoutY(cutoutY: Int): ImageKBlur {
        this._cutoutY = cutoutY
        return this
    }

    fun applyBlurOption(option: BlurKConfig) {
        applyBlurOption(option, false)
    }

    fun destroy() {
        setImageBitmap(null)
        _abortBlur = true
        if (_blurOption != null) {
            _blurOption = null
        }
        if (_cacheAction != null) {
            _cacheAction!!.destroy()
            _cacheAction = null
        }
        _blurFinish.set(false)
        _isAnimating = false
        _startDuration = 0
    }

    fun update() {
        if (_blurOption != null) {
            applyBlurOption(_blurOption!!, true)
        }
    }

    /**
     * alpha进场动画
     */
    fun start(duration: Long) {
        _startDuration = duration
        if (!_blurFinish.get()) {
            if (_cacheAction == null) {
                _cacheAction = CacheAction({ start(_startDuration) }, 0)
                UtilKLogWrapper.e(TAG, "start 缓存模糊动画，等待模糊完成")
            }
            return
        }
        if (_cacheAction != null) {        //干掉缓存的runnable
            _cacheAction!!.destroy()
            _cacheAction = null
        }
        if (_isAnimating) return
        UtilKLogWrapper.i(TAG, "start 开始模糊alpha动画")
        _isAnimating = true
        if (duration > 0) {
            startAlphaInAnimation(duration)
        } else if (duration == -2L) {
            startAlphaInAnimation(if (_blurOption == null) 500 else _blurOption!!.getBlurInDuration())
        } else {
            imageAlpha = 255
        }
    }

    /**
     * alpha退场动画
     */
    fun dismiss(duration: Long) {
        _isAnimating = false
        UtilKLogWrapper.i(TAG, "dismiss 模糊imageview alpha动画")
        if (duration > 0) {
            startAlphaOutAnimation(duration)
        } else if (duration == -2L) {
            startAlphaOutAnimation(if (_blurOption == null) 500 else _blurOption!!.getBlurOutDuration())
        } else {
            imageAlpha = 0
        }
    }

    override fun onAttachedToWindow() {
        super.onAttachedToWindow()
        _isAttachedToWindow = true
        if (_attachedCache != null) {
            _attachedCache!!.forceRestore()
        }
    }

    override fun onDetachedFromWindow() {
        super.onDetachedFromWindow()
        _abortBlur = true
    }

    private fun startAlphaInAnimation(duration: Long) {
//        val valueAnimator = ValueAnimator.ofInt(0, 255)
//        valueAnimator.duration = duration
//        valueAnimator.interpolator = AccelerateDecelerateInterpolator()
//        valueAnimator.addListener(object : AnimatorListenerAdapter() {
//            override fun onAnimationEnd(animation: Animator) {
//                _isAnimating = false
//            }
//        })
//        valueAnimator.addUpdateListener { animation -> imageAlpha = (animation.animatedValue as Int) }
//        valueAnimator.start()

        AnimatorAlphaViewType().setAlpha(0f, 1f)
            .addAnimatorUpdateListener(object : IAnimatorUpdateListener<Int> {
                override fun onChange(value: Int?) {
                    value?.let {
                        imageAlpha = value
                    }
                }
            })
            .addAnimatorListener(object : AnimatorListenerAdapter() {
                override fun onAnimationEnd(animation: Animator) {
                    _isAnimating = false
                }
            }).setDuration(duration).setInterpolator(AccelerateDecelerateInterpolator()).build().start()
    }

    private fun startAlphaOutAnimation(duration: Long) {
//        val valueAnimator = ValueAnimator.ofInt(255, 0)
//        valueAnimator.duration = duration
//        valueAnimator.interpolator = AccelerateInterpolator()
//        valueAnimator.addListener(object : AnimatorListenerAdapter() {
//            override fun onAnimationEnd(animation: Animator) {
//                _isAnimating = false
//            }
//        })
//        valueAnimator.addUpdateListener { animation -> imageAlpha = (animation.animatedValue as Int) }
//        valueAnimator.start()

        AnimatorAlphaViewType().setAlpha(1f, 0f)
            .addAnimatorUpdateListener(object : IAnimatorUpdateListener<Int> {
                override fun onChange(value: Int?) {
                    value?.let {
                        imageAlpha = value
                    }
                }
            })
            .addAnimatorListener(object : AnimatorListenerAdapter() {
                override fun onAnimationEnd(animation: Animator) {
                    _isAnimating = false
                }
            })
            .setDuration(duration).setInterpolator(AccelerateInterpolator()).build().start()
    }

    /**
     * 子线程模糊
     * @param anchorView
     */
    private fun startBlurTask(anchorView: View) {
        TaskKExecutor.execute(TAG, runnable = CreateBlurBitmapRunnable(anchorView))
    }

    /**
     * 判断是否处于主线程，并进行设置bitmap
     * @param blurBitmap
     */
    private fun setImageBitmapOnUiThread(blurBitmap: Bitmap?, isOnUpdate: Boolean) {
        if (UtilKThreadWrapper.isMainThread()) {
            handleSetImageBitmap(blurBitmap, isOnUpdate)
        } else {
            if (!_isAttachedToWindow) {
                _attachedCache = CacheAction({ handleSetImageBitmap(blurBitmap, isOnUpdate) }, 0)
            } else {
                post { handleSetImageBitmap(blurBitmap, isOnUpdate) }
            }
        }
    }

    /**
     * 设置bitmap，并进行后续处理（此方法必定运行在主线程）
     * @param bitmap
     */
    private fun handleSetImageBitmap(bitmap: Bitmap?, isOnUpdate: Boolean) {
        if (bitmap != null) {
            UtilKLogWrapper.i(TAG, "bitmap: 【" + bitmap.width + "," + bitmap.height + "】")
        }
        imageAlpha = if (isOnUpdate) 255 else 0
        setImageBitmap(bitmap)
        val option = _blurOption
        if (option != null && !option.isFullScreen()) {
            val anchorView = option.getBlurView() ?: return            //非全屏的话，则需要将bitmap变化到对应位置
            val rect = Rect()
            anchorView.getGlobalVisibleRect(rect)
            val matrix = imageMatrix
            matrix.setTranslate(rect.left.toFloat(), rect.top.toFloat())
            imageMatrix = matrix
        }
        _blurFinish.compareAndSet(false, true)
        UtilKLogWrapper.i(TAG, "设置成功：" + _blurFinish.get())
        if (_cacheAction != null) {
            UtilKLogWrapper.i(TAG, "恢复缓存动画")
            _cacheAction!!.restore()
        }
        if (_attachedCache != null) {
            _attachedCache!!.destroy()
            _attachedCache = null
        }
    }

    private fun applyBlurOption(option: BlurKConfig, isOnUpdate: Boolean) {
        _blurOption = option
        val anchorView = option.getBlurView()
        if (anchorView == null) {
            UtilKLogWrapper.e(TAG, "applyBlurOption 模糊锚点View为空，放弃模糊操作...")
            destroy()
            return
        }
        if (option.isBlurAsync() && !isOnUpdate) {        //因为考虑到实时更新位置（包括模糊也要实时）的原因，因此强制更新时模糊操作在主线程完成。
            UtilKLogWrapper.i(TAG, "applyBlurOption 子线程blur")
            startBlurTask(anchorView)
        } else {
            try {
                UtilKLogWrapper.i(TAG, "applyBlurOption 主线程blur")
                if (!RenderScriptUtil.isRenderScriptSupported()) {
                    UtilKLogWrapper.e(TAG, "applyBlurOption 不支持脚本模糊。。。最低支持api 17(Android 4.2.2)，将采用fastBlur")
                }
                setImageBitmapOnUiThread(
                    RenderScriptUtil.blur(anchorView, option.getBlurPreScaleRatio(), option.getBlurRadius(), option.isFullScreen(), _cutoutX, _cutoutY), isOnUpdate
                )
            } catch (e: Exception) {
                e.printStackTrace()
                e.message?.e(TAG)
                UtilKLogWrapper.e(TAG, "applyBlurOption 模糊异常 $e")
                destroy()
            }
        }
    }

    inner class CreateBlurBitmapRunnable(target: View) : Runnable {
        private val _outWidth: Int
        private val _outHeight: Int
        private val _bitmap: Bitmap?

        init {
            _outWidth = target.width
            _outHeight = target.height
            _bitmap = UtilKViewWrapper.getBitmap_ofViewBackground(target, _blurOption!!.getBlurPreScaleRatio(), _blurOption!!.isFullScreen(), _cutoutX, _cutoutY)
        }

        override fun run() {
            if (_abortBlur || _blurOption == null) {
                UtilKLogWrapper.e(TAG, "run 放弃模糊，可能是已经移除了布局")
                return
            }
            UtilKLogWrapper.i(TAG, "run 子线程模糊执行")
            setImageBitmapOnUiThread(
                RenderScriptUtil.blur(_bitmap, _outWidth, _outHeight, _blurOption!!.getBlurRadius()), false
            )
        }
    }

    inner class CacheAction(
        private var _action: Runnable?, private var _delay: Long
    ) {
        private val _startTime: Long = System.currentTimeMillis()
        private val _isOverTime: Boolean
            get() = System.currentTimeMillis() - _startTime > 1000 //图片模糊超时1秒

        fun restore() {
            if (_isOverTime) {
                UtilKLogWrapper.e(TAG, "restore 模糊超时")
                destroy()
                return
            }
            if (_action != null) {
                post(_action)
            }
        }

        fun forceRestore() {
            if (_action != null) {
                post(_action)
            }
        }

        fun destroy() {
            if (_action != null) {
                removeCallbacks(_action)
            }
            _action = null
            _delay = 0
        }

        fun matches(otherAction: Runnable): Boolean {
            return (_action != null && _action == otherAction)
        }
    }
}