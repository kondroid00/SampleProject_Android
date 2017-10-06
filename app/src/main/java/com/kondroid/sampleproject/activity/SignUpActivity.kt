package com.kondroid.sampleproject.activity

import android.databinding.DataBindingUtil
import android.os.Bundle
import com.kondroid.sampleproject.R
import com.kondroid.sampleproject.databinding.ActivitySignupBinding
import com.kondroid.sampleproject.helper.makeWeak
import com.kondroid.sampleproject.viewmodel.SignUpViewModel

class SignUpActivity : BaseActivity() {
    private lateinit var vm: SignUpViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        var binding = DataBindingUtil.setContentView<ActivitySignupBinding>(this, R.layout.activity_signup)
        vm = SignUpViewModel(this)
        binding.vm = vm

        vm.initVM()

        setUpCallback()

        setTitle(R.string.title_signup)
    }

    override fun onStop() {
        super.onStop()
        vm.release()
    }

    private fun setUpCallback() {
        val weakSelf = makeWeak(this)
        vm.onTapRegister = {
            weakSelf.get()?.signUp()
        }
    }

    private fun signUp() {
        val weakSelf = makeWeak(this)
        vm.signUp({
                      weakSelf.get()?.goToHome()
                  },
                  {e ->
                      weakSelf.get()?.showAlert(getString(R.string.alert_signup_error_message),
                                                getString(R.string.alert_signup_error_title))
                  })
    }
}
