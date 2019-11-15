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
import com.example.dogwalker.data.CommonResponse
import com.example.dogwalker.data.Register
import com.example.dogwalker.databinding.FragmentRegisterBinding
import com.example.dogwalker.network.DogWalkerServiceApi
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.launch
import java.text.SimpleDateFormat

class RegisterFragment : Fragment() {

    private lateinit var binding: FragmentRegisterBinding

    private val REGISTER = "register"

    private val isRegisterSuccess = MutableLiveData<Boolean>(false)
    private var errorMessage = ""

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentRegisterBinding.inflate(inflater)

        val job = Job()
        val coroutineScope = CoroutineScope(job + Dispatchers.Main)

        binding.registerButton.setOnClickListener {
            coroutineScope.launch {
                register()
            }
        }

        isRegisterSuccess.observe(this, Observer {
            if(it) {
                //Return to login page.
            } else {
                Toast.makeText(context, "Error: ${errorMessage}", Toast.LENGTH_SHORT).show()
            }
        })

        return binding.root
    }

    private suspend fun register() {
        val date = binding.birthDateCalendar.date
        val dates = SimpleDateFormat("dd-MM-yyyy").format(date)

        val gender = if(binding.maleRadio.isChecked) "Male" else "Female"
        val registerData = Register(
            phoneNumber = binding.phoneEditText.text.toString(),
            password =  binding.passwordEditText.text.toString(),
            name = binding.nameEditText.text.toString(),
            address = binding.addressEditText.text.toString(),
            gender = gender,
            dateOfBirth =  dates,
            placeOfBirth = binding.birthplaceEditText.text.toString()
        )

        Log.i(REGISTER, "Register Data: ${registerData}")

        var response: CommonResponse? = null

        try {
            response = DogWalkerServiceApi.DogWalkerService.register(registerData)?.await()
        } catch(e: Exception) {
            Log.e(REGISTER, "Error when hit register API, err: ${e.message}")
            isRegisterSuccess.value = false
            if(response != null) {
                errorMessage = response.message
            }
            return
        }

        if(response != null) {
            isRegisterSuccess.value = true
            return
        }

        isRegisterSuccess.value = false
    }
}