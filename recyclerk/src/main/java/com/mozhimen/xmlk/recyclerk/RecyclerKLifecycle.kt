package com.mozhimen.xmlk.recyclerk

import android.content.Context
import android.util.AttributeSet
import androidx.lifecycle.LifecycleOwner
import androidx.recyclerview.widget.RecyclerView
import com.mozhimen.basick.lintk.optins.OApiCall_BindLifecycle
import com.mozhimen.basick.lintk.optins.OApiInit_ByLazy
import com.mozhimen.basick.elemk.androidx.lifecycle.commons.IDefaultLifecycleObserver
import com.mozhimen.basick.lintk.optins.OApiCall_BindViewLifecycle
import com.mozhimen.basick.utilk.androidx.lifecycle.runOnMainThread


/**
 * @ClassName RecyclerKLifecycle
 * @Description 这边RecyclerView持有Adapter会造成内存泄漏, 特别是在fragment中, 所以在onPause要置空
 * @Author Mozhimen & Kolin Zhao
 * @Date 2022/11/23 11:42
 * @Version 1.0
 */
@OptIn(OApiInit_ByLazy::class)
@OApiCall_BindLifecycle
@OApiCall_BindViewLifecycle
class RecyclerKLifecycle @JvmOverloads constructor(context: Context, attrs: AttributeSet? = null, defStyleAttr: Int = 0) : RecyclerView(context, attrs, defStyleAttr), IDefaultLifecycleObserver {

    override fun bindLifecycle(owner: LifecycleOwner) {
        owner.runOnMainThread {
            owner.lifecycle.removeObserver(this@RecyclerKLifecycle)
            owner.lifecycle.addObserver(this@RecyclerKLifecycle)
        }
    }

    override fun onDestroy(owner: LifecycleOwner) {
        this.adapter = null
        owner.lifecycle.removeObserver(this@RecyclerKLifecycle)
    }
}