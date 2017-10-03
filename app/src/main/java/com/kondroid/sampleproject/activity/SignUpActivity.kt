package com.kondroid.sampleproject.activity

import android.databinding.DataBindingUtil
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import com.kondroid.sampleproject.R
import com.kondroid.sampleproject.databinding.ActivitySignupBinding
import com.kondroid.sampleproject.viewmodel.SignUpViewModel

class SignUpActivity : AppCompatActivity() {
    private lateinit var vm: SignUpViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        var binding = DataBindingUtil.setContentView<ActivitySignupBinding>(this, R.layout.activity_signup)
        vm = SignUpViewModel()
        binding.vm = vm


    }
}
