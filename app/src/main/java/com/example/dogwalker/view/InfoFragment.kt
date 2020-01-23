package com.example.dogwalker.view

import android.Manifest
import android.app.Activity
import android.app.AlertDialog
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.provider.MediaStore
import android.util.Log
import androidx.fragment.app.Fragment
import com.example.dogwalker.databinding.FragmentInfoBinding
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import java.io.File
import androidx.core.app.ActivityCompat
import android.content.pm.PackageManager
import android.graphics.drawable.Drawable
import android.view.*
import android.view.animation.Animation
import android.widget.Toast
import androidx.core.content.ContextCompat
import androidx.core.net.toUri
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import androidx.recyclerview.widget.LinearLayoutManager
import com.bumptech.glide.Glide
import com.bumptech.glide.load.engine.DiskCacheStrategy
import com.bumptech.glide.request.RequestOptions
import com.example.dogwalker.*
import com.example.dogwalker.adapter.InfoAdapter
import com.example.dogwalker.viewmodel.InfoViewModel
import com.example.dogwalker.viewmodel.ViewModelFactory
import kotlinx.coroutines.launch

class InfoFragment : Fragment() {

    private val TAG = InfoFragment::class.java.simpleName
    private lateinit var infoAdapter: InfoAdapter

    private lateinit var binding: FragmentInfoBinding
    private val infoViewModel: InfoViewModel by lazy {
        ViewModelProviders.of(this, ViewModelFactory()).get(InfoViewModel::class.java)
    }
    private val counts = MutableLiveData(0)
    private val coroutineScope = CoroutineScope(Job() + Dispatchers.Main)

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentInfoBinding.inflate(inflater)

        setHasOptionsMenu(true)

        //If in any case context is null, then don't set adapter to the recycler view, it'll make the app crash.
        val mContext = context
        if(mContext != null) {
            binding.infoRecyclerView.layoutManager =
                LinearLayoutManager(context, LinearLayoutManager.HORIZONTAL, false)
            infoAdapter = InfoAdapter(listOf(), mContext)
            binding.infoRecyclerView.adapter = infoAdapter
        }

        //Get user and its dog data
        val sharedPreferences = context!!.getSharedPreferences(getString
            (R.string.preferences_file_key), Context.MODE_PRIVATE)
        val session = sharedPreferences.getString(getString(R.string.session_cache), "")

        //Go to add dog page.
        binding.addDogImage.setOnClickListener {
            val intent = Intent(context, RegisterDogActivity::class.java)

            startActivity(intent)
        }

        //Go to register page.
        binding.registerWalkerButton.setOnClickListener {
            val intent = Intent(context, RegisterWalkerActivity::class.java)

            startActivity(intent)
        }

        binding.updateButton.setOnClickListener {
            val intent = Intent(context, UpdateInfoActivity::class.java)

            startActivity(intent)
        }

        //Change user type.
        binding.changeInfoButton.setOnClickListener{
            val intent = Intent(context, WalkerDashboardActivity::class.java)
            intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK

            with (sharedPreferences.edit()) {
                putString(getString(R.string.type_cache), "Walker")
                apply()
            }

            startActivity(intent)
        }

        //Observing dog data
        infoViewModel.dogResponse.observe(this, Observer {
            val dogData = it.body

            if(dogData == null) {
                Toast.makeText(context, "Sinyal rusak, mohon dicoba kembali", Toast.LENGTH_SHORT).show()
                return@Observer
            }

            counts.value = counts.value?.plus(1)

            infoAdapter.listData = dogData
            infoAdapter.notifyDataSetChanged()
        })

        //Observing customer data
        infoViewModel.infoResponse.observe(this, Observer {
            if(it != null && it.message == SUCCESSFUL) {
                val userData = it.body

                if(userData == null) {
                    Toast.makeText(context, "Sinyal rusak, mohon dicoba kembali", Toast.LENGTH_SHORT).show()
                    return@Observer
                }

                counts.value = counts.value?.plus(1)

                binding.user = userData
                binding.notifyChange()

                if(userData.gender.toLowerCase().trim() == "male") {
                    binding.genderInfoImage.setImageResource(R.drawable.male_icon)
                } else {
                    binding.genderInfoImage.setImageResource(R.drawable.female_icon)
                }

                if(userData.isWalker) {
                    binding.registerWalkerButton.visibility = View.GONE
                    binding.walkerRegistrationText.visibility = View.GONE
                    binding.changeInfoButton.visibility = View.VISIBLE
                } else {
                    binding.registerWalkerButton.visibility = View.VISIBLE
                    binding.walkerRegistrationText.visibility = View.VISIBLE
                    binding.changeInfoButton.visibility = View.GONE
                }

                //If user image is empty then don't do anything.
                Log.d(TAG, "UserImageURL: ${userData.userImageUrl}")
                if(userData.userImageUrl != null && userData.userImageUrl != "") {
                    val imgUri = userData.userImageUrl.toUri().buildUpon().scheme("https").build()
                    val imageView = binding.userPicture

                    Glide.with(imageView.context)
                        .load(imgUri)
                        .apply(RequestOptions.skipMemoryCacheOf(true))
                        .apply(RequestOptions.diskCacheStrategyOf(DiskCacheStrategy.NONE))
                        .into(imageView)
                }
            }
        })

        counts.observe(this, Observer {
            Log.d(TAG, "COUNT: $it")
            if((it % 2) == 0 && it != 0) {
                binding.progressBar.visibility = View.GONE
                binding.constraintInfo.alpha = 1f
            } else {
                binding.progressBar.visibility = View.VISIBLE
                binding.constraintInfo.alpha = 0.5f
            }
        })

        return binding.root
    }

    override fun onResume() {
        val session = context!!.getSharedPreferences(getString
            (R.string.preferences_file_key), Context.MODE_PRIVATE)
            .getString(getString(R.string.session_cache), "")

        coroutineScope.launch {
            infoViewModel.getInformation(session)
        }

        super.onResume()
    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        inflater.inflate(R.menu.menu_item, menu)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        if(item.itemId == R.id.logout) {
            val intent = Intent(context, MainActivity::class.java)
            intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK

            context?.deleteSharedPreferences(getString(R.string.preferences_file_key))

            startActivity(intent)
        }
        return super.onOptionsItemSelected(item)
    }
}