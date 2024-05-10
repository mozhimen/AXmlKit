package com.mozhimen.xmlk.recyclerk.decoration

import android.graphics.Rect
import android.view.View
import androidx.recyclerview.widget.RecyclerView

class RecyclerKDecorationSpaceGrid private constructor(private val halfSpace: Int) : RecyclerView.ItemDecoration() {

    override fun getItemOffsets(outRect: Rect, view: View, parent: RecyclerView, state: RecyclerView.State) {
        outRect.top = halfSpace
        outRect.bottom = halfSpace
        outRect.left = halfSpace
        outRect.right = halfSpace
    }

    companion object {
        fun setSingleGridSpaceDecoration(recyclerView: RecyclerView, pixelSpacing: Int) {
            if (recyclerView.itemDecorationCount == 0) {
                val halfSpacing = pixelSpacing / 2
                recyclerView.addItemDecoration(RecyclerKDecorationSpaceGrid(halfSpacing))
                recyclerView.setPadding(halfSpacing, halfSpacing, halfSpacing, halfSpacing)
                recyclerView.clipToPadding = false
            }
        }
    }
}
