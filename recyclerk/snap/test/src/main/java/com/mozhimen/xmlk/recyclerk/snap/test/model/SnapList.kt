package com.mozhimen.xmlk.recyclerk.snap.test.model

import android.view.Gravity
import com.mozhimen.xmlk.recyclerk.snap.RecyclerKSnapHelperGravity
import com.mozhimen.xmlk.recyclerk.snap.test.R

data class SnapList(
    val gravity: Int,
    val title: String,
    val apps: List<App>,
    val layoutId: Int = R.layout.adapter_snap,
    val snapToPadding: Boolean = false,
    val showScrollButtons: Boolean = true,
    val maxFlingSizeFraction: Float = RecyclerKSnapHelperGravity.FLING_SIZE_FRACTION_DISABLE,
    val scrollMsPerInch: Float = 100f,
    val addStartDecoration: Boolean = false,
    val addEndDecoration: Boolean = false
) {
    companion object {
        @JvmStatic
        fun getSnaps(apps: List<App>): List<SnapList> {
            return listOf(
                SnapList(
                    gravity = Gravity.CENTER,
                    title = "Center",
                    apps = apps
                ), SnapList(
                    gravity = Gravity.START,
                    title = "Start",
                    apps = apps
                ), SnapList(
                    gravity = Gravity.END,
                    title = "End",
                    apps = apps
                ), SnapList(
                    gravity = Gravity.CENTER,
                    title = "Center with fling limited",
                    maxFlingSizeFraction = 1.0f, // Max fling = recyclerview width
                    scrollMsPerInch = 50f,
                    apps = apps
                ), SnapList(
                    gravity = Gravity.START,
                    title = "Start with fling limited",
                    maxFlingSizeFraction = 1.0f,
                    scrollMsPerInch = 50f,
                    apps = apps
                ), SnapList(
                    layoutId = R.layout.adapter_snap_padding_start,
                    gravity = Gravity.START,
                    snapToPadding = true,
                    title = "Start with padding",
                    apps = apps
                ), SnapList(
                    gravity = Gravity.END,
                    layoutId = R.layout.adapter_snap_padding_end,
                    snapToPadding = true,
                    title = "End with padding",
                    apps = apps
                ), SnapList(
                    gravity = Gravity.START,
                    snapToPadding = false,
                    title = "Start with decoration",
                    apps = apps,
                    addStartDecoration = true
                ), SnapList(
                    gravity = Gravity.CENTER,
                    snapToPadding = false,
                    title = "Center with decoration",
                    addStartDecoration = true,
                    apps = apps
                ), SnapList(
                    gravity = Gravity.END,
                    snapToPadding = false,
                    title = "End with decoration",
                    addEndDecoration = true,
                    apps = apps
                ), SnapList(
                    gravity = Gravity.CENTER,
                    title = "Center with fast scroll",
                    scrollMsPerInch = 50f,
                    apps = apps
                ), SnapList(
                    gravity = Gravity.START,
                    title = "Start with fast scroll",
                    scrollMsPerInch = 50f,
                    apps = apps
                ), SnapList(
                    gravity = Gravity.CENTER,
                    title = "Center with slow scroll",
                    scrollMsPerInch = 200f,
                    apps = apps
                )
            )
        }
    }
}
