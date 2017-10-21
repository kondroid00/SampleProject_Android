package com.kondroid.sampleproject.view.adapter

import android.content.Context
import android.databinding.DataBindingUtil
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.kondroid.sampleproject.R
import com.kondroid.sampleproject.databinding.CellChatIncomingListviewBinding
import com.kondroid.sampleproject.databinding.CellChatOutgoingListviewBinding
import com.kondroid.sampleproject.dto.websocket.WebSocketMessageDto
import com.kondroid.sampleproject.viewmodel.cell.ChatListViewModel
import java.lang.ref.WeakReference

/**
 * Created by kondo on 2017/10/19.
 */

class ChatListAdapter(context: Context) : RecyclerView.Adapter<ChatListAdapter.ListViewHolder>() {
    private val OUTGOING = 1
    private val INCOMING = 2

    private val context: WeakReference<Context> = WeakReference(context)
    private var messages: List<WebSocketMessageDto> = mutableListOf()

    var clientNo: Int? = null
        set(value) { if (field == null) field = value }

    fun setMessages(messages: List<WebSocketMessageDto>) {
        this.messages = messages
        notifyDataSetChanged()
    }

    override fun onCreateViewHolder(parent: ViewGroup?, viewType: Int): ListViewHolder {
        if (context != null) {
            val vm = ChatListViewModel()
            when (viewType) {
                OUTGOING -> {
                    val binding = DataBindingUtil.inflate<CellChatOutgoingListviewBinding>(LayoutInflater.from(context.get()), R.layout.cell_chat_outgoing_listview, parent, false)
                    binding.vm = vm
                    return ListViewHolder(binding.root, vm)
                }
                INCOMING -> {
                    val binding = DataBindingUtil.inflate<CellChatIncomingListviewBinding>(LayoutInflater.from(context.get()), R.layout.cell_chat_incoming_listview, parent, false)
                    binding.vm = vm
                    return ListViewHolder(binding.root, vm)
                }
            }
        }
        return ListViewHolder(View(null), ChatListViewModel())
    }

    override fun onBindViewHolder(holder: ListViewHolder?, position: Int) {
        holder?.loadItem(messages[position])
    }

    override fun getItemCount(): Int {
        return messages.size
    }

    override fun getItemViewType(position: Int): Int {
        if (messages[position].clientNo == clientNo) {
            return OUTGOING
        }
        return INCOMING
    }

    class ListViewHolder(view: View, private val vm: ChatListViewModel) : RecyclerView.ViewHolder(view) {

        fun loadItem(data: WebSocketMessageDto) {
            vm.loadItem(data)
        }
    }
}