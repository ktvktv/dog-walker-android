package com.example.dogwalker.view

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.Observer
import androidx.navigation.findNavController
import androidx.navigation.fragment.findNavController
import com.example.dogwalker.LOGIN_SUCCESSFUL
import com.example.dogwalker.R
import com.example.dogwalker.data.CommonResponse
import com.example.dogwalker.data.Login
import com.example.dogwalker.databinding.FragmentLoginBinding
import com.example.dogwalker.network.DogWalkerServiceApi
import com.example.dogwalker.network.retrofit
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.launch

class LoginFragment : Fragment() {

    private var isLogin: MutableLiveData<Boolean> = MutableLiveData(false)

    private lateinit var binding: FragmentLoginBinding

    private var errorMessage :String? = ""

    private val CHECK_CREDENTIALS = "checkCredential"

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentLoginBinding.inflate(inflater)

        val job = Job()
        val coroutineScope = CoroutineScope(job + Dispatchers.Main)

        binding.loginButton.setOnClickListener {
            coroutineScope.launch {
                checkCredential()
            }
        }

        isLogin.observe(this, Observer{
            if(it) {
                findNavController().navigate(R.id.action_loginFragment_to_dashboardActivity)
            } else {
                Toast.makeText(context, errorMessage, Toast.LENGTH_SHORT).show()
            }
        })

        binding.registerButton.setOnClickListener {
            it.findNavController().navigate(R.id.action_loginFragment_to_registerFragment)
        }

        return binding.root
    }

    private suspend fun checkCredential() {
        val loginData = Login(
            phoneNumber = binding.phoneNumber.text.toString(),
            password = binding.passwordEditText.text.toString()
        )

        var result: CommonResponse? = null
        try {
            result = DogWalkerServiceApi.DogWalkerService.login(loginData)?.await()
        } catch (e: Exception) {
            Log.e(CHECK_CREDENTIALS, "Error when hit login API, err: " + e.message)
            isLogin.value = false
            return
        }

        Log.i(CHECK_CREDENTIALS, "Result value: ${result}}")
        if (result != null) {
            if (result.message.equals(LOGIN_SUCCESSFUL)) {
                isLogin.value = true
                return
            } else {
                errorMessage = result.message
            }
        }

        isLogin.value = false
    }
}