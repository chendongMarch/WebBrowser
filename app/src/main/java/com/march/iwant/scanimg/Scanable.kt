package com.march.iwant.scanimg

import android.os.Parcelable

/**
 *  CreateAt : 2017/1/22
 *  Describe :
 *  @author chendong
 */
interface Scanable : Parcelable {
    fun getUrl(): String
}