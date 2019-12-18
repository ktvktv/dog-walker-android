package com.example.dogwalker.view

import android.content.Context
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
import androidx.navigation.findNavController
import androidx.navigation.fragment.findNavController
import com.example.dogwalker.LOGIN_SUCCESSFUL
import com.example.dogwalker.R
import com.example.dogwalker.data.CommonResponse
import com.example.dogwalker.data.LoginRequest
import com.example.dogwalker.databinding.FragmentLoginBinding
import com.example.dogwalker.network.DogWalkerServiceApi
import com.example.dogwalker.viewmodel.LoginViewModel
import com.example.dogwalker.viewmodel.ViewModelFactory
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.launch

class LoginFragment : Fragment() {

    private lateinit var binding: FragmentLoginBinding
    private val coroutineScope = CoroutineScope(Job() + Dispatchers.Main)
    private val loginViewModel by lazy {
        ViewModelProviders.of(this, ViewModelFactory()).get(LoginViewModel::class.java)
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentLoginBinding.inflate(inflater)

        binding.loginButton.setOnClickListener {
            val phoneNumber = binding.phoneNumber.text.toString().trim()
            val password = binding.passwordEditText.text.toString().trim()
            if(phoneNumber == "" || password == "") {
                Toast.makeText(context, "Phone number and password must be filled", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }

            coroutineScope.launch {
                loginViewModel.checkCredential(LoginRequest(
                    phoneNumber, password
                ))
            }
        }

        loginViewModel.isLoginSuccess.observe(this, Observer{
            if(it) {
                val userCache = loginViewModel.getLoginResponse()

                val sharedPreferences = context?.getSharedPreferences(getString(R.string.preferences_file_key), Context.MODE_PRIVATE)

                with (sharedPreferences!!.edit()) {
                    putString(getString(R.string.session_cache), userCache?.session)
                    putString(getString(R.string.name_cache), userCache?.body?.name)
                    putString(getString(R.string.type_cache), userCache?.body?.type)
                    apply()
                }

                findNavController().navigate(R.id.action_loginFragment_to_dashboardActivity)
            } else {
                Toast.makeText(context, loginViewModel.getLoginMessage(), Toast.LENGTH_SHORT).show()
            }
        })

        binding.registerButton.setOnClickListener {
            it.findNavController().navigate(R.id.action_loginFragment_to_registerFragment)
        }

        return binding.root
    }
}