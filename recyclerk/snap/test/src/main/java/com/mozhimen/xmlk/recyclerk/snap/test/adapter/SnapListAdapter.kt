package com.mozhimen.xmlk.recyclerk.snap.test.adapter

import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.core.view.isVisible
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.mozhimen.xmlk.recyclerk.snap.test.model.SnapList
import com.mozhimen.kotlin.lintk.optins.OApiCall_BindLifecycle
import com.mozhimen.kotlin.lintk.optins.OApiCall_BindViewLifecycle
import com.mozhimen.xmlk.recyclerk.decoration.RecyclerKDecorationLinearEdge
import com.mozhimen.xmlk.recyclerk.snap.RecyclerKSnap
import com.mozhimen.xmlk.recyclerk.snap.RecyclerKSnapHelperGravity
import com.mozhimen.xmlk.recyclerk.snap.test.R

class SnapListAdapter(private var items: List<SnapList> = listOf()) : RecyclerView.Adapter<SnapListAdapter.VH>() {

    fun setItems(list: List<SnapList>) {
        this.items = list
        notifyDataSetChanged()
    }

    override fun getItemViewType(position: Int): Int {
        return items[position].layoutId
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): VH {
        return VH(LayoutInflater.from(parent.context).inflate(viewType, parent, false))
    }

    override fun getItemCount(): Int {
        return items.size
    }

    override fun onBindViewHolder(holder: VH, position: Int) {
        holder.bind(items[position])
    }

    /////////////////////////////////////////////////////////////////////////////

    @OptIn(OApiCall_BindLifecycle::class, OApiCall_BindViewLifecycle::class)
    class VH(view: View) : RecyclerView.ViewHolder(view), RecyclerKSnapHelperGravity.SnapListener {
        private val titleView: TextView = view.findViewById(R.id.snapTextView)
        private val gravityLinearSnapRecyclerView: RecyclerKSnap = view.findViewById(R.id.recyclerView)
        private val linearLayoutManager = LinearLayoutManager(view.context, LinearLayoutManager.HORIZONTAL, false)
        private val gravityLinearSnapHelper = gravityLinearSnapRecyclerView.getGravityLinearSnapHelper()
        private val snapNextButton: View = view.findViewById(R.id.scrollNextButton)
        private val snapPreviousButton: View = view.findViewById(R.id.scrollPreviousButton)
        private var item: SnapList? = null
        private val appAdapter = AppAdapter()

        init {
            gravityLinearSnapRecyclerView.layoutManager = linearLayoutManager
            gravityLinearSnapRecyclerView.adapter = appAdapter
            gravityLinearSnapRecyclerView.clipChildren = false
            snapNextButton.setOnClickListener { gravityLinearSnapRecyclerView.snapToNext(true) }
            snapPreviousButton.setOnClickListener { gravityLinearSnapRecyclerView.snapToPrevious(true) }
            gravityLinearSnapRecyclerView.getGravityLinearSnapHelper().setSnapListener(this)
        }

        fun bind(snapList: SnapList) {
            item = snapList
            snapNextButton.isVisible = snapList.showScrollButtons
            titleView.text = snapList.title
            appAdapter.setItems(snapList.apps)
            gravityLinearSnapHelper.setEnableSnapLastItem(false)
            gravityLinearSnapHelper.setGravity(snapList.gravity, false)
            gravityLinearSnapHelper.setScrollMsPerInch(snapList.scrollMsPerInch)
            gravityLinearSnapHelper.setMaxFlingSizeFraction(snapList.maxFlingSizeFraction)
            gravityLinearSnapHelper.setSnapToPadding(snapList.snapToPadding)
            applyDecoration(snapList)
        }

        private fun applyDecoration(snapList: SnapList) {
            val decorations = gravityLinearSnapRecyclerView.itemDecorationCount
            repeat(decorations) {
                gravityLinearSnapRecyclerView.removeItemDecorationAt(0)
            }
            if (snapList.addStartDecoration) {
                gravityLinearSnapRecyclerView.addItemDecoration(
                    RecyclerKDecorationLinearEdge(
                        startPadding = gravityLinearSnapRecyclerView.resources.getDimensionPixelOffset(
                            R.dimen.extra_padding
                        ), endPadding = 0, orientation = RecyclerView.HORIZONTAL
                    )
                )
            }
            if (snapList.addEndDecoration) {
                gravityLinearSnapRecyclerView.addItemDecoration(
                    RecyclerKDecorationLinearEdge(
                        startPadding = 0, endPadding = gravityLinearSnapRecyclerView.resources.getDimensionPixelOffset(
                            R.dimen.extra_padding
                        ), orientation = RecyclerView.HORIZONTAL
                    )
                )
            }
        }

        override fun onSnap(position: Int) {
            Log.d("Snapped ${item?.title}", position.toString())
        }
    }
}
