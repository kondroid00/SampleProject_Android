package com.kondroid.sampleproject.activity

import android.databinding.DataBindingUtil
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import com.kondroid.sampleproject.R
import com.kondroid.sampleproject.databinding.ActivityAddRoomBinding
import com.kondroid.sampleproject.viewmodel.AddRoomViewModel

class AddRoomActivity : BaseActivity() {
    private lateinit var vm: AddRoomViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        var binding = DataBindingUtil.setContentView<ActivityAddRoomBinding>(this, R.layout.activity_add_room)
        vm = AddRoomViewModel(this)
        binding.vm = vm

        vm.initVM()

        setUpCallback()

        setTitle(R.string.title_addroom)
    }

    override fun onStop() {
        super.onStop()
        vm.release()
    }

    private fun setUpCallback() {
        vm.onTapCreate = {
            createRoom()
        }
    }

    private fun createRoom() {
        vm.createRoom({
                          showAlert(getString(R.string.alert_room_create_success_message),
                                    getString(R.string.alert_room_create_success_title))
                      },
                      {e ->
                          showAlert(getString(R.string.alert_room_create_error_message),
                                    getString(R.string.alert_room_create_error_title))
                      })
    }
}
