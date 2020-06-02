package com.yk.silence.customandroid.widget.fragment

import android.annotation.SuppressLint
import android.os.Build
import androidx.annotation.RequiresApi
import androidx.core.view.isVisible
import androidx.core.widget.NestedScrollView
import androidx.lifecycle.Observer
import com.yk.silence.customandroid.R
import com.yk.silence.customandroid.base.BaseFragment
import com.yk.silence.customandroid.base.BaseVMFragment
import com.yk.silence.customandroid.common.ActivityManager
import com.yk.silence.customandroid.databinding.FragmentDiscoverBinding
import com.yk.silence.customandroid.impl.ScrollToTop
import com.yk.silence.customandroid.model.Article
import com.yk.silence.customandroid.model.BannerModel
import com.yk.silence.customandroid.ui.BannerImageLoader
import com.yk.silence.customandroid.viewmodel.discover.DiscoverViewModel
import com.yk.silence.customandroid.widget.activity.DetailActivity
import com.yk.silence.customandroid.widget.activity.MainActivity
import com.yk.silence.customandroid.widget.activity.SearchActivity
import com.yk.silence.customandroid.widget.activity.ShareActivity
import com.yk.silence.customandroid.widget.adapter.HotWordAdapter
import com.yk.silence.customandroid.widget.adapter.TagAdapter
import com.youth.banner.BannerConfig
import com.youth.banner.Transformer
import com.youth.banner.transformer.BackgroundToForegroundTransformer


class DiscoverFragment : BaseVMFragment<DiscoverViewModel, FragmentDiscoverBinding>(), ScrollToTop {

    private lateinit var mHotAdapter: HotWordAdapter

    companion object {
        fun newInstance() = DiscoverFragment()
    }

    override fun getLayoutID() = R.layout.fragment_discover

    override fun viewModelClass() = DiscoverViewModel::class.java

    @RequiresApi(Build.VERSION_CODES.M)
    @SuppressLint("ResourceAsColor")
    override fun initBinding(mBinding: FragmentDiscoverBinding) {
        super.initBinding(mBinding)
        mBinding.imgDiscoverAdd.setOnClickListener {
            checkLogin {
                ActivityManager.start(ShareActivity::class.java)
            }
        }
        mBinding.imgDiscoverSearch.setOnClickListener {
            ActivityManager.start(SearchActivity::class.java)
        }
        mBinding.srlDiscover.run {
            setColorSchemeColors(R.color.colorPrimary)
            setProgressBackgroundColorSchemeColor(R.color.bgColorPrimary)
            setOnRefreshListener {
                mViewModel.getData()
            }
        }

        mHotAdapter = HotWordAdapter(R.layout.item_hot_word).apply {
            bindToRecyclerView(mBinding.rlvDiscover)
            setOnItemClickListener { adapter, view, position -> }
        }
        mBinding.reloadView.setOnClickListener {
            mViewModel.getData()
        }

        mBinding.nsvDiscover.setOnScrollChangeListener { _, _, scrollY, _, oldScrollY ->
            if (activity is MainActivity && scrollY != oldScrollY) {
                (activity as MainActivity).animateBottomNavigationView(scrollY < oldScrollY)
            }
        }
    }


    override fun lazyLoadData() {
        super.lazyLoadData()
        mViewModel.getData()
    }

    override fun observe() {
        super.observe()
        mViewModel.run {
            mBannerList.observe(viewLifecycleOwner, Observer {
                setBanner(it)
            })
            mHotWords.observe(viewLifecycleOwner, Observer {
                mHotAdapter.setNewData(it)
                mBinding.txtHotTitle.isVisible = it.isNotEmpty()
            })
            mFrequentlyList.observe(viewLifecycleOwner, Observer {
                mBinding.tflDiscover.adapter = TagAdapter(it)
                mBinding.tflDiscover.setOnTagClickListener { _, position, _ ->
                    val frequently = it[position]
                    //TODO:跳转到详情页
                    ActivityManager.start(
                        DetailActivity::class.java,
                        mapOf(
                            DetailActivity.PARAM_ARTICLE to Article(
                                title = frequently.name,
                                link = frequently.link
                            )
                        )
                    )

                    false
                }
                mBinding.txtDiscoverWebsite.isVisible = it.isEmpty()

            })
            mRefreshState.observe(viewLifecycleOwner, Observer {
                mBinding.srlDiscover.isRefreshing = it
            })

            mReloadState.observe(viewLifecycleOwner, Observer {
                mBinding.reloadView.isVisible = it
            })
        }
    }


    override fun scrollToTop() {
        mBinding.nsvDiscover.smoothScrollTo(0, 0)
    }

    override fun onResume() {
        super.onResume()
        mBinding.bannerDiscover.startAutoPlay()
    }

    override fun onPause() {
        super.onPause()
        mBinding.bannerDiscover.stopAutoPlay()
    }

    /**
     * 设置banner
     */
    private fun setBanner(banners: List<BannerModel>) {
        mBinding.bannerDiscover.run {
            setBannerStyle(BannerConfig.NOT_INDICATOR)
            setImageLoader(BannerImageLoader())
            setImages(banners)
            setBannerAnimation(Transformer.BackgroundToForeground)
            start()
            setOnBannerListener {
                val banner = banners[it]
                //TODO:跳转到详情页
                ActivityManager.start(
                    DetailActivity::class.java,
                    mapOf(
                        DetailActivity.PARAM_ARTICLE to Article(
                            title = banner.title,
                            link = banner.url
                        )
                    )
                )
            }

        }

    }
}