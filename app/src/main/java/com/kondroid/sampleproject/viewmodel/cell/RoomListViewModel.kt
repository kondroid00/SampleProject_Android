package com.kondroid.sampleproject.viewmodel.cell

import android.databinding.ObservableField
import com.kondroid.sampleproject.dto.RoomDto

/**
 * Created by kondo on 2017/10/04.
 */

class RoomListViewModel {
    val nameText: ObservableField<String> = ObservableField("")
    val themeText: ObservableField<String> = ObservableField("")

    fun loadItem(data: RoomDto) {
        nameText.set(data.name?.let {it} ?: "")
        themeText.set(data.theme?.let {it} ?: "")
    }
}