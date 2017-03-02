package com.march.iwant.xxxiao.model

import android.annotation.SuppressLint
import android.os.Parcel
import android.os.Parcelable
import com.march.iwant.scanimg.Scanable

/**
 *  CreateAt : 2017/1/22
 *  Describe :
 *  @author chendong
 */
@SuppressLint("ParcelCreator")
data class XiaoDetailDataModel(
        var linkUrl: String = "",
        var standardPicUrl: String = "",
        var thumbPicUrl: String = "",
        var width: String = "",
        var height: String = "",
        var srcSet: String = "") : Scanable {
    override fun getUrl(): String {
        return standardPicUrl
    }

    override fun describeContents(): Int {
        return 0
    }

    override fun writeToParcel(dest: Parcel?, flags: Int) {
        dest?.writeString(this.linkUrl)
        dest?.writeString(this.standardPicUrl)
        dest?.writeString(this.thumbPicUrl)
        dest?.writeString(this.width)
        dest?.writeString(this.height)
        dest?.writeString(this.srcSet)
    }

    constructor(inData: Parcel) : this(
            inData.readString(),
            inData.readString(),
            inData.readString(),
            inData.readString(),
            inData.readString(),
            inData.readString())

    companion object {
        @JvmField val CREATOR: Parcelable.Creator<XiaoDetailDataModel> =
                object : Parcelable.Creator<XiaoDetailDataModel> {
                    override fun createFromParcel(source: Parcel): XiaoDetailDataModel {
                        return XiaoDetailDataModel(source)
                    }

                    override fun newArray(size: Int): Array<XiaoDetailDataModel?> {
                        return arrayOfNulls(size)
                    }
                }
    }
}