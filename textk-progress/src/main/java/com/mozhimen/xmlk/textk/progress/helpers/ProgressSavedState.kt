package com.mozhimen.xmlk.textk.progress.helpers

import android.os.Parcel
import android.os.Parcelable
import android.os.Parcelable.Creator
import android.view.View
import com.mozhimen.xmlk.textk.progress.cons.CProgressState

/**
 * @ClassName ProgressSavedState
 * @Description TODO
 * @Author Mozhimen / Kolin Zhao
 * @Date 2023/10/22 22:09
 * @Version 1.0
 */

class ProgressSavedState : View.BaseSavedState {
    var progress: Int = 0
    var progressState: Int = CProgressState.PROGRESS_STATE_IDLE
    var charSequence: String = ""

    ////////////////////////////////////////////////////////////////////////////////////

    constructor(parcel: Parcel) : super(parcel) {
        this.progress = parcel.readInt()
        this.progressState = parcel.readInt()
        this.charSequence = parcel.readString() ?: ""
    }

    constructor(parcelable: Parcelable?, progress: Int, state: Int, currentText: String) : super(parcelable) {
        this.progress = progress
        this.progressState = state
        this.charSequence = currentText
    }

    override fun writeToParcel(out: Parcel, flags: Int) {
        super.writeToParcel(out, flags)
        out.writeInt(progress)
        out.writeInt(progressState)
        out.writeString(charSequence)
    }

    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (javaClass != other?.javaClass) return false

        other as ProgressSavedState

        if (progress != other.progress) return false
        if (progressState != other.progressState) return false
        if (charSequence != other.charSequence) return false

        return true
    }

    override fun hashCode(): Int {
        var result = progress.hashCode()
        result = 31 * result + progressState
        result = 31 * result + charSequence.hashCode()
        return result
    }

    companion object CREATOR : Creator<ProgressSavedState> {
        override fun createFromParcel(parcel: Parcel): ProgressSavedState {
            return ProgressSavedState(parcel)
        }

        override fun newArray(size: Int): Array<ProgressSavedState?> {
            return arrayOfNulls(size)
        }
    }
}
