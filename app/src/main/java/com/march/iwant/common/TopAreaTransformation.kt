package com.march.iwant.common

import android.content.Context
import android.graphics.Bitmap
import com.bumptech.glide.load.engine.bitmap_recycle.BitmapPool
import com.bumptech.glide.load.resource.bitmap.BitmapTransformation
import com.march.dev.helper.Logger

/**
 *  CreateAt : 2017/1/23
 *  Describe :
 *  @author chendong
 */
class TopAreaTransformation(context: Context) : BitmapTransformation(context) {

    override fun getId(): String {
        return "com.march.iwant.common.TopAreaTransformation"
    }

    override fun transform(pool: BitmapPool, toTransform: Bitmap, outWidth: Int, outHeight: Int): Bitmap {
        val myTransformedBitmap = toTransform
        Logger.e("chendong", myTransformedBitmap.width.toString() + "  " + myTransformedBitmap.height)
        Logger.e("chendong", outWidth.toString() + "  " + outHeight)

        return myTransformedBitmap
    }
}