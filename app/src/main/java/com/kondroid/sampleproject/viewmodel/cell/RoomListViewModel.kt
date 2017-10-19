package com.kondroid.sampleproject.viewmodel.cell

import android.databinding.ObservableField
import com.kondroid.sampleproject.dto.RoomDto

/**
 * Created by kondo on 2017/10/04.
 */

class RoomListViewModel {
    val nameText: ObservableField<String> = ObservableField("")
    val themeText: ObservableField<String> = ObservableField("")

    private lateinit var room: RoomDto
    lateinit var selectCallback: (RoomDto) -> Unit

    fun loadItem(data: RoomDto) {
        room = data
        nameText.set(data.name?.let {it} ?: "")
        themeText.set(data.theme?.let {it} ?: "")
    }

    fun selectItem() {
        selectCallback(room)
    }
}