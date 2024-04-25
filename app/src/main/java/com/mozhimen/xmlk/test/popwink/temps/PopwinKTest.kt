package com.mozhimen.xmlk.test.popwink.temps

import android.content.Context
import android.graphics.Color
import android.view.View
import android.view.animation.Animation
import com.mozhimen.basick.animk.builder.impls.AnimationTranslationType
import com.mozhimen.basick.utilk.android.util.UtilKLogWrapper
import com.mozhimen.xmlk.popwink.bases.BasePopwinKLifecycle
import com.mozhimen.xmlk.test.R


/**
 * @ClassName PopwinTest
 * @Description TODO
 * @Author Mozhimen & Kolin Zhao
 * @Version 1.0
 */
class PopwinKTest(
    context: Context,
    private val _constructor1: String,

    ) : BasePopwinKLifecycle(context) {
    interface IClickListener {
        fun onInvoke(log: String)
    }

    private var _variate1: String = "idel"
    private val _onClickListener = object : IClickListener {
        override fun onInvoke(log: String) {
            UtilKLogWrapper.d(TAG, "onInvoke: $log")
        }
    }

    init {
/*            _variate1 = "inited"
            _variate2 = "inited"
            _variate3 = "inited"
            _variate4 = "inited"*/
        setContentView(R.layout.popwink)
        setBackgroundColor(Color.TRANSPARENT)
    }

    override fun onViewCreated(contentView: View) {
        super.onViewCreated(contentView)
        UtilKLogWrapper.d(TAG, "onViewCreated: _constructor1 $_constructor1")
        UtilKLogWrapper.d(TAG, "onViewCreated: _variate1 $_variate1")
        _onClickListener.onInvoke("onViewCreated")
    }

    override fun onBeforeShow(): Boolean {
        UtilKLogWrapper.d(TAG, "onBeforeShow: _constructor1 $_constructor1")
        UtilKLogWrapper.d(TAG, "onBeforeShow: _variate1 $_variate1")
        _onClickListener.onInvoke("onBeforeShow")
        return super.onBeforeShow()
    }

    override fun onShowing() {
        super.onShowing()
        UtilKLogWrapper.d(TAG, "onShowing: _constructor1 $_constructor1")
        UtilKLogWrapper.d(TAG, "onShowing: _variate1 $_variate1")
        _onClickListener.onInvoke("onShowing")
    }

    override fun onBeforeDismiss(): Boolean {
        UtilKLogWrapper.d(TAG, "onBeforeDismiss: _constructor1 $_constructor1")
        UtilKLogWrapper.d(TAG, "onBeforeDismiss: _variate1 $_variate1")
        _onClickListener.onInvoke("onBeforeDismiss")
        return super.onBeforeDismiss()
    }

    override fun onDismiss() {
        UtilKLogWrapper.d(TAG, "onDismiss: _constructor1 $_constructor1")
        UtilKLogWrapper.d(TAG, "onDismiss: _variate1 $_variate1")
        _onClickListener.onInvoke("onDismiss")
        super.onDismiss()
    }

    override fun onCreateShowAnimation(): Animation {
        return AnimationTranslationType.FROM_TOP_SHOW.build()
    }

    override fun onCreateDismissAnimation(): Animation {
        return AnimationTranslationType.TO_TOP_HIDE.build()
    }
}