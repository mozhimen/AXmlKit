package com.mozhimen.xmlk.textk.switcher

import android.content.Context
import android.graphics.Color
import android.graphics.Typeface
import android.text.TextUtils
import android.util.AttributeSet
import android.util.TypedValue
import android.view.View
import android.view.ViewGroup
import android.view.animation.Animation
import android.view.animation.TranslateAnimation
import android.widget.TextSwitcher
import android.widget.TextView
import android.widget.ViewSwitcher
import com.mozhimen.kotlin.elemk.androidx.lifecycle.commons.ILifecycleOwner
import com.mozhimen.kotlin.lintk.optins.OApiCall_BindLifecycle
import com.mozhimen.kotlin.lintk.optins.OApiCall_BindViewLifecycle
import com.mozhimen.kotlin.lintk.optins.OApiInit_ByLazy
import com.mozhimen.kotlin.utilk.android.util.sp2px
import com.mozhimen.kotlin.utilk.kotlin.applyTry
import com.mozhimen.kotlin.utilk.wrapper.UtilKRes
import com.mozhimen.taskk.temps.TaskKPollInfinite
import com.mozhimen.xmlk.basic.commons.IXmlK
import com.mozhimen.xmlk.basic.helpers.XmlLifecycleDelegate
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

/**
 * @ClassName TextKSwitcher
 * @Description TODO
 * @Author mozhimen
 * @Date 2025/2/14
 * @Version 1.0
 */
class TextKSwitcher @JvmOverloads constructor(context: Context, attrs: AttributeSet? = null) :
    TextSwitcher(context, attrs), ViewSwitcher.ViewFactory, IXmlK, ILifecycleOwner by XmlLifecycleDelegate() {

    private var _textSize: Float = 15f.sp2px()
    private var _textColor: Int = Color.BLACK
    private var _maxLines: Int = 0//最多显示的行数
    private var _ellipsize: Int = 3
    private var _textStyle: Int = 0
    private var _animDirection: Int = 0

    @Volatile
    private var _num = 0
    private val _dataList = mutableListOf<String>()

    //如果想要循环的变换文字颜色的话加上这个集合
//    private val colorList = mutableListOf<Int>()
    @OptIn(OApiCall_BindViewLifecycle::class, OApiCall_BindLifecycle::class, OApiInit_ByLazy::class)
    private val _taskKPollInfinite by lazy { TaskKPollInfinite() }

    /////////////////////////////////////////////////////////////////////

    init {
        initAttrs(attrs)
        initView()
    }

    /////////////////////////////////////////////////////////////////////


    /**
     * 填充数据
     */
    fun setDataList(theData: List<String>) {
        //防止滚动过程中处理数据发生异常
        stopScroll()

        _dataList.clear()
        _dataList.addAll(theData)
    }


//    /**
//     * 如果想要循环的变换字体颜色的话增加这个方法
//     */
//    fun setColorList(theData: List<Int>){
//        //防止滚动过程中处理数据发生异常
//        stopScroll()
//
//        colorList.clear()
//        colorList.addAll(theData)
//    }

    /**
     * 开启滚动
     * intervalTime：每隔多少秒滚动一次，可以不填写，默认是2秒切换一次
     */
    @OptIn(OApiCall_BindViewLifecycle::class, OApiCall_BindLifecycle::class, OApiInit_ByLazy::class)
    fun startScroll(intervalTime: Long = 2000) {
        if (!_taskKPollInfinite.isActive()) {
            _taskKPollInfinite.start(intervalTime) {
                _num++
                withContext(Dispatchers.Main) {
                    if (_dataList.size > 0) {
                        setText(_dataList[_num % _dataList.size])
                    }
                }
            }
        }
    }

    /**
     * 停止滚动
     */
    @OptIn(OApiCall_BindViewLifecycle::class, OApiCall_BindLifecycle::class, OApiInit_ByLazy::class)
    fun stopScroll() {
        _taskKPollInfinite.cancel()
    }

    /**
     * 点击VerticalTextSwitcher时需要知道其所属的下标位置，用户有可能会进行一些操作
     */
    fun getCurrentPosition(): Int =
        if (_dataList.size > 0) _num % _dataList.size else -1

    /**
     * 重写setText()方法，使得其可以循环的更改文字颜色
     */
    fun setText(text: CharSequence?, textColor: Int) {
        val nextView = nextView as TextView
        nextView.text = text
        nextView.setTextColor(UtilKRes.gainColor(textColor))
        showNext()
    }

    /////////////////////////////////////////////////////////////////////

    override fun initAttrs(attrs: AttributeSet?) {
        attrs ?: return
        context.obtainStyledAttributes(attrs, R.styleable.TextKSwitcher).applyTry({
            _textSize =
                it.getDimension(R.styleable.TextKSwitcher_textKSwitcher_textSize, _textSize)
            _textColor =
                it.getColor(R.styleable.TextKSwitcher_textKSwitcher_textColor, _textColor)
            _maxLines =
                it.getInt(R.styleable.TextKSwitcher_textKSwitcher_maxLines, _maxLines)
            _ellipsize =
                it.getInt(R.styleable.TextKSwitcher_textKSwitcher_ellipsize, _ellipsize)
            _textStyle =
                it.getInt(R.styleable.TextKSwitcher_textKSwitcher_textStyle, _textStyle)
            _animDirection =
                it.getInt(R.styleable.TextKSwitcher_textKSwitcher_animDirection, _animDirection)
        }, {
            it.recycle()
        })
    }

    @OptIn(OApiCall_BindViewLifecycle::class, OApiCall_BindLifecycle::class, OApiInit_ByLazy::class)
    override fun initView() {

        //创建动画
        when (_animDirection) {
            0 -> createBottomToTopAnimation()//从下往上的动画
            else -> createTopToBottomAnimation()//从上往下
        }

        setFactory(this)
        _taskKPollInfinite.bindLifecycle(this)
    }

    override fun makeView(): View {
        val textView = TextView(context)
        when (_ellipsize) {
            1 -> textView.ellipsize = TextUtils.TruncateAt.START
            2 -> textView.ellipsize = TextUtils.TruncateAt.MIDDLE
            3 -> textView.ellipsize = TextUtils.TruncateAt.END
            4 -> textView.ellipsize = TextUtils.TruncateAt.MARQUEE
        }
        when (_textStyle) {
            1 -> textView.typeface = Typeface.defaultFromStyle(Typeface.BOLD)
            2 -> textView.typeface = Typeface.defaultFromStyle(Typeface.ITALIC)
        }
        if (_maxLines > 0) {
            textView.maxLines = _maxLines
        }
        textView.setTextSize(TypedValue.COMPLEX_UNIT_PX, _textSize)
        textView.setTextColor(_textColor)
//        textView.layoutParams = F.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT)
        return textView
    }

    override fun onAttachedToWindow() {
        super.onAttachedToWindow()
        onCreate()
    }

    override fun onDetachedFromWindow() {
        super.onDetachedFromWindow()
        onDestroy()
    }

    override fun onWindowVisibilityChanged(visibility: Int) {
        super.onWindowVisibilityChanged(visibility)
        if (visibility == View.VISIBLE) {
            onStart()
            onResume()
        } else {
            onPause()
            onStop()
        }
    }

    /////////////////////////////////////////////////////////////////////

    /**
     * 从下到上的动画（实现3）
     */
    private fun createBottomToTopAnimation() {
        createAnimation(
            //入场动画
            Animation.ABSOLUTE, 0f,
            Animation.ABSOLUTE, 0f,
            Animation.RELATIVE_TO_PARENT, 1f,
            Animation.ABSOLUTE, 0f,
            //出场动画
            Animation.ABSOLUTE, 0f,
            Animation.ABSOLUTE, 0f,
            Animation.ABSOLUTE, 0f,
            Animation.RELATIVE_TO_PARENT, -1f
        )
    }

    /**
     * 从上到下（实现4）
     */
    private fun createTopToBottomAnimation() {
        createAnimation(
            //入场动画
            Animation.ABSOLUTE, 0f,
            Animation.ABSOLUTE, 0f,
            Animation.RELATIVE_TO_PARENT, -1f,
            Animation.ABSOLUTE, 0f,
            //出场动画
            Animation.ABSOLUTE, 0f,
            Animation.ABSOLUTE, 0f,
            Animation.ABSOLUTE, 0f,
            Animation.RELATIVE_TO_PARENT, 1f
        )
    }

    /**
     * 具体创建动画的方法
     */
    private fun createAnimation(
        ina: Int,
        inFromXValue: Float,
        inb: Int,
        inToXValue: Float,
        inc: Int,
        inFromYValue: Float,
        ind: Int,
        inToYValue: Float,

        outa: Int,
        outFromXValue: Float,
        outb: Int,
        outToXValue: Float,
        outc: Int,
        outFromYValue: Float,
        outd: Int,
        outToYValue: Float,
    ) {
        //入场动画
        val translateAnimationIn: Animation = TranslateAnimation(
            ina, inFromXValue,
            inb, inToXValue,
            inc, inFromYValue,
            ind, inToYValue
        )
        translateAnimationIn.duration = 1000
        translateAnimationIn.fillAfter = true
        inAnimation = translateAnimationIn
        //出场动画
        val translateAnimationOut: Animation = TranslateAnimation(
            outa, outFromXValue,
            outb, outToXValue,
            outc, outFromYValue,
            outd, outToYValue
        )
        translateAnimationOut.duration = 1000
        translateAnimationOut.fillAfter = true
        outAnimation = translateAnimationOut
    }
}

