package com.kondroid.sampleproject.activity

import android.databinding.DataBindingUtil
import android.support.v7.app.AppCompatActivity
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

        setTitle(R.string.title_signup)

        setUpSignUp()
    }

    private fun setUpSignUp() {
        vm.signUpOnSuccess = {
            signUpSuccess()
        }
        vm.signUpOnFailed = { e ->
            signUpFailed(e)
        }
    }

    private fun signUpSuccess() {
        goToHome()
    }

    private fun signUpFailed(e: Throwable) {
        showAlert(getString(R.string.error_signup_message),
                  getString(R.string.error_signup_title))
    }
}
