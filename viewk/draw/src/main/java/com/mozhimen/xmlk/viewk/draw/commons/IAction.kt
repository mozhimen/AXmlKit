package com.mozhimen.xmlk.viewk.draw.commons

import android.graphics.Path
import java.io.Serializable
import java.io.Writer

/**
 * @ClassName Action
 * @Description TODO
 * @Author Mozhimen & Kolin Zhao
 * @Date 2023/5/9 14:58
 * @Version 1.0
 */
interface IAction : Serializable {
    fun perform(path: Path)

    fun perform(writer: Writer)
}