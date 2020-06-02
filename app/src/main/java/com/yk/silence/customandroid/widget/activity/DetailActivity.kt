package com.yk.silence.customandroid.widget.activity

import android.net.Uri
import android.os.Build
import android.util.Log
import android.view.KeyEvent
import android.view.ViewGroup
import android.webkit.ConsoleMessage
import android.webkit.WebResourceRequest
import android.webkit.WebView
import androidx.annotation.RequiresApi
import androidx.lifecycle.Observer
import com.just.agentweb.AgentWeb
import com.just.agentweb.DefaultWebClient
import com.just.agentweb.WebChromeClient
import com.just.agentweb.WebViewClient
import com.yk.silence.customandroid.R
import com.yk.silence.customandroid.base.BaseVMActivity
import com.yk.silence.customandroid.common.ActivityManager
import com.yk.silence.customandroid.common.Constants
import com.yk.silence.customandroid.databinding.ActivityDetailBinding
import com.yk.silence.customandroid.ext.htmlToSpanned
import com.yk.silence.customandroid.ext.setBrightness
import com.yk.silence.customandroid.ext.whiteHostList
import com.yk.silence.customandroid.model.Article
import com.yk.silence.customandroid.util.DayOrNightUtil
import com.yk.silence.customandroid.util.EventBus
import com.yk.silence.customandroid.util.SettingsStore
import com.yk.silence.customandroid.viewmodel.detail.DetailViewModel

class DetailActivity : BaseVMActivity<DetailViewModel, ActivityDetailBinding>() {
    //文章
    private lateinit var mArticle: Article
    private var agentWeb: AgentWeb? = null


    companion object {
        const val PARAM_ARTICLE = "param_article"
    }

    override fun getLayoutID() = R.layout.activity_detail

    override fun viewModelClass() = DetailViewModel::class.java

    override fun initBinding(mBinding: ActivityDetailBinding) {
        super.initBinding(mBinding)
        mArticle = intent?.getParcelableExtra(PARAM_ARTICLE) ?: return
        mBinding.txtDetailTitle.text = mArticle.title.htmlToSpanned()
        mBinding.imgDetailBack.setOnClickListener {
            ActivityManager.finish(DetailActivity::class.java)
        }
        mBinding.imgDetailMore.setOnClickListener {

        }

        if (DayOrNightUtil.isNightModel(this)) {
            setBrightness(0.8f)
        }

    }

    @RequiresApi(Build.VERSION_CODES.M)
    override fun initData() {
        super.initData()
        if (mArticle.id != 0) {
            mViewModel.saveReadHistory(mArticle)
        }
        agentWeb = AgentWeb.with(this)
            .setAgentWebParent(mBinding.flyWebContainer, ViewGroup.LayoutParams(-1, -1))
            .useDefaultIndicator(getColor(R.color.textColorPrimary), 2)
            .interceptUnkownUrl()
            .setMainFrameErrorView(R.layout.include_reload, R.id.btnReload)
            .setSecurityType(AgentWeb.SecurityType.STRICT_CHECK)
            .setOpenOtherPageWays(DefaultWebClient.OpenOtherPageWays.DISALLOW)
            .setWebChromeClient(object : WebChromeClient() {
                override fun onReceivedTitle(view: WebView?, title: String?) {
                    mBinding.txtDetailTitle.text = title
                    super.onReceivedTitle(view, title)
                }

                override fun onConsoleMessage(consoleMessage: ConsoleMessage?): Boolean {
                    Log.d("WanAandroidWebView", "${consoleMessage?.message()}")
                    return super.onConsoleMessage(consoleMessage)
                }
            })
            .setWebViewClient(object : WebViewClient() {
                override fun shouldOverrideUrlLoading(
                    view: WebView?,
                    request: WebResourceRequest?
                ): Boolean {
                    return !whiteHostList().contains(request?.url?.host)
                }

                override fun onPageFinished(view: WebView?, url: String?) {
                    super.onPageFinished(view, url)
                    view?.loadUrl(customJs(url))
                }
            })
            .createAgentWeb()
            .ready()
            .get()
        agentWeb?.webCreator?.webView?.run {
            overScrollMode = WebView.OVER_SCROLL_NEVER
            settings.run {
                javaScriptCanOpenWindowsAutomatically = false
                loadsImagesAutomatically = true
                useWideViewPort = true
                loadWithOverviewMode = true
                textZoom = SettingsStore.getWebTextZoom()
            }
        }
        agentWeb?.urlLoader?.loadUrl(mArticle.link)
    }

    /**
     * 加载js，去掉掘金、简书、CSDN等H5页面的Title、底部操作栏，以及部分广告
     */
    private fun customJs(url: String? = mArticle.link): String {
        val js = StringBuilder()
        js.append("javascript:(function(){")
        when (Uri.parse(url).host) {
            "juejin.im" -> {
                js.append("var headerList = document.getElementsByClassName('main-header-box');")
                js.append("if(headerList&&headerList.length){headerList[0].parentNode.removeChild(headerList[0])}")
                js.append("var openAppList = document.getElementsByClassName('open-in-app');")
                js.append("if(openAppList&&openAppList.length){openAppList[0].parentNode.removeChild(openAppList[0])}")
                js.append("var actionBox = document.getElementsByClassName('action-box');")
                js.append("if(actionBox&&actionBox.length){actionBox[0].parentNode.removeChild(actionBox[0])}")
                js.append("var actionBarList = document.getElementsByClassName('action-bar');")
                js.append("if(actionBarList&&actionBarList.length){actionBarList[0].parentNode.removeChild(actionBarList[0])}")
                js.append("var columnViewList = document.getElementsByClassName('column-view');")
                js.append("if(columnViewList&&columnViewList.length){columnViewList[0].style.margin = '0px'}")
            }
            "www.jianshu.com" -> {
                js.append("var jianshuHeader = document.getElementById('jianshu-header');")
                js.append("if(jianshuHeader){jianshuHeader.parentNode.removeChild(jianshuHeader)}")
                js.append("var headerShimList = document.getElementsByClassName('header-shim');")
                js.append("if(headerShimList&&headerShimList.length){headerShimList[0].parentNode.removeChild(headerShimList[0])}")
                js.append("var fubiaoList = document.getElementsByClassName('fubiao-dialog');")
                js.append("if(fubiaoList&&fubiaoList.length){fubiaoList[0].parentNode.removeChild(fubiaoList[0])}")
                js.append("var ads = document.getElementsByClassName('note-comment-above-ad-wrap');")
                js.append("if(ads&&ads.length){ads[0].parentNode.removeChild(ads[0])}")

                js.append("var lazyShimList = document.getElementsByClassName('v-lazy-shim');")
                js.append("if(lazyShimList&&lazyShimList.length&&lazyShimList[0]){lazyShimList[0].parentNode.removeChild(lazyShimList[0])}")
                js.append("if(lazyShimList&&lazyShimList.length&&lazyShimList[1]){lazyShimList[1].parentNode.removeChild(lazyShimList[1])}")
            }
            "blog.csdn.net" -> {
                js.append("var csdnToolBar = document.getElementById('csdn-toolbar');")
                js.append("if(csdnToolBar){csdnToolBar.parentNode.removeChild(csdnToolBar)}")
                js.append("var csdnMain = document.getElementById('main');")
                js.append("if(csdnMain){csdnMain.style.margin='0px'}")
                js.append("var operate = document.getElementById('operate');")
                js.append("if(operate){operate.parentNode.removeChild(operate)}")
            }
        }
        js.append("})()")
        return js.toString()
    }

    override fun onKeyDown(keyCode: Int, event: KeyEvent): Boolean {
        return if (agentWeb?.handleKeyEvent(keyCode, event) == true) {
            return true
        } else {
            super.onKeyDown(keyCode, event)
        }
    }

    override fun onResume() {
        agentWeb?.webLifeCycle?.onResume()
        super.onResume()
    }

    override fun onPause() {
        agentWeb?.webLifeCycle?.onPause()
        super.onPause()
    }

    override fun onDestroy() {
        agentWeb?.webLifeCycle?.onDestroy()
        super.onDestroy()
    }

    /**
     * 更改收藏状态
     */
    fun changeCollect() {
        if (mArticle.collect) {
            mViewModel.unCollect(mArticle.id)
        } else {
            mViewModel.collect(mArticle.id)
        }
    }

    /**
     * 刷新页面
     */
    fun refreshPage() {
        agentWeb?.urlLoader?.reload()
    }

    override fun observer() {
        super.observer()
        mViewModel.isCollect.observe(this, Observer {
            if (mArticle.collect != it) {
                mArticle.collect = it
                // 收藏状态变化，通知其他更新
                EventBus.post(Constants.USER_COLLECT_UPDATED, mArticle.id to it)
            }
        })
        EventBus.observe<Boolean>(Constants.USER_LOGIN_STATE_CHANGED, this) {
            mViewModel.updateCollectState(mArticle.id)
        }
    }

}
