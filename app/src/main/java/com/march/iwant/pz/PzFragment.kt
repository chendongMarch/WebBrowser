package com.march.iwant.pz

import android.os.Bundle
import android.view.View
import android.widget.ImageView
import com.march.dev.uikit.SimpleWebViewActivity
import com.march.dev.utils.CheckUtils
import com.march.iwant.R
import com.march.iwant.base.BaseCrawlerFragment
import com.march.iwant.common.IntentKeys
import com.march.iwant.common.utils.IWantImageUtils
import com.march.iwant.pz.model.PzDataModel
import com.march.lib.adapter.common.OnLoadMoreListener
import com.march.lib.adapter.common.SimpleItemListener
import com.march.lib.adapter.core.BaseViewHolder
import org.jsoup.Jsoup

/**
 *  CreateAt : 2017/3/2
 *  Describe :
 *  @author chendong
 */
class PzFragment : BaseCrawlerFragment<PzDataModel>() {

    override fun isInitTitle(): Boolean {
        return false
    }

    private var mPageNum = 1
    private var mUrl: String? = ""

    companion object {

        fun newInst(type: Int, url: String): PzFragment {
            val pzFragment = PzFragment()
            val args = Bundle()
            args.putInt(IntentKeys.KEY_TYPE, type)
            args.putString(IntentKeys.KEY_URL, url)
            pzFragment.arguments = args
            return pzFragment
        }
    }


    override fun onInitIntent(intent: Bundle?) {
        val type = intent?.getInt(IntentKeys.KEY_TYPE)
        mUrl = intent?.getString(IntentKeys.KEY_URL, "")
    }


    override fun forceLoad(): Boolean {
        return false
    }

    override fun getLayoutId(): Int {
        return R.layout.pz_fragment
    }


    override fun onInitViews(view: View?, saveData: Bundle?) {
        super.onInitViews(view, saveData)
    }

    @Throws(Exception::class)
    override fun crawler() {
        val url = mUrl + mPageNum
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
                SimpleWebViewActivity.loadUrl(activity, data!!.link)
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
        return R.layout.pz_item_list
    }


    override fun onBindDataShow(holder: BaseViewHolder<PzDataModel>, data: PzDataModel, pos: Int, type: Int) {
        holder.setText(R.id.tv_title, data.title)
                ?.setText(R.id.tv_desc, data.desc)
        if (CheckUtils.isEmpty(data.imgUrl)) {
            holder.setVisibility(R.id.iv_cover, View.GONE)
        } else {
            holder.setVisibility(R.id.iv_cover, View.VISIBLE)
            val iv = holder.getView<ImageView>(R.id.iv_cover)
            IWantImageUtils.loadImg(activity, data.imgUrl, iv)
        }
    }

}