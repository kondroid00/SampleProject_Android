package com.kondroid.sampleproject.activity

import android.databinding.DataBindingUtil
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import com.kondroid.sampleproject.R
import com.kondroid.sampleproject.databinding.ActivityTopBinding
import com.kondroid.sampleproject.viewmodel.TopViewModel

class TopActivity : BaseActivity() {
    private lateinit var vm: TopViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        var binding = DataBindingUtil.setContentView<ActivityTopBinding>(this, R.layout.activity_top)
        vm = TopViewModel()
        binding.viewModel = vm

        supportActionBar?.hide()
    }

}
