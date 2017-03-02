package com.march.iwant.common.utils

import android.app.Activity
import android.app.WallpaperManager
import android.content.Context
import android.graphics.Bitmap
import android.os.Build
import android.view.View
import android.widget.ImageView
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.drawable.GlideDrawable
import com.bumptech.glide.request.RequestListener
import com.bumptech.glide.request.animation.GlideAnimation
import com.bumptech.glide.request.target.ImageViewTarget
import com.bumptech.glide.request.target.SimpleTarget
import com.bumptech.glide.request.target.Target
import com.march.dev.helper.Logger
import com.march.dev.helper.Toaster
import com.march.dev.utils.FileUtils
import com.march.dev.utils.PathUtils
import com.march.dev.utils.ShareUtils
import com.march.dev.widget.LeProgressView
import java.io.File
import java.io.FileNotFoundException
import java.io.FileOutputStream
import java.io.IOException
import java.lang.reflect.InvocationTargetException

/**
 * Project  : Reaper
 * Package  : com.march.reaper.helper
 * CreateAt : 2016/10/13
 * Describe :

 * @author chendong
 */

object IWantImageUtils {

    //    public static void loadRoundImg(Context context, String url, ImageView iv) {
    //        Glide.with(context).load(url).transform(new GlideRoundTransform(context, 50)).crossFade().into(iv);
    //    }

    fun loadImg(context: Context, url: String, iv: ImageView) {
        Glide.with(context).load(url).thumbnail(0.1f).crossFade().into(iv)
    }

    fun loadImg(context: Context, url: String, w: Int, h: Int, iv: ImageView) {
        Glide.with(context).load(url).override(w, h).crossFade().into(iv)
    }

    fun loadImgProgress(context: Activity, url: String, iv: ImageView, loadingView: View?) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN_MR1 && context.isDestroyed) {
            Logger.e("chendong", "load image after activity on destroy")
            return
        }
        Glide.with(context).load(url).into(object : ImageViewTarget<GlideDrawable>(iv) {
            override fun setResource(resource: GlideDrawable) {
                view.setImageDrawable(resource)
                if (loadingView != null) {
                    (loadingView as LeProgressView).stopLoading { loadingView.visibility = View.GONE }
                }
            }
        })

    }

    interface OnDownloadOverHandler {
        fun onSuccess(bitmap: Bitmap)
    }

    // 设置壁纸，锁屏壁纸和桌面壁纸
    fun setWallPaper(context: Context, isScreen: Boolean, isLockScreen: Boolean, url: String, endRunnable: Runnable?) {
        downloadPic(context, url, object : OnDownloadOverHandler {
            override fun onSuccess(bitmap: Bitmap) {
                val wm = WallpaperManager.getInstance(context)
                try {
                    if (isScreen) {
                        wm.setBitmap(bitmap)
                        Toaster.get().show(context, "桌面壁纸设置成功～")
                    }
                    if (isLockScreen) {
                        SetLockWallPaper(context, bitmap)
                        Toaster.get().show(context, "锁屏壁纸设置成功～")
                    }
                } catch (e: Exception) {
                    if (e is IOException) {
                        Toaster.get().show(context, "桌面壁纸设置失败")
                    } else {
                        Toaster.get().show(context, "锁屏壁纸设置失败")
                    }
                    e.printStackTrace()
                } finally {
                    endRunnable?.run()
                }

            }
        })
    }

    @Throws(InvocationTargetException::class, IllegalAccessException::class, NoSuchMethodException::class)
    private fun SetLockWallPaper(context: Context, bitmap: Bitmap) {
        val mWallManager = WallpaperManager.getInstance(context)
        val class1 = mWallManager.javaClass//获取类名
        val setWallPaperMethod = class1.getMethod("setBitmapToLockWallpaper", Bitmap::class.java)//获取设置锁屏壁纸的函数
        setWallPaperMethod.invoke(mWallManager, bitmap)//调用锁屏壁纸的函数，并指定壁纸的路径imageFilesPath

        Glide.with(context).load("").listener(object : RequestListener<String, GlideDrawable> {
            override fun onException(e: Exception, model: String, target: Target<GlideDrawable>, isFirstResource: Boolean): Boolean {
                return false
            }

            override fun onResourceReady(resource: GlideDrawable, model: String, target: Target<GlideDrawable>, isFromMemoryCache: Boolean, isFirstResource: Boolean): Boolean {
                return false
            }
        })
    }

    //图片保存到本地和分享
    fun saveToLocal(context: Context, url: String, isShare: Boolean, endRunnable: Runnable?) {
        downloadPic(context, url, object : OnDownloadOverHandler {
            override fun onSuccess(bitmap: Bitmap) {
                val saveDir: File
                val fileName = "download_" + System.currentTimeMillis() + ".jpg"
                try {
                    if (isShare) {
                        saveDir = PathUtils.thumb()
                    } else {
                        saveDir = PathUtils.download()
                    }
                    val file = File(saveDir, fileName)
                    bitmap.compress(Bitmap.CompressFormat.JPEG, 100, FileOutputStream(file))
                    if (isShare) {
                        ShareUtils.get(context).shareSingleImage(file.absolutePath)
                    } else {
                        Toaster.get().show(context, "图片下载完毕,保存到 " + file.absolutePath)
                        FileUtils.insertImageToMediaStore(context, file.absolutePath, System.currentTimeMillis(), bitmap.width, bitmap.height)
                    }
                } catch (e: FileNotFoundException) {
                    e.printStackTrace()
                } finally {
                    endRunnable?.run()
                }
            }
        })
    }

    fun downloadPic(context: Context, url: String, handler: OnDownloadOverHandler) {
        Glide.with(context).load(url).asBitmap().into(object : SimpleTarget<Bitmap>() {
            override fun onResourceReady(resource: Bitmap,
                                         glideAnimation: GlideAnimation<in Bitmap>) {
                handler.onSuccess(resource)
            }
        })
    }
}
