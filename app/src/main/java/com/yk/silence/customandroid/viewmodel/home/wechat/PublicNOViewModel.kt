package com.yk.silence.customandroid.viewmodel.home.wechat

import androidx.lifecycle.MutableLiveData
import com.yk.silence.customandroid.base.BaseViewModel
import com.yk.silence.customandroid.common.Constants
import com.yk.silence.customandroid.common.LoadMoreStatus
import com.yk.silence.customandroid.model.Article
import com.yk.silence.customandroid.model.Category
import com.yk.silence.customandroid.util.EventBus
import com.yk.silence.customandroid.viewmodel.collect.CollectRepository

class PublicNOViewModel : BaseViewModel() {

    companion object {
        const val INITIAL_CHECKED = 0
        const val INITIAL_PAGE = 1
    }

    //公众号
    private val mPublicNORepository by lazy { PublicNORepository() }

    //收藏
    private val mCollectRepository by lazy { CollectRepository() }

    //分类
    val mCategoryList: MutableLiveData<MutableList<Category>> = MutableLiveData()

    //选择的分类
    val mCheckedCategory: MutableLiveData<Int> = MutableLiveData()

    //文章集合
    val mArticleList: MutableLiveData<MutableList<Article>> = MutableLiveData()

    //加载更多状态
    val mLoadMoreStatus = MutableLiveData<LoadMoreStatus>()

    //下拉刷新状态
    val mRefreshStatus = MutableLiveData<Boolean>()

    //重新加载状态
    val mReloadStatus = MutableLiveData<Boolean>()

    //重新加载集合状态
    val mReloadListStatus = MutableLiveData<Boolean>()

    private var page = INITIAL_PAGE + 1

    /**
     * 获取公众号分类
     */
    fun getPublicNOCategories() {
        mRefreshStatus.value = true
        mReloadStatus.value = false
        launch(
            block = {
                val categories = mPublicNORepository.getPublicNOCategories()
                val checkedPosition = INITIAL_CHECKED
                val id = categories[checkedPosition].id
                val pagination = mPublicNORepository.getPublicNOArticleList(INITIAL_PAGE, id)
                page = pagination.curPage

                mCategoryList.value = categories
                mCheckedCategory.value = checkedPosition
                mArticleList.value = pagination.datas.toMutableList()
                mRefreshStatus.value = false
            },
            error = {
                mRefreshStatus.value = false
                mReloadStatus.value = true
            }
        )

    }

    /**
     * 刷新公众号文章集合
     */
    fun refreshPublicNOArticleList(
        checkedPosition: Int = mCheckedCategory.value ?: INITIAL_CHECKED
    ) {
        mRefreshStatus.value = true
        mReloadListStatus.value = false
        if (checkedPosition != mCheckedCategory.value) {
            mArticleList.value = mutableListOf()
            mCheckedCategory.value = checkedPosition
        }
        launch(
            block = {
                val categories = mCategoryList.value ?: return@launch
                val id = categories[checkedPosition].id
                val pagination = mPublicNORepository.getPublicNOArticleList(page, id)
                page = pagination.curPage

                mArticleList.value = pagination.datas.toMutableList()
                mRefreshStatus.value = false
            },
            error = {
                mRefreshStatus.value = false
                mReloadListStatus.value = mArticleList.value?.isEmpty()
            }
        )
    }

    /**
     * 上拉加载
     */
    fun loadMorePublicNOArticle() {
        mLoadMoreStatus.value = LoadMoreStatus.LOADING
        launch(
            block = {
                val categories = mCategoryList.value ?: return@launch
                val checkedPosition = mCheckedCategory.value ?: return@launch
                val id = categories[checkedPosition].id
                val pagination = mPublicNORepository.getPublicNOArticleList(page + 1, id)
                page = pagination.curPage

                val currentList = mArticleList.value ?: mutableListOf()
                currentList.addAll(pagination.datas)
                mArticleList.value = currentList

                mLoadMoreStatus.value = if (pagination.offset >= pagination.total) {
                    LoadMoreStatus.END
                } else {
                    LoadMoreStatus.COMPLETED
                }
            },
            error = {
                mLoadMoreStatus.value = LoadMoreStatus.ERROR
            }
        )

    }

    /**
     * 收藏
     */
    fun collect(id: Int) {
        launch(
            block = {
                mCollectRepository.collect(id)
                mUserRepository.updateUserInfo(mUserRepository.getUserInfo()!!.apply {
                    if (!collectIds.contains(id)) collectIds.add(id)
                })
                updateItemCollectState(id to true)
                EventBus.post(Constants.USER_COLLECT_UPDATED, id to true)
            },
            error = {
                updateItemCollectState(id to false)
            }
        )
    }

    /**
     * 取消收藏
     */
    fun unCollect(id: Int) {
        launch(
            block = {
                mCollectRepository.unCollect(id)
                mUserRepository.updateUserInfo(mUserRepository.getUserInfo()!!.apply {
                    if (collectIds.contains(id)) collectIds.remove(id)
                })
                updateItemCollectState(id to false)
                EventBus.post(Constants.USER_COLLECT_UPDATED, id to false)
            },
            error = {
                updateItemCollectState(id to true)
            }
        )
    }

    /**
     * 更新列表收藏状态
     */
    fun updateListCollectState() {
        val list = mArticleList.value
        if (list.isNullOrEmpty()) return
        if (mUserRepository.isLogin()) {
            val collectIds = mUserRepository.getUserInfo()?.collectIds ?: return
            list.forEach { it.collect = collectIds.contains(it.id) }
        } else {
            list.forEach { it.collect = false }
        }
        mArticleList.value = list
    }

    /**
     * 更新Item的收藏状态
     */
    fun updateItemCollectState(target: Pair<Int, Boolean>) {
        val list = mArticleList.value
        val item = list?.find { it.id == target.first } ?: return
        item.collect = target.second
        mArticleList.value = list
    }
}