package com.march.iwant.scanimg

import android.animation.ObjectAnimator
import android.animation.PropertyValuesHolder
import android.app.Activity
import android.content.Intent
import android.content.pm.PackageManager
import android.os.Bundle
import android.support.v4.util.SparseArrayCompat
import android.support.v4.view.PagerAdapter
import android.support.v4.view.ViewPager
import android.view.View
import android.view.ViewGroup
import android.view.animation.DecelerateInterpolator
import com.march.dev.app.dialog.impl.CommonMenuListDialog
import com.march.dev.utils.ActivityAnimUtils
import com.march.dev.utils.DimensionUtils
import com.march.dev.utils.PermissionUtils
import com.march.dev.widget.LeProgressView
import com.march.iwant.R
import com.march.iwant.base.IWantActivity
import com.march.iwant.common.Constant
import com.march.iwant.common.IntentKeys
import com.march.iwant.common.utils.IWantImageUtils
import kotlinx.android.synthetic.main.scan_img_list_activity.*
import uk.co.senab.photoview.PhotoView
import uk.co.senab.photoview.PhotoViewAttacher
import java.util.*

/**
 * 图片查看,可以缩放,基于photoview
 */
class ScanImgListActivity : IWantActivity() {

    private var mPhotoSrc: String = ""
    private var isShown = true

    private var mPos: Int = 0
    private val mDetailList: MutableList<Scanable> by lazy {
        intent.getParcelableArrayListExtra<Scanable>(IntentKeys.KEY_LIST)
    }
    private val mCachePhotoView: SparseArrayCompat<PhotoView> by lazy {
        SparseArrayCompat<PhotoView>()
    }
    private val mMoreMenuDialog: CommonMenuListDialog by lazy {
        initMoreMenuDialog()
    }
    private val mWallPaperMenuDialog: CommonMenuListDialog by lazy {
        initWallpaperSetDialog()
    }
    private val mEndRunnable: Runnable? by lazy {
        Runnable { showLoading(false) }
    }

    override fun getLayoutId(): Int {
        return R.layout.scan_img_list_activity
    }


    override fun onInitViews(view: View?, save: Bundle?) {
        super.onInitViews(view, save)
        mPos = intent.getIntExtra(IntentKeys.KEY_POS, 0)
        updateCenterText()
        registerClickListener(R.id.back_tv, R.id.right_tv)
        mImgListVp.adapter = ImgListPagerAdapter()
        mImgListVp.offscreenPageLimit = 3
        mImgListVp.currentItem = mPos
        hideTitleBar()
    }

    override fun onInitEvents() {
        super.onInitEvents()
        mImgListVp.addOnPageChangeListener(object : ViewPager.OnPageChangeListener {
            override fun onPageScrolled(position: Int, positionOffset: Float, positionOffsetPixels: Int) {

            }

            override fun onPageSelected(position: Int) {
                mPos = position
                if (mCachePhotoView.get(position - 1) != null) {
                    mCachePhotoView.get(position - 1).setScale(1.0f, true)
                }
                if (mCachePhotoView.get(position + 1) != null) {
                    mCachePhotoView.get(position + 1).setScale(1.0f, true)
                }
                updateCenterText()
            }

            override fun onPageScrollStateChanged(state: Int) {
                hideTitleBar()
            }
        })
    }


    private fun updateCenterText() {
        mCenterTv.text = String.format("专辑详情 %d/%d", mPos, mDetailList.size)
    }

    public override fun onClickView(view: View) {
        when (view.id) {
            R.id.back_tv ->
                if (mWallPaperMenuDialog.isShowing) {
                    mWallPaperMenuDialog.dismiss()
                } else if (mMoreMenuDialog.isShowing) {
                    mMoreMenuDialog.dismiss()
                } else {
                    onBackPressed()
                }
            R.id.right_tv -> mMoreMenuDialog.show()
        }
    }


    private fun initMoreMenuDialog(): CommonMenuListDialog {
        val myMenus = ArrayList<CommonMenuListDialog.Menu>()
        myMenus.add(CommonMenuListDialog.Menu(0, "下载到本地"))
        myMenus.add(CommonMenuListDialog.Menu(1, "设置为壁纸"))
        myMenus.add(CommonMenuListDialog.Menu(2, "分享给好友"))

        val mMoreMenuDialog = CommonMenuListDialog(mContext, myMenus)
        mMoreMenuDialog.setMenuTitle("更多")
        mMoreMenuDialog.setListener { pos, data, dialog ->
            mPhotoSrc = mDetailList[mPos].getUrl()
            when (pos) {
                0 -> {
                    showLoading(true)
                    IWantImageUtils.saveToLocal(mContext, mPhotoSrc, false, mEndRunnable)
                }
                1 -> {
                    mWallPaperMenuDialog.show()
                }
                2 -> {
                    showLoading(true)
                    IWantImageUtils.saveToLocal(mContext, mPhotoSrc, true, mEndRunnable)
                }
            }
        }
        return mMoreMenuDialog
    }

    override fun getPermission2Check(): Array<out String> {
        return arrayOf(PermissionUtils.PER_WRITE_EXTERNAL_STORAGE)
    }

    override fun handlePermissionResult(resultNotOk: MutableMap<String, Int>?): Boolean {
        var rst = true
        resultNotOk?.map {
            if (it.value == PackageManager.PERMISSION_DENIED)
                rst = false
        }
        return rst
    }

    private fun initWallpaperSetDialog(): CommonMenuListDialog {
        val menus = ArrayList<CommonMenuListDialog.Menu>()
        menus.add(CommonMenuListDialog.Menu(0, "锁屏壁纸"))
        menus.add(CommonMenuListDialog.Menu(1, "桌面壁纸"))
        menus.add(CommonMenuListDialog.Menu(2, "同时设置"))
        val mWallPaperMenuDialog = CommonMenuListDialog(mContext, menus)
        mWallPaperMenuDialog.setMenuTitle("设置壁纸")
        mWallPaperMenuDialog.setListener { pos, data, dialog ->
            showLoading(true)
            when (pos) {
                0 -> IWantImageUtils.setWallPaper(mContext, false, true, mPhotoSrc, mEndRunnable)
                1 -> IWantImageUtils.setWallPaper(mContext, true, false, mPhotoSrc, mEndRunnable)
                2 -> IWantImageUtils.setWallPaper(mContext, true, true, mPhotoSrc, mEndRunnable)
            }
        }
        return mWallPaperMenuDialog
    }

    private val DIST: Int by lazy { DimensionUtils.dp2px(mContext, 50f) }

    private fun showTitleBar() {
        if (isShown)
            return
        mTitleBar.visibility = View.VISIBLE
        val pvhY = PropertyValuesHolder.ofFloat("alpha", 0f, 1f)

        val pvhZ = PropertyValuesHolder.ofFloat("translationY", -DIST.toFloat(), 0f)
        val animator = ObjectAnimator.ofPropertyValuesHolder(mTitleBar, pvhY, pvhZ).setDuration(200)
        animator.interpolator = DecelerateInterpolator()
        animator.start()
        isShown = true
    }

    private fun hideTitleBar() {
        if (!isShown)
            return
        val pvhY = PropertyValuesHolder.ofFloat("alpha", 1f, 0f)
        val pvhZ = PropertyValuesHolder.ofFloat("translationY", 0f, -DIST.toFloat())
        val animator = ObjectAnimator.ofPropertyValuesHolder(mTitleBar, pvhY, pvhZ).setDuration(200)
        animator.interpolator = DecelerateInterpolator()
        animator.start()
        isShown = false
    }

    private fun toggleTitleBar() {
        if (isShown) {
            hideTitleBar()
        } else {
            showTitleBar()
        }
    }

    override fun isInitTitle(): Boolean {
        return false
    }

    internal inner class ImgListPagerAdapter : PagerAdapter() {

        override fun getCount(): Int {
            return mDetailList.size
        }

        override fun isViewFromObject(view: View, `object`: Any): Boolean {
            return view === `object`
        }

        override fun instantiateItem(container: ViewGroup, position: Int): Any {
            val inflate = layoutInflater.inflate(R.layout.scan_img_item_list, mImgListVp, false)
            val processView = inflate.findViewById(R.id.lpv_progress) as LeProgressView
            val detail = mDetailList[position]
            val iv = inflate.findViewById(R.id.scan_iv) as PhotoView
            iv.onPhotoTapListener = object : PhotoViewAttacher.OnPhotoTapListener {
                override fun onPhotoTap(view: View, v: Float, v1: Float) {
                    toggleTitleBar()
                }

                override fun onOutsidePhotoTap() {
                    toggleTitleBar()
                }
            }
            iv.setOnLongClickListener {
                mMoreMenuDialog.show()
                false
            }
            processView.startLoadingWithPrepare { IWantImageUtils.loadImgProgress(mActivity, detail.getUrl(), iv, processView) }
            mCachePhotoView.put(position, iv)
            container.addView(inflate)
            return inflate
        }

        override fun destroyItem(container: ViewGroup, position: Int, `object`: Any) {
            container.removeView(`object` as View)
        }
    }

    companion object {

        fun <T : Scanable> scanImgList(activity: Activity, detail: List<T>, pos: Int) {
            val intent = Intent(activity, ScanImgListActivity::class.java)
            val details = ArrayList<Scanable>()
            details.addAll(detail)
            intent.putExtra(IntentKeys.KEY_LIST, details)
            intent.putExtra(IntentKeys.KEY_POS, pos)
            activity.startActivity(intent)
            ActivityAnimUtils.translateStart(activity)
        }
    }
}
