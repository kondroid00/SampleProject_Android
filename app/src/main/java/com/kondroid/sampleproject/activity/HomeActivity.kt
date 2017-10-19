package com.kondroid.sampleproject.activity

import android.databinding.DataBindingUtil
import android.os.Bundle
import android.support.v7.widget.DividerItemDecoration
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.helper.ItemTouchHelper
import android.view.KeyEvent
import android.view.Menu
import android.view.MenuItem
import com.kondroid.sampleproject.R
import com.kondroid.sampleproject.databinding.ActivityHomeBinding
import com.kondroid.sampleproject.dto.RoomDto
import com.kondroid.sampleproject.helper.makeWeak
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

        vm.initVM()

        setTitle(R.string.title_home)

        setUpRecyclerView()
    }

    override fun onResume() {
        super.onResume()
        fetchRooms()
    }

    override fun onStop() {
        super.onStop()
        vm.release()
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.home_menu, menu)
        return super.onCreateOptionsMenu(menu)
    }

    override fun onOptionsItemSelected(item: MenuItem?): Boolean {
        val weakSelf = makeWeak(this)
        item?.let {
            when (item.itemId) {
                R.id.menu_room_add -> {
                    weakSelf.get()?.goToAddRoom()
                }
                else -> {}
            }
        }
        return super.onOptionsItemSelected(item)
    }

    override fun dispatchKeyEvent(event: KeyEvent?): Boolean {
        // バックボタン無効化
        event?.let { if (event.keyCode == KeyEvent.KEYCODE_BACK) return true }
        return super.dispatchKeyEvent(event)
    }

    private fun setUpRecyclerView() {
        val recyclerView = homeRecyclerView
        recyclerView.setHasFixedSize(true)

        val layoutManager = LinearLayoutManager(this)
        recyclerView.layoutManager = layoutManager

        val dividerItemDecoration = DividerItemDecoration(recyclerView.context, layoutManager.orientation)
        recyclerView.addItemDecoration(dividerItemDecoration)

        roomListAdapter = RoomListAdapter()
        val weakSelf = makeWeak(this)
        roomListAdapter.selectCallback = {room ->
            weakSelf.get()?.goToChat(room)
        }
        recyclerView.adapter = roomListAdapter
    }

    private fun fetchRooms() {
        val weakSelf = makeWeak(this)
        vm.fetchRooms({
                          weakSelf.get()?.roomListAdapter?.setRooms(vm.rooms)
                      },
                      {e ->

                      })
    }

    private fun goToAddRoom() {
        startActivity<AddRoomActivity>()
    }

    private fun goToChat(room: RoomDto) {
        if (room.id != null && room.name != null) {
            startActivity<ChatActivity>("roomId" to room.id, "roomName" to room.name)
        }
    }
}
