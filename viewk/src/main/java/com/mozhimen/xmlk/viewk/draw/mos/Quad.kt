package com.mozhimen.xmlk.viewk.draw.mos

import android.graphics.Path
import com.mozhimen.xmlk.viewk.draw.commons.IAction
import java.io.Writer

/**
 * @ClassName Quad
 * @Description TODO
 * @Author Mozhimen & Kolin Zhao
 * @Date 2023/5/9 14:59
 * @Version 1.0
 */
class Quad(private val x1: Float, private val y1: Float, private val x2: Float, private val y2: Float) : IAction {

    override fun perform(path: Path) {
        path.quadTo(x1, y1, x2, y2)
    }

    override fun perform(writer: Writer) {
        writer.write("Q$x1,$y1 $x2,$y2")
    }
}