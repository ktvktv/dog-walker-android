package com.example.dogwalker.view

import android.content.Context
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.net.toUri
import androidx.fragment.app.Fragment
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import com.bumptech.glide.Glide
import com.example.dogwalker.R
import com.example.dogwalker.SUCCESSFUL
import com.example.dogwalker.data.Dog
import com.example.dogwalker.databinding.FragmentDogUpdateBinding
import com.example.dogwalker.viewmodel.DogUpdateViewModel
import com.example.dogwalker.viewmodel.ViewModelFactory
import kotlinx.android.synthetic.main.comment_item.view.*
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.launch

class DogUpdateFragment : Fragment() {
    private lateinit var binding: FragmentDogUpdateBinding
    private lateinit var listDog: List<Dog>

    private val dogUpdateViewModel by lazy {
        ViewModelProviders.of(this, ViewModelFactory()).get(DogUpdateViewModel::class.java)
    }
    private val coroutineScope = CoroutineScope(Job() + Dispatchers.Main)
    private val TAG = DogUpdateFragment::class.java.simpleName

    private var currentPage = MutableLiveData<Int>()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentDogUpdateBinding.inflate(inflater)

        val session = activity!!.getSharedPreferences(getString(R.string.preferences_file_key), Context.MODE_PRIVATE)
            .getString(getString(R.string.session_cache), "")

        coroutineScope.launch {
            dogUpdateViewModel.getListDog(session)
        }

        dogUpdateViewModel.listDogResponse.observe(this, Observer {
            if(it != null && it.message == SUCCESSFUL && it.body != null) {
                listDog = it.body
                currentPage.value = 0
            }
        })

        currentPage.observe(this, Observer {
            when (it) {
                0 -> {
                    binding.previousButton.visibility = View.GONE
                    binding.nextButton.visibility = View.VISIBLE
                }
                (listDog.size-1) -> {
                    binding.nextButton.visibility = View.GONE
                    binding.previousButton.visibility = View.VISIBLE
                }
                else -> {
                    binding.nextButton.visibility = View.VISIBLE
                    binding.previousButton.visibility = View.VISIBLE
                }
            }

            setData()
        })

        binding.nextButton.setOnClickListener {
            Log.d(TAG, "current page now: ${currentPage.value}")
            currentPage.value = currentPage.value as Int + 1
            Log.d(TAG, "current page now: ${currentPage.value}")
        }

        binding.previousButton.setOnClickListener {
            currentPage.value = currentPage.value as Int - 1
        }

        return binding.root
    }

    private fun setData() {
        Log.d(TAG, "current page now: ${currentPage.value}")
        val currentPage = currentPage.value as Int
        binding.dog = listDog[currentPage]

        if(listDog[currentPage].photo != null && listDog[currentPage].photo != "") {
            val imgUri = listDog[currentPage].photo!!.toUri().buildUpon().scheme("https").build()
            val imageView = binding.dogPhoto

            Glide.with(imageView.context)
                .load(imgUri)
                .into(imageView)
        }

        if(listDog[currentPage].gender.toLowerCase() == "male") {
            binding.maleRadio.isChecked = true
            binding.femaleRadio.isChecked = false
        } else {
            binding.maleRadio.isChecked = false
            binding.femaleRadio.isChecked = true
        }

        val age = listDog[currentPage].age

        binding.yearAge.setText("${age/12}")
        binding.monthAge.setText("${age%12}")
    }
}