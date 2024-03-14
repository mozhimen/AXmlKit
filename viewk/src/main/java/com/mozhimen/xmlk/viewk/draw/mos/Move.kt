package com.mozhimen.xmlk.viewk.draw.mos

import android.graphics.Path
import com.mozhimen.xmlk.viewk.draw.commons.IAction
import java.io.Writer

/**
 * @ClassName Move
 * @Description TODO
 * @Author Mozhimen & Kolin Zhao
 * @Date 2023/5/9 14:59
 * @Version 1.0
 */
class Move(val x: Float, val y: Float) : IAction {

    override fun perform(path: Path) {
        path.moveTo(x, y)
    }

    override fun perform(writer: Writer) {
        writer.write("M$x,$y")
    }
}