package com.march.iwant.github

import android.os.Bundle
import android.view.View
import com.march.dev.helper.Logger
import com.march.dev.uikit.SimpleWebViewActivity
import com.march.dev.widget.TitleBarView
import com.march.iwant.R
import com.march.iwant.base.BaseCrawlerActivity
import com.march.iwant.github.model.GitHubDataModel
import com.march.iwant.github.model.GitHubSearchParam
import com.march.lib.adapter.common.OnLoadMoreListener
import com.march.lib.adapter.common.SimpleItemListener
import com.march.lib.adapter.core.BaseViewHolder
import org.jsoup.Jsoup

/**
 * CreateAt : 2017/1/16
 * Describe : 搜索GitHub项目
 * @author chendong
 */
internal class GitHubSearchActivity : BaseCrawlerActivity<GitHubDataModel>() {

    companion object {
        val BASE_URL = "https://github.com"
    }

    private lateinit var mQueryParam: GitHubSearchParam

    // lazy 使用时才创建
    private val mGitHubDialog: GitHubSearchParamDialog by lazy {
        val dialog: GitHubSearchParamDialog = GitHubSearchParamDialog(mContext)
        dialog.setListener(object : GitHubSearchParamDialog.OnChooseSearchListener {
            override fun onChoose(sortType: String, queryKeyWds: String) {
                if (mQueryParam.s == sortType && mQueryParam.q == queryKeyWds) {
                    return
                }
                if (startCrawlerTask()) {
                    mQueryParam.s = sortType
                    mQueryParam.q = queryKeyWds
                    mQueryParam.p = 1
                    mDatas.clear()
                    mAdapter.notifyDataSetChanged(mDatas, true)
                    mAdapter.refreshPreDataCount()
                }
            }

        })
        dialog
    }

    override fun getLayoutId(): Int {
        return R.layout.github_activity
    }

    override fun onInitDatas() {
        super.onInitDatas()
        mQueryParam = GitHubSearchParam()
    }


    override fun onInitViews(view: View?, saveData: Bundle?) {
        super.onInitViews(view, saveData)
        mTitleBarView.setText("返回", "GitHub", "选择")
        mTitleBarView.setLeftBackListener(mActivity)
        mTitleBarView.setListener(TitleBarView.RIGHT) {
            mGitHubDialog.show()
        }
    }

    private fun generateUrl(): String {
        val sb = StringBuilder("https://github.com/search?")
        sb.append("l=").append(mQueryParam.l)
        sb.append("&q=").append(mQueryParam.q)
        sb.append("&s=").append(mQueryParam.s)
        sb.append("&p=").append(mQueryParam.p)
        sb.append("&o=desc&type=Repositories&utf8=%E2%9C%93")
        return sb.toString()
    }


    override fun onBindDataShow(holder: BaseViewHolder<GitHubDataModel>, data: GitHubDataModel, pos: Int, type: Int) {
        holder.setText(R.id.tv_title, data.title)
                .setText(R.id.tv_desc, data.desc)
                .setText(R.id.tv_lang, "lang:" + data.language)
                .setText(R.id.tv_star, "star:" + data.starNum)
                .setText(R.id.tv_fork, "fork:" + data.forkNum)
                .setText(R.id.tv_time, data.time.substring(0, 10))
    }


    @Throws(Exception::class)
    override fun crawler() {
        val url = generateUrl()
        Logger.e(url)
        val document = Jsoup.connect(url).get()
        val liNoteList = document.getElementsByClass("col-12 d-block width-full py-4 border-bottom public source")

        for (element in liNoteList) {
            val aNode = element.getElementsByClass("v-align-middle")[0]
            val pNodeList = element.getElementsByClass("col-9 text-gray pr-4 py-1 m-0")
            var desc = ""
            if (pNodeList.size > 0) {
                desc = pNodeList[0].text()
            }
            val lang = element.getElementsByClass("mr-3")[0].text()
            val starNum = getTextIfExist(element.getElementsByAttributeValue("aria-label", "Stargazers"), "0")
            val forkNum = getTextIfExist(element.getElementsByAttributeValue("aria-label", "Forks"), "0")
            val time = element.getElementsByTag("relative-time").attr("datetime")
            val data = GitHubDataModel(aNode.text(), BASE_URL + aNode.attr("href"), desc, time, starNum, forkNum, lang)
            mDatas.add(data)
        }
    }


    override fun getItemListener(): SimpleItemListener<GitHubDataModel> {
        return object : SimpleItemListener<GitHubDataModel>() {
            override fun onClick(pos: Int, holder: BaseViewHolder<*>?, data: GitHubDataModel?) {
                SimpleWebViewActivity.loadUrl(mActivity, data!!.link)
            }
        }
    }

    override fun getLoadMoreListener(): OnLoadMoreListener {
        return OnLoadMoreListener {
            mRv.postDelayed({
                mQueryParam.p++
                startCrawlerTask()
            }, 2000)
        }
    }

    override fun getAdapterLayoutId(): Int {
        return R.layout.github_item_list
    }

}
