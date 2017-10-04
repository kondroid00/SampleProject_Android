package com.kondroid.sampleproject.activity

import android.databinding.DataBindingUtil
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import com.kondroid.sampleproject.R
import com.kondroid.sampleproject.auth.AccountManager
import com.kondroid.sampleproject.databinding.ActivityTopBinding
import com.kondroid.sampleproject.viewmodel.TopViewModel
import org.jetbrains.anko.startActivity

class TopActivity : BaseActivity() {
    private lateinit var vm: TopViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        var binding = DataBindingUtil.setContentView<ActivityTopBinding>(this, R.layout.activity_top)
        vm = TopViewModel()
        binding.vm = vm

        supportActionBar?.hide()

        if(AccountManager.hasUserId()) {
            vm.startButtonVisibility.set(View.VISIBLE)
            login()
        }

        vm.onTapStart = {
            signUp()
        }
    }

    private fun signUp() {
        startActivity<SignUpActivity>()
    }

    private fun login() {
        vm.login({
                    goToHome()
                 },
                 {e ->
                     showAlert(getString(R.string.error_login_message),
                               getString(R.string.error_login_title),
                               getString(R.string.error_login_btn_retry),
                               {
                                   login()
                               })
                 })
    }

}
