package com.mozhimen.xmlk.viewk.draw.mos

import android.graphics.Path
import com.mozhimen.xmlk.viewk.draw.commons.IAction
import java.io.Writer

/**
 * @ClassName Line
 * @Description TODO
 * @Author Mozhimen & Kolin Zhao
 * @Date 2023/5/9 14:58
 * @Version 1.0
 */
class Line(val x: Float, val y: Float) : IAction {

    override fun perform(path: Path) {
        path.lineTo(x, y)
    }

    override fun perform(writer: Writer) {
        writer.write("L$x,$y")
    }
}