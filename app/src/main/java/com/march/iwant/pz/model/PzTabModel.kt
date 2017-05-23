package com.march.iwant.pz.model

/**
 *  CreateAt : 2017/3/2
 *  Describe :
 *  @author chendong
 */
data class PzTabModel(var type: Int) {

    companion object {
        val TYPE_ANDROID = 0
        val TYPE_JS = 1
        val TYPE_CSS = 2
        val TYPE_HTML5CSS3 = 3
        val TYPE_REACT = 4
        val TYPE_DB = 5
        val TYPE_MSG_IT = 6
        val TYPE_MSG_MOBILE = 7
        val TYPE_MSG_SOCIAL = 8
        val TYPE_MSG_BUSINESS = 9
        val TYPE_MSG_ANDROID = 10
    }

    private val URL_ANDROID = "http://www.jcodecraeer.com/plus/list.php?tid=16&PageNo="
    private val URL_WEB_JS = "http://www.jcodecraeer.com/plus/list.php?tid=1&PageNo="
    private val URL_WEB_CSS = "http://www.jcodecraeer.com/plus/list.php?tid=3&PageNo="
    private val URL_WEB_H5CSS3 = "http://www.jcodecraeer.com/plus/list.php?tid=26&PageNo="
    private val URL_DB = "http://www.jcodecraeer.com/plus/list.php?tid=14&PageNo="
    private val URL_MSG_IT = "http://www.jcodecraeer.com/plus/list.php?tid=8&PageNo="
    private val URL_MSG_MOBILE = "http://www.jcodecraeer.com/plus/list.php?tid=21&PageNo="
    private val URL_MSG_SOCIAL = "http://www.jcodecraeer.com/plus/list.php?tid=22&PageNo="
    private val URL_MSG_BUSINESS = "http://www.jcodecraeer.com/plus/list.php?tid=24&PageNo="
    private val URL_MSG_ANDROID = "http://www.jcodecraeer.com/plus/list.php?tid=17&PageNo="

    var url: String = ""
    var name: String = ""

    init {
        when (type) {
            TYPE_ANDROID -> {
                url = URL_ANDROID
                name = "Android"
            }
            TYPE_JS -> {
                url = URL_WEB_JS
                name = "JavaScript"
            }
            TYPE_CSS -> {
                url = URL_WEB_CSS
                name = "CSS"
            }
            TYPE_HTML5CSS3 -> {
                url = URL_WEB_H5CSS3
                name = "H5/CSS3"
            }
            TYPE_DB -> {
                url = URL_DB
                name = "数据库"
            }
            TYPE_MSG_IT -> {
                url = URL_MSG_IT
                name = "IT业界"
            }
            TYPE_MSG_MOBILE -> {
                url = URL_MSG_MOBILE
                name = "移动互联"
            }
            TYPE_MSG_SOCIAL -> {
                url = URL_MSG_SOCIAL
                name = "社交网络"
            }
            TYPE_MSG_BUSINESS -> {
                url = URL_MSG_BUSINESS
                name = "电子商务"
            }
            TYPE_MSG_ANDROID -> {
                url = URL_MSG_ANDROID
                name = "安卓资讯"
            }
        }
    }

}