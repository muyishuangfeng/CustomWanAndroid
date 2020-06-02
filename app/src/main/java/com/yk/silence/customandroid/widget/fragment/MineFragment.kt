package com.yk.silence.customandroid.widget.fragment

import android.annotation.SuppressLint
import androidx.core.view.isGone
import androidx.core.view.isVisible
import androidx.lifecycle.Observer
import com.yk.silence.customandroid.R
import com.yk.silence.customandroid.base.BaseVMFragment
import com.yk.silence.customandroid.common.ActivityManager
import com.yk.silence.customandroid.common.Constants
import com.yk.silence.customandroid.databinding.FragmentMineBinding
import com.yk.silence.customandroid.util.EventBus
import com.yk.silence.customandroid.viewmodel.mine.MineViewModel
import com.yk.silence.customandroid.widget.activity.*

class MineFragment : BaseVMFragment<MineViewModel, FragmentMineBinding>() {

    companion object {
        fun newInstance() = MineFragment()
    }

    override fun getLayoutID() = R.layout.fragment_mine

    override fun viewModelClass() = MineViewModel::class.java

    override fun initBinding(mBinding: FragmentMineBinding) {
        super.initBinding(mBinding)
        mBinding.lytMineAbout.setOnClickListener {
            checkLogin {
                ActivityManager.start(AboutActivity::class.java)
            }
        }
        mBinding.lytMineCollect.setOnClickListener {
            checkLogin {
                ActivityManager.start(MineCollectActivity::class.java)
            }
        }
        mBinding.lytMineHistory.setOnClickListener {
            checkLogin {
                ActivityManager.start(HistoryActivity::class.java)
            }
        }
        mBinding.lytMineOpenSource.setOnClickListener {
            checkLogin()
        }
        mBinding.lytMinePoint.setOnClickListener {
            checkLogin {
                ActivityManager.start(MinePointActivity::class.java)
            }
        }
        mBinding.lytMinePointRank.setOnClickListener {
            checkLogin {
                ActivityManager.start(PointRankActivity::class.java)
            }
        }
        mBinding.lytMineSetting.setOnClickListener {
            checkLogin {
                ActivityManager.start(SettingActivity::class.java)
            }
        }
        mBinding.lytMineShare.setOnClickListener {
            checkLogin {
                ActivityManager.start(ShareActivity::class.java)
            }
        }
        mBinding.cstHeader.setOnClickListener {
            checkLogin()
        }

    }

    override fun lazyLoadData() {
        super.lazyLoadData()
        mViewModel.getUserInfo()
    }

    @SuppressLint("SetTextI18n")
    override fun observe() {
        super.observe()
        mViewModel.run {
            isLogin.observe(viewLifecycleOwner, Observer {
                mBinding.txtMineRegister.isGone = it
                mBinding.txtNickName.isVisible = it
                mBinding.txtMineId.isVisible = it
            })
            mUserModel.observe(viewLifecycleOwner, Observer {
                it?.let {
                    mBinding.txtNickName.text = it.nickname
                    mBinding.txtMineId.text = "ID:${it.id}"
                }
            })
        }
        EventBus.post(Constants.USER_LOGIN_STATE_CHANGED, viewLifecycleOwner)
    }


}