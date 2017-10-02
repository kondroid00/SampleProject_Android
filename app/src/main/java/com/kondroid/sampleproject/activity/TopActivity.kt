package com.kondroid.sampleproject.activity

import android.databinding.DataBindingUtil
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import com.kondroid.sampleproject.R
import com.kondroid.sampleproject.auth.AccountManager
import com.kondroid.sampleproject.databinding.ActivityTopBinding
import com.kondroid.sampleproject.viewmodel.TopViewModel
import java.lang.ref.WeakReference

class TopActivity : BaseActivity() {
    private lateinit var vm: TopViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        var binding = DataBindingUtil.setContentView<ActivityTopBinding>(this, R.layout.activity_top)
        vm = TopViewModel()
        binding.viewModel = vm

        supportActionBar?.hide()

        if(AccountManager.hasUserId()) {
            vm.startButtonVisibility.set(View.VISIBLE)
            vm.login()
        }

        val weakRef = WeakReference<TopActivity>(this)
        vm.onTapStart = {
            if (weakRef != null) {
                goToSignUp()
            }
        }
    }

    fun goToSignUp() {
        startActivity<SignUpActivity>(this)
    }

}
