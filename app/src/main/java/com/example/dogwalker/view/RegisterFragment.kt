package com.example.dogwalker.view

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import androidx.navigation.fragment.findNavController
import com.example.dogwalker.R
import com.example.dogwalker.data.CommonResponse
import com.example.dogwalker.data.Register
import com.example.dogwalker.databinding.FragmentRegisterBinding
import com.example.dogwalker.network.DogWalkerServiceApi
import com.example.dogwalker.viewmodel.RegisterViewModel
import com.example.dogwalker.viewmodel.ViewModelFactory
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.launch
import java.text.SimpleDateFormat

class RegisterFragment : Fragment() {

    private lateinit var binding: FragmentRegisterBinding
    private val coroutineScope = CoroutineScope(Job() + Dispatchers.Main)
    private val registerViewModel by lazy {
        ViewModelProviders.of(this, ViewModelFactory()).get(RegisterViewModel::class.java)
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentRegisterBinding.inflate(inflater)

        binding.registerButton.setOnClickListener {
            coroutineScope.launch {
                val date = binding.birthDateCalendar.date
                val dates = SimpleDateFormat("dd-MM-yyyy").format(date)

                val gender = if(binding.maleRadio.isChecked) "Male" else "Female"

                registerViewModel.register(Register(
                    address = binding.addressEditText.text.toString(),
                    dateOfBirth = dates,
                    email = binding.emailEditText.text.toString(),
                    gender = gender,
                    name = binding.nameEditText.text.toString(),
                    nik = binding.nikEditText.text.toString(),
                    password = binding.passwordEditText.text.toString(),
                    phoneNumber = binding.phoneEditText.text.toString(),
                    placeOfBirth = binding.birthplaceEditText.text.toString()
                ))
            }
        }


        registerViewModel.isRegisterSuccess.observe(this, Observer {
            if(it) {
                Toast.makeText(context, "Success!", Toast.LENGTH_SHORT).show()
                findNavController().navigate(R.id.action_registerFragment_to_loginFragment)
            } else {
                var registerMessage = registerViewModel.getRegisterMessage()?.message
                if(registerMessage == null) {
                    registerMessage = "Unknown error, please try it again."
                }

                Toast.makeText(context, registerMessage, Toast.LENGTH_SHORT).show()
            }
        })

        return binding.root
    }
}