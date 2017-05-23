package com.march.iwant.xxxiao

import android.os.Bundle
import android.widget.ImageView
import com.march.dev.helper.Logger
import com.march.dev.utils.DimensionUtils
import com.march.iwant.R
import com.march.iwant.base.BaseCrawlerFragment
import com.march.iwant.common.IntentKeys
import com.march.iwant.common.utils.IWantImageUtils
import com.march.iwant.xxxiao.model.XiaoAlbumDataModel
import com.march.lib.adapter.common.OnLoadMoreListener
import com.march.lib.adapter.common.SimpleItemListener
import com.march.lib.adapter.core.BaseViewHolder
import org.jsoup.Jsoup

/**
 *  CreateAt : 2017/1/19
 *  Describe : xxxiao数据爬取
 *  @author chendong
 */
class XiaoAlbumFragment : BaseCrawlerFragment<XiaoAlbumDataModel>() {

    private var mUrl: String? = ""

    companion object {
        fun newInst(url: String): XiaoAlbumFragment {
            val xiaoAlbumFragment = XiaoAlbumFragment()
            val args = Bundle()
            args.putString(IntentKeys.KEY_URL, url)
            xiaoAlbumFragment.arguments = args
            return xiaoAlbumFragment
        }
    }

    override fun onInitIntent(intent: Bundle?) {
        mUrl = intent?.getString(IntentKeys.KEY_URL)
    }

    override fun onBindDataShow(holder: BaseViewHolder<XiaoAlbumDataModel>, data: XiaoAlbumDataModel, pos: Int, type: Int) {
        holder.setText(R.id.tv_desc, data.desc)
                .setText(R.id.tv_tags, data.tagListStr)
        val imageView: ImageView = holder.getView(R.id.iv_image)
        val w = DimensionUtils.getScreenWidth(mContext)
        val h = w * (data.height.toFloat() / data.width.toFloat())
        imageView.layoutParams.width = w
        imageView.layoutParams.height = w
        IWantImageUtils.loadImg(mContext, data.imagePath, w, h.toInt(), imageView)
    }

    override fun getItemListener(): SimpleItemListener<XiaoAlbumDataModel> {
        return object : SimpleItemListener<XiaoAlbumDataModel>() {
            override fun onClick(pos: Int, holder: BaseViewHolder<*>, data: XiaoAlbumDataModel) {
                XiaoDetailActivity.startXiaoDetailActivity(activity, data.linkUrl)
            }
        }
    }

    override fun getLoadMoreListener(): OnLoadMoreListener {
        return OnLoadMoreListener {

        }
    }


    override fun forceLoad(): Boolean {
        return false
    }

    override fun crawler() {
        Logger.e("chendong",mUrl)
        val document = Jsoup.connect(mUrl).get()
        val divPostThumbList = document.getElementsByClass("post-thumb")
        var data: XiaoAlbumDataModel
        divPostThumbList.map {
            data = XiaoAlbumDataModel()
            data.pageUrl = mUrl!!
            val aNodeList = it.getElementsByClass("thumb-link")
            if (aNodeList.size > 0) {
                val aNode = aNodeList[0]
                data.linkUrl = aNode.attr("href")
                val imgNodeList = aNode.getElementsByTag("img")
                if (imgNodeList.size > 0) {
                    val imgNode = imgNodeList[0]
                    data.imagePath = imgNode.attr("src")
                    data.width = imgNode.attr("width")
                    data.height = imgNode.attr("height")
                    data.srcSet = imgNode.attr("srcSet")
                }
            }
            var tagListStr: String = "#"
            val relTagNodeList = it.getElementsByAttributeValue("rel", "category tag")
            relTagNodeList.map {
                tagListStr += (it.text() + "#")
            }
            data.tagListStr = tagListStr
            val aBookMarkNodeList = it.getElementsByAttributeValue("rel", "bookmark")
            if (aBookMarkNodeList.size > 0) {
                data.desc = aBookMarkNodeList[0].text()
            }
            mDatas.add(data)
        }
    }

    override fun isInitTitle(): Boolean {
        return false
    }

    override fun getAdapterLayoutId(): Int {
        return R.layout.xiao_album_item_list
    }

    override fun getLayoutId(): Int {
        return R.layout.xiao_album_fragment
    }

}