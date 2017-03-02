package com.march.iwant.common.utils

import com.march.iwant.github.model.GitHubSearchParam

/**
 * CreateAt : 2017/1/19
 * Describe :
 * @author chendong
 */
object IWantCommonUtils {

    fun getGitHubSortTypeShow(type: String): String {
        var rst: String = "非选择类型"
        when (type) {
            GitHubSearchParam.SORT_BY_FORK -> rst = "按照fork数量排序"
            GitHubSearchParam.SORT_BY_STAR -> rst = "按照star数量排序"
            GitHubSearchParam.SORT_BY_UPDATE -> rst = "按照更新时间排序"
            GitHubSearchParam.SORT_BY_MACTH -> rst = "按照最佳匹配排序"
        }
        return rst
    }
}
