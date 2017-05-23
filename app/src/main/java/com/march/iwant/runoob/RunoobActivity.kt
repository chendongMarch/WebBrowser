package com.march.iwant.runoob

import android.content.Context
import android.os.Bundle
import android.support.v7.widget.RecyclerView
import android.view.KeyEvent
import android.view.View
import android.view.inputmethod.EditorInfo
import android.view.inputmethod.InputMethodManager
import com.march.dev.helper.AnimatorHelper
import com.march.dev.helper.Toaster
import com.march.dev.uikit.SimpleWebViewActivity
import com.march.dev.utils.DimensionUtils
import com.march.iwant.R
import com.march.iwant.base.BaseCrawlerActivity
import com.march.iwant.runoob.model.RunoobModel
import com.march.lib.adapter.common.OnLoadMoreListener
import com.march.lib.adapter.common.SimpleItemListener
import com.march.lib.adapter.core.BaseViewHolder
import kotlinx.android.synthetic.main.runoob_activity.*
import org.jsoup.Jsoup

/**
 *  CreateAt : 2017/3/2
 *  Describe :
 *  @author chendong
 */
class RunoobActivity : BaseCrawlerActivity<RunoobModel>() {

    var mUrl: String = ""
    var mKeyWds: String = "android"
    var mCurrentPage = 0

    fun resetUrl() {
        mUrl = "http://www.runoob.com/?s=$mKeyWds&page=$mCurrentPage"
    }


    private var animatorHelper: AnimatorHelper? = null
    private var totalY: Int = 0

    fun attachRecyclerView(recyclerView: RecyclerView) {
        animatorHelper = AnimatorHelper(mTopLL, DimensionUtils.dp2px(mActivity, 50f))
        recyclerView.addOnScrollListener(object : RecyclerView.OnScrollListener() {
            override fun onScrolled(recyclerView: RecyclerView?, dx: Int, dy: Int) {
                super.onScrolled(recyclerView, dx, dy)
                totalY += dy
                if (dy == 0 || animatorHelper!!.isAnimate) return
                val threshold = animatorHelper!!.dist / 2
                if (totalY < threshold) {
                    animatorHelper!!.show()
                } else {
                    if (dy > 0 && totalY > threshold) {
                        // 往下
                        animatorHelper!!.hide()
                    } else {
                        // 往上
                        animatorHelper!!.show()
                    }
                }
            }
        })
    }

    override fun onInitViews(view: View?, saveData: Bundle?) {
        super.onInitViews(view, saveData)
        mSearchEt.imeOptions = EditorInfo.IME_ACTION_SEARCH
        mSearchEt.inputType = EditorInfo.TYPE_CLASS_TEXT
        mSearchEt.setOnEditorActionListener { v, actionId, event ->
            if (actionId == EditorInfo.IME_ACTION_SEND || (event != null && event.keyCode == KeyEvent.KEYCODE_ENTER)) {
                if ("" == mSearchEt.text.toString().trim())
                    false
                mDatas.clear()
                mAdapter.notifyDataSetChanged(mDatas, true)
                mAdapter.refreshPreDataCount()
                mCurrentPage = 0
                isLoadOver = false
                mKeyWds = mSearchEt.text.toString().trim()
                resetUrl()
                startCrawlerTask()
                val service: InputMethodManager = getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
                service.hideSoftInputFromWindow(mSearchEt.windowToken, 0)
                true
            }
            false
        }
        attachRecyclerView(mRv)
        resetUrl()
    }

    override fun onBindDataShow(holder: BaseViewHolder<RunoobModel>, data: RunoobModel, pos: Int, type: Int) {
        holder.setText(R.id.tv_title, data.title)
                ?.setText(R.id.tv_desc, data.desc)
    }

    override fun getItemListener(): SimpleItemListener<RunoobModel> {
        return object : SimpleItemListener<RunoobModel>() {
            override fun onClick(pos: Int, holder: BaseViewHolder<*>?, data: RunoobModel?) {
                SimpleWebViewActivity.loadUrl(mActivity, data!!.linkUrl)
            }
        }
    }

    override fun getLoadMoreListener(): OnLoadMoreListener {
        return OnLoadMoreListener {
            mRv.postDelayed({
                mCurrentPage++
                resetUrl()
                startCrawlerTask()
            }, 200)
        }
    }

    override fun crawler() {
        val document = Jsoup.connect(mUrl).get()
        val divNodeList = document.getElementsByClass("archive-list")[0].getElementsByClass("archive-list-item")
        for (element in divNodeList) {
            val model = RunoobModel()
            val aNodeList = element.getElementsByAttributeValue("rel", "bookmark")
            if (aNodeList.size > 0) {
                val aNode = aNodeList[0]
                model.linkUrl = aNode.attr("href")
                model.title = aNode.text()
                val pNode = element.getElementsByTag("p")[0]
                model.desc = pNode.text()
                mDatas.add(model)
            } else {
                Toaster.get().show(mActivity, "没有更多数据")
                isLoadOver = true
            }
        }
    }

    override fun getAdapterLayoutId(): Int {
        return R.layout.runoob_item_list
    }


    override fun isInitTitle(): Boolean {
        return false
    }

    override fun getLayoutId(): Int {
        return R.layout.runoob_activity
    }


}