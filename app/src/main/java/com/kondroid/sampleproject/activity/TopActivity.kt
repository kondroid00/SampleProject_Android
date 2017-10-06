package com.kondroid.sampleproject.activity

import android.databinding.DataBindingUtil
import android.os.Bundle
import android.view.View
import com.kondroid.sampleproject.R
import com.kondroid.sampleproject.auth.AccountManager
import com.kondroid.sampleproject.databinding.ActivityTopBinding
import com.kondroid.sampleproject.helper.makeWeak
import com.kondroid.sampleproject.viewmodel.TopViewModel
import org.jetbrains.anko.startActivity

class TopActivity : BaseActivity() {
    private lateinit var vm: TopViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        var binding = DataBindingUtil.setContentView<ActivityTopBinding>(this, R.layout.activity_top)
        vm = TopViewModel(this)
        binding.vm = vm

        vm.initVM()

        setUpCallback()

        supportActionBar?.hide()

        if(AccountManager.hasUserId()) {
            vm.startButtonVisibility.set(View.VISIBLE)
            login()
        }
    }

    override fun onStop() {
        super.onStop()
        vm.release()
    }

    private fun setUpCallback() {
        val weakSelf = makeWeak(this)
        vm.onTapStart = {
            weakSelf.get()?.signUp()
        }
    }

    private fun signUp() {
        startActivity<SignUpActivity>()
    }

    private fun login() {
        val weakSelf = makeWeak(this)
        vm.login({
                     weakSelf.get()?.goToHome()
                 },
                 {e ->
                     weakSelf.get()?.showAlert(getString(R.string.alert_login_error_message),
                                               getString(R.string.alert_login_error_title),
                                               getString(R.string.alert_login_error_btn_retry),
                                               {
                                                   weakSelf.get()?.login()
                                               })
                 })
    }

}
