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
import com.google.android.gms.tasks.OnCompleteListener
import com.google.firebase.iid.FirebaseInstanceId
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.launch
import java.text.SimpleDateFormat
import java.util.*

class RegisterFragment : Fragment() {
    private val TAG = RegisterFragment::class.java.simpleName
    private lateinit var binding: FragmentRegisterBinding
    private val coroutineScope = CoroutineScope(Job() + Dispatchers.Main)
    private val registerViewModel by lazy {
        ViewModelProviders.of(this, ViewModelFactory()).get(RegisterViewModel::class.java)
    }
    private val DateTAG = "Date-Picker"

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentRegisterBinding.inflate(inflater)

        var token = ""

        FirebaseInstanceId.getInstance().instanceId.addOnCompleteListener {
            token = it.result?.token ?: ""
        }

        binding.registerButton.setOnClickListener {
            val date = binding.birthDateEditText.text.toString()
            if(date == "" || date.isEmpty()) {
                Toast.makeText(context, "Birthdate must not empty", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }

            val datez = date.split("/")
            val cld = Calendar.getInstance()
            cld.set(Calendar.YEAR, datez[2].toInt())
            cld.set(Calendar.MONTH, datez[1].toInt()-1)
            cld.set(Calendar.DAY_OF_MONTH, datez[0].toInt())
            val result = cld.time

            val dates = SimpleDateFormat("dd-MM-yyyy").format(result)
            Log.d(TAG, "THIS IS DATES: $dates")

            if(!binding.maleRadio.isChecked && !binding.femaleRadio.isChecked) {
                Toast.makeText(context, "Gender must be chosen", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }

            val gender = if(binding.maleRadio.isChecked) "Male" else "Female"

            val address = binding.addressEditText.text.toString()
            val email = binding.emailEditText.text.toString()
            val name = binding.nameEditText.text.toString()
            val nik = binding.nikEditText.text.toString()
            val password = binding.passwordEditText.text.toString()
            val phoneNumber = binding.phoneEditText.text.toString()
            val birthplace = binding.birthplaceEditText.text.toString()

            if(address.isEmpty() || email.isEmpty() || name.isEmpty() || nik.isEmpty() || password.isEmpty()
                || phoneNumber.isEmpty() || birthplace.isEmpty()) {
                Toast.makeText(context, "Field must not empty", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }

            if(nik.length != 16) {
                Toast.makeText(context, "NIK length must be 16", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }

            coroutineScope.launch {
                registerViewModel.register(Register(
                    address = address,
                    dateOfBirth = dates,
                    email = email,
                    gender = gender,
                    name = name,
                    nik = nik,
                    password = password,
                    phoneNumber = phoneNumber,
                    placeOfBirth = birthplace,
                    token = token
                ))
            }
        }

        binding.birthDateEditText.setOnClickListener{
            val dateFragment = DateFragment(binding.birthDateEditText)
            dateFragment.show(fragmentManager!!, DateTAG)
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