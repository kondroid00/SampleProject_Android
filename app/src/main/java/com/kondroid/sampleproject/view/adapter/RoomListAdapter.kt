package com.kondroid.sampleproject.view.adapter

import android.databinding.DataBindingUtil
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.kondroid.sampleproject.R
import com.kondroid.sampleproject.databinding.CellRoomListviewBinding
import com.kondroid.sampleproject.dto.RoomDto
import com.kondroid.sampleproject.viewmodel.cell.RoomListViewModel

/**
 * Created by kondo on 2017/10/04.
 */

class RoomListAdapter() : RecyclerView.Adapter<RoomListAdapter.ListViewHolder>() {
    private var rooms: List<RoomDto> = mutableListOf()
    lateinit var selectCallback: (RoomDto) -> Unit

    fun setRooms(rooms: List<RoomDto>) {
        this.rooms = rooms
        notifyDataSetChanged()
    }

    override fun onCreateViewHolder(parent: ViewGroup?, viewType: Int): ListViewHolder {
        val binding = DataBindingUtil.inflate<CellRoomListviewBinding>(LayoutInflater.from(parent!!.context), R.layout.cell_room_listview, parent, false)
        val viewModel = RoomListViewModel()
        viewModel.selectCallback = selectCallback
        binding.vm = viewModel
        return ListViewHolder(binding.root, viewModel)
    }

    override fun onBindViewHolder(holder: ListViewHolder?, position: Int) {
        holder?.loadItem(rooms[position])
    }

    override fun getItemCount(): Int {
        return rooms.size
    }


    class ListViewHolder(view: View, private val vm: RoomListViewModel) : RecyclerView.ViewHolder(view) {

        fun loadItem(data: RoomDto) {
            vm.loadItem(data)
        }
    }
}