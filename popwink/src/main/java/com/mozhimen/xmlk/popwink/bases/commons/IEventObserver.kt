package com.mozhimen.xmlk.popwink.bases.commons

import android.os.Message


/**
 * @ClassName IEventObserver
 * @Description TODO
 * @Author Mozhimen & Kolin Zhao
 * @Date 2022/12/26 12:46
 * @Version 1.0
 */
interface IEventObserver {
    fun onEvent(msg: Message)
}