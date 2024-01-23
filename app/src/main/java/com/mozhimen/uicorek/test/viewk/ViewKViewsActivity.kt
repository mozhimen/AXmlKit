package com.mozhimen.uicorek.test.viewk

import android.os.Bundle
import com.mozhimen.basick.elemk.androidx.appcompat.bases.databinding.BaseActivityVB
import com.mozhimen.uicorek.test.databinding.ActivityViewkViewsBinding

class ViewKViewsActivity : BaseActivityVB<ActivityViewkViewsBinding>() {

    override fun initView(savedInstanceState: Bundle?) {
        /*val squareQRScan = findViewById<SquareQRScan>(R.id.viewk_squareQrScan)
       squareQRScan.setSquareQrScanCallback(object : SquareQRScan.SquareQrScanCallback {
           override fun onAnimEnd() {
               Log.i(TAG, "onAnimEnd : OK")
           }
       })
       GlobalScope.launch(Dispatchers.Main) {
           delay(4000)
           squareQRScan.requireSuccess()
       }*/
    }
}