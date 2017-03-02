package com.march.iwant.github

import android.content.Context
import android.view.Gravity
import android.view.View
import android.widget.ArrayAdapter
import android.widget.EditText
import android.widget.Spinner
import com.march.dev.app.dialog.BaseDialog
import com.march.dev.utils.CheckUtils
import com.march.iwant.R
import com.march.iwant.common.utils.IWantCommonUtils
import com.march.iwant.github.model.GitHubSearchParam


/**
 * CreateAt : 2017/1/16
 * Describe : 选择github搜索条件的dialog
 * @author chendong
 */
class GitHubSearchParamDialog(context: Context) : BaseDialog(context) {

    private lateinit var mSpinner: Spinner
    private lateinit var mEt: EditText
    private lateinit var mDatas: MutableList<String>
    private lateinit var mListener: OnChooseSearchListener

    interface OnChooseSearchListener {
        fun onChoose(sortType: String, queryKeyWds: String)
    }

    fun setListener(listener: OnChooseSearchListener) {
        this.mListener = listener
    }

    override fun initViews() {
        registerClickListener(R.id.tv_cancel, R.id.tv_confirm)

        mEt = getView<EditText>(R.id.et)
        mSpinner = getView(R.id.spinner)
        mDatas = arrayListOf()
        mDatas.add(GitHubSearchParam.SORT_BY_FORK)
        mDatas.add(GitHubSearchParam.SORT_BY_STAR)
        mDatas.add(GitHubSearchParam.SORT_BY_MACTH)
        mDatas.add(GitHubSearchParam.SORT_BY_UPDATE)

        val dataShowList: MutableList<String> = arrayListOf()
        mDatas.map {
            dataShowList.add(IWantCommonUtils.getGitHubSortTypeShow(it))
        }
        mSpinner.adapter = ArrayAdapter(context, android.R.layout.simple_list_item_1, android.R.id.text1, dataShowList)
    }


    override fun onClickView(view: View?) {
        when (view!!.id) {
            R.id.tv_cancel -> dismiss()
            R.id.tv_confirm -> {
                val trim = mEt.text.toString().trim()
                if (!CheckUtils.isEmpty(trim)) {
                    mListener.onChoose(mDatas[mSpinner.selectedItemPosition], trim)
                }
                dismiss()
            }
        }
    }


    override fun getLayoutId(): Int {
        return R.layout.github_search_param_dialog
    }

    override fun setWindowParams() {
        buildDefaultParams(MATCH, WRAP, Gravity.BOTTOM)
        setBottomToCenterAnimation()
    }
}
