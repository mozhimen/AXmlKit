package com.mozhimen.xmlk.layoutk.magnet.commons

import com.mozhimen.xmlk.layoutk.magnet.LayoutKMagnet

/**
 * @ClassName ILayoutKMagnetListener
 * @Description TODO
 * @Author mozhimen
 * @Date 2024/9/10
 * @Version 1.0
 */
interface ILayoutKMagnetListener {
    fun onRemoved(layoutKMagnet: LayoutKMagnet)
    fun onClicked(layoutKMagnet: LayoutKMagnet)
}