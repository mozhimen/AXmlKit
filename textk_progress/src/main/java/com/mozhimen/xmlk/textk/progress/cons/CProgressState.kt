package com.mozhimen.xmlk.textk.progress.cons

/**
 * @ClassName CProgressState
 * @Description TODO
 * @Author mozhimen
 * @Date 2024/11/6
 * @Version 1.0
 */
object CProgressState {
    const val PROGRESS_STATE_IDLE = 0//开始下载
    const val PROGRESS_STATE_LOAD = 1//下载之中
    const val PROGRESS_STATE_OVER = 2//下载完成
}