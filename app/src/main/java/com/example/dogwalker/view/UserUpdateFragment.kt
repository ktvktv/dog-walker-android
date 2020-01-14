package com.example.dogwalker.view

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.net.toUri
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import com.bumptech.glide.Glide
import com.example.dogwalker.R
import com.example.dogwalker.SUCCESSFUL
import com.example.dogwalker.databinding.FragmentUserUpdateBinding
import com.example.dogwalker.viewmodel.UserUpdateViewModel
import com.example.dogwalker.viewmodel.ViewModelFactory
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.launch

class UserUpdateFragment : Fragment() {

    private val coroutineScope = CoroutineScope(Job() + Dispatchers.Main)
    private val userUpdateViewModel by lazy {
        ViewModelProviders.of(this, ViewModelFactory()).get(UserUpdateViewModel::class.java)
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val binding = FragmentUserUpdateBinding.inflate(inflater)

        val session = context!!.getSharedPreferences(getString(R.string.preferences_file_key), Context.MODE_PRIVATE)
            .getString(getString(R.string.session_cache), "")

        //Get the data
        coroutineScope.launch {
            userUpdateViewModel.getUserInfo(session)
        }

        userUpdateViewModel.user.observe(this, Observer {
            if(it != null && it.message == SUCCESSFUL) {
                binding.user = it.body

                if(it.body!!.gender.toLowerCase() == "male") {
                    binding.maleRadio.isChecked = true
                    binding.femaleRadio.isChecked = false
                } else {
                    binding.femaleRadio.isChecked = true
                    binding.maleRadio.isChecked = false
                }

                if(it.body.userImageUrl != null && it.body.userImageUrl != "") {
                    val imgUri = it.body.userImageUrl.toUri().buildUpon().scheme("https").build()
                    val imageView = binding.imageView

                    Glide.with(imageView.context)
                        .load(imgUri)
                        .into(imageView)
                }
            }
        })

        return binding.root
    }
}