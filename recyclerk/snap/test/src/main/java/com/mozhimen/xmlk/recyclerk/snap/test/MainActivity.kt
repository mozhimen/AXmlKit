package com.mozhimen.xmlk.recyclerk.snap.test

import android.os.Bundle
import android.view.MenuItem
import androidx.appcompat.widget.Toolbar
import androidx.recyclerview.widget.LinearLayoutManager
import com.mozhimen.kotlin.lintk.optins.OApiCall_BindLifecycle
import com.mozhimen.kotlin.lintk.optins.OApiCall_BindViewLifecycle
import com.mozhimen.kotlin.utilk.android.content.startContext
import com.mozhimen.uik.databinding.bases.viewbinding.activity.BaseActivityVB
import com.mozhimen.xmlk.recyclerk.snap.test.adapter.AppAdapter
import com.mozhimen.xmlk.recyclerk.snap.test.adapter.SnapListAdapter
import com.mozhimen.xmlk.recyclerk.snap.test.databinding.ActivityMainBinding
import com.mozhimen.xmlk.recyclerk.snap.test.model.App
import com.mozhimen.xmlk.recyclerk.snap.test.model.SnapList

class MainActivity : BaseActivityVB<ActivityMainBinding>(), Toolbar.OnMenuItemClickListener {

    companion object {
        const val STATE_ORIENTATION = "orientation"
    }

    private var horizontal = false

    @OptIn(OApiCall_BindLifecycle::class, OApiCall_BindViewLifecycle::class)
    override fun initView(savedInstanceState: Bundle?) {
        horizontal = savedInstanceState?.getBoolean(STATE_ORIENTATION) ?: true

        vb.toolbar.inflateMenu(R.menu.main)
        vb.toolbar.setOnMenuItemClickListener(this)

        vb.recyclerView.bindLifecycle(this)
        vb.recyclerView.layoutManager = LinearLayoutManager(this)
        vb.recyclerView.setHasFixedSize(true)
        setupAdapter()
    }

    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)
        outState.putBoolean(STATE_ORIENTATION, horizontal)
    }

    override fun onMenuItemClick(item: MenuItem): Boolean {
        if (item.itemId == R.id.layoutType) {
            horizontal = !horizontal
            setupAdapter()
            item.title = if (horizontal) "Vertical" else "Horizontal"
        } else if (item.itemId == R.id.grid) {
            startContext<GridActivity>()
        }
        return false
    }

    @OptIn(OApiCall_BindLifecycle::class, OApiCall_BindViewLifecycle::class)
    private fun setupAdapter() {
        if (horizontal) {
            vb.recyclerView.snapEnabled(false)
            vb.recyclerView.adapter = SnapListAdapter(SnapList.getSnaps( App.getApps()))
        } else {
            vb.recyclerView.adapter = AppAdapter(R.layout.adapter_vertical,App.getApps())
            vb.recyclerView.snapEnabled(true)
        }
    }
}
