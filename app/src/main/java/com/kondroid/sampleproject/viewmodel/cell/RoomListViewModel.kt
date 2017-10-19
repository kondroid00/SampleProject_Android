package com.kondroid.sampleproject.viewmodel.cell

import android.databinding.ObservableField
import com.kondroid.sampleproject.dto.RoomDto

/**
 * Created by kondo on 2017/10/04.
 */

class RoomListViewModel {
    val nameText: ObservableField<String> = ObservableField("")
    val themeText: ObservableField<String> = ObservableField("")

    private var roomId: String? = null
    lateinit var selectCallback: (String) -> Unit

    fun loadItem(data: RoomDto) {
        roomId = data.id
        nameText.set(data.name?.let {it} ?: "")
        themeText.set(data.theme?.let {it} ?: "")
    }

    fun selectItem() {
        if (roomId != null) {
            selectCallback(roomId!!)
        }
    }
}