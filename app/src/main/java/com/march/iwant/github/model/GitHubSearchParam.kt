package com.march.iwant.github.model

/**
 *  CreateAt : 2017/1/19
 *  Describe :
 *  @author chendong
 */
data class GitHubSearchParam(
        //l 语言
        var l: String = "java",
        //s 排序条件
        var s: String = "stars",
        //q 查找
        var q: String = "CommonLib",
        //p page
        var p: Int = 1) {


    companion object {
        val SORT_BY_UPDATE = "updated"
        val SORT_BY_FORK = "forks"
        val SORT_BY_STAR = "stars"
        val SORT_BY_MACTH = ""
    }



}
