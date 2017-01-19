package com.march.iwant

import android.os.Bundle
import android.view.View
import android.widget.ImageView
import com.bumptech.glide.Glide
import com.march.dev.uikit.SimpleWebViewActivity
import com.march.dev.utils.CheckUtils
import com.march.iwant.model.PzDataModel
import com.march.lib.adapter.common.OnLoadMoreListener
import com.march.lib.adapter.common.SimpleItemListener
import com.march.lib.adapter.core.BaseViewHolder
import org.jsoup.Jsoup

/**
 * CreateAt : 2017/1/15
 * Describe : 泡在网上的日子
 * @author chendong
 */
internal class PzActivity : BaseCrawlerActivity<PzDataModel>() {

    companion object {
        private val BASE_URL = "http://www.jcodecraeer.com/plus/list.php?tid=18&TotalResult=1751&PageNo="
    }

    private var mPageNum = 1


    override fun getLayoutId(): Int {
        return R.layout.activity_jsoup
    }


    override fun onInitViews(view: View?, saveData: Bundle?) {
        super.onInitViews(view, saveData)
        mTitleBarView.setText("返回", "泡在网上的日子", null)
        mTitleBarView.setLeftBackListener(mActivity)
    }

    @Throws(Exception::class)
    override fun crawler() {
        val url = BASE_URL + mPageNum
        val document = Jsoup.connect(url).get()
        val liNodeList = document.getElementsByClass("archive-item clearfix")
        for (liNode in liNodeList) {
            val aNode = liNode.getElementsByTag("a")[0]
            val imgNoteList = aNode.getElementsByClass("cover imgloadinglater")
            var imgUrl: String? = null
            if (imgNoteList.size > 0) imgUrl = imgNoteList[0].attr("src")
            val pTag = liNode.getElementsByTag("p")[0]
            val PzDataModel = PzDataModel("http://www.jcodecraeer.com" + aNode.attr("href"), aNode.attr("title"), pTag.text(), if (imgUrl == null) "" else "http://www.jcodecraeer.com" + imgUrl)
            mDatas.add(PzDataModel)
        }
    }


    override fun getItemListener(): SimpleItemListener<PzDataModel> {
        return object : SimpleItemListener<PzDataModel>() {
            override fun onClick(pos: Int, holder: BaseViewHolder<*>?, data: PzDataModel?) {
                SimpleWebViewActivity.loadUrl(mActivity, data!!.link)
            }
        }
    }

    override fun getLoadMoreListener(): OnLoadMoreListener {
        return OnLoadMoreListener {
            mPageNum++
            startCrawlerTask()
        }
    }

    override fun getAdapterLayoutId(): Int {
        return R.layout.item_jsoup
    }


    override fun onBindDataShow(holder: BaseViewHolder<PzDataModel>, data: PzDataModel, pos: Int, type: Int) {
        holder.setText(R.id.tv_title, data.title)
                ?.setText(R.id.tv_desc, data.desc)
        if (CheckUtils.isEmpty(data.imgUrl)) {
            holder.setVisibility(R.id.iv_cover, View.GONE)
        } else {
            holder.setVisibility(R.id.iv_cover, View.VISIBLE)
            val iv = holder.getView<ImageView>(R.id.iv_cover)
            Glide.with(mActivity).load(data.imgUrl).into(iv)
        }
    }
}
