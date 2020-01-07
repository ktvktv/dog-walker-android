package com.example.dogwalker.view

import android.content.Context
import android.os.Bundle
import android.util.Log
import android.view.KeyEvent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import androidx.navigation.findNavController
import androidx.navigation.fragment.findNavController
import com.example.dogwalker.R
import com.example.dogwalker.data.LoginRequest
import com.example.dogwalker.databinding.FragmentLoginBinding
import com.example.dogwalker.viewmodel.LoginViewModel
import com.example.dogwalker.viewmodel.ViewModelFactory
import com.google.android.gms.tasks.OnCompleteListener
import com.google.firebase.iid.FirebaseInstanceId
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.launch

class LoginFragment : Fragment() {
    private val TAG = LoginFragment::class.java.simpleName
    private lateinit var binding: FragmentLoginBinding
    private val coroutineScope = CoroutineScope(Job() + Dispatchers.Main)
    private val loginViewModel by lazy {
        ViewModelProviders.of(this, ViewModelFactory()).get(LoginViewModel::class.java)
    }
    private var token = ""

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentLoginBinding.inflate(inflater)

        FirebaseInstanceId.getInstance().instanceId
            .addOnCompleteListener(OnCompleteListener { task ->
                if (!task.isSuccessful) {
                    Log.w(TAG, "getInstanceId failed", task.exception)
                    return@OnCompleteListener
                }

                // Get new Instance ID token
                if(task.result != null) {
                    token = task.result!!.token
                }
            })

        binding.passwordEditText.setOnKeyListener { v, keyCode, event ->
            if(event.action == KeyEvent.ACTION_DOWN) {
                when(keyCode) {
                    KeyEvent.KEYCODE_ENTER -> {
                        doLogin()
                        true
                    }
                    else -> false
                }
            } else false
        }

        binding.loginButton.setOnClickListener {
            doLogin()
        }

        loginViewModel.isLoginSuccess.observe(this, Observer{
            if(it) {
                val userCache = loginViewModel.getLoginResponse()

                val sharedPreferences = context?.getSharedPreferences(getString(R.string.preferences_file_key), Context.MODE_PRIVATE)

                with (sharedPreferences!!.edit()) {
                    putString(getString(R.string.session_cache), userCache?.session)
                    putString(getString(R.string.name_cache), userCache?.body?.name)
                    putString(getString(R.string.type_cache), userCache?.body?.type)
                    putString(getString(R.string.phone_number_cache), userCache?.body?.phoneNumber)
                    putString(getString(R.string.address_cache), userCache?.body?.address)
                    apply()
                }

                val action = LoginFragmentDirections.actionLoginFragmentToDashboardActivity()
                findNavController().navigate(action)
            } else {
                Toast.makeText(context, loginViewModel.getLoginMessage(), Toast.LENGTH_SHORT).show()
            }
        })

        binding.registerButton.setOnClickListener {
            it.findNavController().navigate(R.id.action_loginFragment_to_registerFragment)
        }

        return binding.root
    }

    fun doLogin() {
        val phoneNumber = binding.phoneNumber.text.toString().trim()
        val password = binding.passwordEditText.text.toString().trim()
        if(phoneNumber == "" || password == "") {
            Toast.makeText(context, "Phone number and password must be filled", Toast.LENGTH_SHORT).show()
            return
        }

        Log.d(TAG, "Here's token after $token")

        coroutineScope.launch {
            loginViewModel.checkCredential(LoginRequest(
                phoneNumber, password, token
            ))
        }
    }
}