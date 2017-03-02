package com.march.iwant.xxxiao

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.support.v7.widget.LinearLayoutManager
import android.view.View
import android.widget.ImageView
import com.bumptech.glide.Glide
import com.march.dev.utils.ActivityAnimUtils
import com.march.dev.utils.DimensionUtils
import com.march.iwant.R
import com.march.iwant.base.BaseCrawlerActivity
import com.march.iwant.common.Constant
import com.march.iwant.scanimg.ScanImgListActivity
import com.march.iwant.xxxiao.model.XiaoDetailDataModel
import com.march.lib.adapter.common.OnLoadMoreListener
import com.march.lib.adapter.common.SimpleItemListener
import com.march.lib.adapter.core.BaseViewHolder
import org.jsoup.Jsoup

/**
 *  CreateAt : 2017/1/22
 *  Describe :
 *  @author chendong
 */
class XiaoDetailActivity : BaseCrawlerActivity<XiaoDetailDataModel>() {

    var mUrl: String = ""

    companion object {
        fun startXiaoDetailActivity(activity: Activity, url: String) {
            val intent = Intent(activity, XiaoDetailActivity::class.java)
            intent.putExtra(Constant.KEY_URL, url)
            activity.startActivity(intent)
            ActivityAnimUtils.translateStart(activity)
        }
    }

    override fun getAdapterLayoutId(): Int {
        return R.layout.xiao_detail_item_list
    }

    override fun onInitDatas() {
        super.onInitDatas()
        mUrl = intent.getStringExtra(Constant.KEY_URL)
    }


    override fun onInitViews(view: View?, saveData: Bundle?) {
        super.onInitViews(view, saveData)
        mTitleBarView.setText("返回", "详情", null)
        mTitleBarView.setLeftBackListener(mActivity)
    }

    override fun initRecyclerView(): Boolean {
//        mRv.layoutManager = StaggeredGridLayoutManager(2, StaggeredGridLayoutManager.VERTICAL)
        mRv.layoutManager = LinearLayoutManager(mContext, LinearLayoutManager.VERTICAL, false)
        return true
    }


    override fun getItemListener(): SimpleItemListener<XiaoDetailDataModel> {
        return object : SimpleItemListener<XiaoDetailDataModel>() {
            override fun onClick(pos: Int, holder: BaseViewHolder<*>?, data: XiaoDetailDataModel?) {
                ScanImgListActivity.scanImgList(mActivity, mDatas, pos)
            }
        }
    }

    override fun onBindDataShow(holder: BaseViewHolder<XiaoDetailDataModel>, data: XiaoDetailDataModel, pos: Int, type: Int) {
        val imageView: ImageView = holder.getView(R.id.iv_image)
        val w = DimensionUtils.getScreenWidth(mContext)
        val h = w * (data.height.toFloat() / data.width.toFloat())
        imageView.layoutParams.width = w
        imageView.layoutParams.height = h.toInt()
        Glide.with(mContext).load(data.standardPicUrl).override(w, h.toInt()).into(imageView)
    }

    override fun getLoadMoreListener(): OnLoadMoreListener {
        return OnLoadMoreListener {

        }
    }

    override fun crawler() {
        val document = Jsoup.connect(mUrl).get()
        val aNodeList = document.getElementsByClass("rgg-a local-link")
        var model: XiaoDetailDataModel
        aNodeList.map {
            model = XiaoDetailDataModel()
            model.standardPicUrl = it.attr("href")
            model.linkUrl = mUrl
            val imgTagList = it.getElementsByTag("img")
            if (imgTagList.size > 0) {
                val imgNode = imgTagList[0]
                model.thumbPicUrl = imgNode.attr("src")
                model.width = imgNode.attr("width")
                model.height = imgNode.attr("height")
                model.srcSet = imgNode.attr("srcSet")
                mDatas.add(model)
            }
        }
    }

    override fun getLayoutId(): Int {
        return R.layout.xiao_detail_activity
    }

}