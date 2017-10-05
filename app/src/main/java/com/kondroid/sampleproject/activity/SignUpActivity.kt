package com.kondroid.sampleproject.activity

import android.databinding.DataBindingUtil
import android.os.Bundle
import com.kondroid.sampleproject.R
import com.kondroid.sampleproject.databinding.ActivitySignupBinding
import com.kondroid.sampleproject.viewmodel.SignUpViewModel

class SignUpActivity : BaseActivity() {
    private lateinit var vm: SignUpViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        var binding = DataBindingUtil.setContentView<ActivitySignupBinding>(this, R.layout.activity_signup)
        vm = SignUpViewModel()
        binding.vm = vm

        setUpCallback()

        setTitle(R.string.title_signup)
    }

    private fun setUpCallback() {
        vm.onTapRegister = {
            signUp()
        }
    }

    private fun signUp() {
        vm.signUp({
                      goToHome()
                  },
                  {e ->
                      showAlert(getString(R.string.alert_signup_error_message),
                                getString(R.string.alert_signup_error_title))
                  })
    }
}
