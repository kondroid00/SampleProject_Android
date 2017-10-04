package com.kondroid.sampleproject.activity

import android.databinding.DataBindingUtil
import android.os.Bundle
import android.support.v7.widget.LinearLayoutManager
import com.kondroid.sampleproject.R
import com.kondroid.sampleproject.databinding.ActivityHomeBinding
import com.kondroid.sampleproject.view.adapter.RoomListAdapter
import com.kondroid.sampleproject.viewmodel.HomeViewModel
import kotlinx.android.synthetic.main.activity_home.*

class HomeActivity : BaseActivity() {
    private lateinit var roomListAdapter: RoomListAdapter
    private lateinit var vm: HomeViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        var binding = DataBindingUtil.setContentView<ActivityHomeBinding>(this, R.layout.activity_home)
        vm = HomeViewModel()
        binding.vm = vm

        setUpRecyclerView()

        fetchRooms()
    }

    private fun setUpRecyclerView() {
        val recyclerView = homeRecyclerView
        recyclerView.setHasFixedSize(true)
        recyclerView.layoutManager = LinearLayoutManager(this)
        roomListAdapter = RoomListAdapter()
        recyclerView.adapter = roomListAdapter
    }

    private fun fetchRooms() {
        vm.fetchRooms({
                          roomListAdapter.setRooms(vm.rooms)
                      },
                      {e ->

                      })
    }

}
