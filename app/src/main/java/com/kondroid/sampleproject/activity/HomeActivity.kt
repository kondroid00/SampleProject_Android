package com.kondroid.sampleproject.activity

import android.databinding.DataBindingUtil
import android.os.Bundle
import android.support.v7.widget.DividerItemDecoration
import android.support.v7.widget.LinearLayoutManager
import android.view.Menu
import android.view.MenuItem
import com.kondroid.sampleproject.R
import com.kondroid.sampleproject.databinding.ActivityHomeBinding
import com.kondroid.sampleproject.view.adapter.RoomListAdapter
import com.kondroid.sampleproject.viewmodel.HomeViewModel
import kotlinx.android.synthetic.main.activity_home.*
import org.jetbrains.anko.startActivity

class HomeActivity : BaseActivity() {
    private lateinit var roomListAdapter: RoomListAdapter
    private lateinit var vm: HomeViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        var binding = DataBindingUtil.setContentView<ActivityHomeBinding>(this, R.layout.activity_home)
        vm = HomeViewModel(this)
        binding.vm = vm

        setTitle(R.string.title_home)

        setUpRecyclerView()

        fetchRooms()
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.home_menu, menu)
        return super.onCreateOptionsMenu(menu)
    }

    override fun onOptionsItemSelected(item: MenuItem?): Boolean {
        item?.let {
            when (item.itemId) {
                R.id.menu_room_add -> {
                    goToAddRoom()
                }
            }
        }
        return super.onOptionsItemSelected(item)
    }

    private fun setUpRecyclerView() {
        val recyclerView = homeRecyclerView
        recyclerView.setHasFixedSize(true)

        val layoutManager = LinearLayoutManager(this)
        recyclerView.layoutManager = layoutManager

        val dividerItemDecoration = DividerItemDecoration(recyclerView.context, layoutManager.orientation)
        recyclerView.addItemDecoration(dividerItemDecoration)

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

    private fun goToAddRoom() {
        startActivity<AddRoomActivity>()
    }

}
