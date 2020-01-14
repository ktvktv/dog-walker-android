package com.example.dogwalker.view

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.core.net.toUri
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import com.bumptech.glide.Glide
import com.example.dogwalker.R
import com.example.dogwalker.SUCCESSFUL
import com.example.dogwalker.data.User
import com.example.dogwalker.databinding.FragmentUserUpdateBinding
import com.example.dogwalker.viewmodel.UserUpdateViewModel
import com.example.dogwalker.viewmodel.ViewModelFactory
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.launch
import java.lang.Exception

class UserUpdateFragment : Fragment() {

    private val coroutineScope = CoroutineScope(Job() + Dispatchers.Main)
    private val userUpdateViewModel by lazy {
        ViewModelProviders.of(this, ViewModelFactory()).get(UserUpdateViewModel::class.java)
    }
    private lateinit var binding: FragmentUserUpdateBinding

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentUserUpdateBinding.inflate(inflater)

        val session = context!!.getSharedPreferences(getString(R.string.preferences_file_key), Context.MODE_PRIVATE)
            .getString(getString(R.string.session_cache), "")

        //Get the data
        coroutineScope.launch {
            userUpdateViewModel.getUserInfo(session)
        }

        binding.birthdateText.setOnClickListener {
            val calendarDialog = DateFragment(binding.birthdateText)

            calendarDialog.show(activity!!.supportFragmentManager, null)
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

        binding.button.setOnClickListener {
            if(checkValidity() != "") {
                return@setOnClickListener
            }

            coroutineScope.launch {
                val gender = if(binding.maleRadio.isChecked) "male" else "female"
                userUpdateViewModel.updateUserInfo(session, User(
                    id = -1,
                    email = binding.emailText.text.toString(),
                    phoneNumber = binding.phonenumberText.text.toString(),
                    password = binding.passwordText.text.toString(),
                    name = binding.nameText.text.toString(),
                    nik = binding.nikText.text.toString(),
                    gender = gender,
                    address = binding.addressText.text.toString(),
                    isWalker = true,
                    type = "",
                    birthDate = binding.birthdateText.text.toString(),
                    birthPlace = binding.birthplaceText.text.toString(),
                    userImageUrl = null
                ), null)
            }
        }

        userUpdateViewModel.updateResp.observe(this, Observer {
            if(it != null && it.message == SUCCESSFUL) {
                Toast.makeText(context, "Success update user information", Toast.LENGTH_SHORT).show()
                activity?.finish()
            }
        })

        return binding.root
    }

    fun checkValidity(): String {
        if(binding.nameText.text.toString().equals("")) {
            return "Name must be filled"
        }

        if(binding.emailText.text.toString().equals("")) {
            return "Email must be filled"
        }

        if(binding.phonenumberText.text.toString().equals("")) {
            return "Phone number must be filled"
        }

        if(binding.addressText.text.toString().equals("")) {
            return "Address must be filled"
        }

        if(binding.passwordText.text.toString().equals("")) {
            return "Password must be filled"
        }

        if(binding.birthdateText.text.toString().equals("")) {
            return "Birthdate must be filled"
        }

        if(binding.birthplaceText.text.toString().equals("")) {
            return "Birthplace must be filled"
        }

        if(!binding.maleRadio.isChecked && !binding.femaleRadio.isChecked) {
            return "Gender must be filled"
        }

//            if(binding.imageView.drawable == null) {
//                return "Image must be filled"
//            }

//            if(file == null) {
//                return "Please input dog photo"
//            }

        return ""
    }
}