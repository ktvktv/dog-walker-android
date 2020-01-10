package com.example.dogwalker.view

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.MenuItem
import android.view.View
import android.view.ViewGroup
import androidx.core.net.toUri
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import com.bumptech.glide.Glide
import com.example.dogwalker.DashboardActivity
import com.example.dogwalker.R
import com.example.dogwalker.databinding.FragmentWalkerInfoBinding
import com.example.dogwalker.viewmodel.ViewModelFactory
import com.example.dogwalker.viewmodel.WalkerInfoViewModel
import kotlinx.android.synthetic.main.order_item.view.*
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.launch

class WalkerInfoFragment : Fragment() {

    private val TAG = WalkerInfoFragment::class.java.simpleName
    private lateinit var binding: FragmentWalkerInfoBinding
    private val walkerInfoViewModel by lazy {
        ViewModelProviders.of(this, ViewModelFactory()).get(WalkerInfoViewModel::class.java)
    }
    private val coroutineScope = CoroutineScope(Job() + Dispatchers.Main)

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentWalkerInfoBinding.inflate(inflater)

        val walkerId = arguments!!.getInt("walkerId")

        val sharedPreferences = context!!.getSharedPreferences(getString(R.string.preferences_file_key), Context.MODE_PRIVATE)
        val session = sharedPreferences.getString(getString(R.string.session_cache), "")

        coroutineScope.launch {
            var walkerIdString = walkerId.toString()
            if(walkerId == -1) {
                walkerIdString = ""
            }
            walkerInfoViewModel.getWalkerData(session, walkerIdString)
        }

        if(walkerId == -1) {
            binding.changeRoleWalkerButton.setOnClickListener {
                val intent = Intent(context, DashboardActivity::class.java)
                intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
                //TODO:Call API to update the type cache

                with(sharedPreferences.edit()) {
                    putString(getString(R.string.type_cache), "Customer")
                    apply()
                }

                startActivity(intent)
            }

            binding.changeRoleWalkerButton.visibility = View.VISIBLE
        } else {
            setHasOptionsMenu(true)
            binding.changeRoleWalkerButton.visibility = View.GONE
        }

        walkerInfoViewModel.walkerInfoData.observe(this, Observer {
            if(it != null) {
                binding.walker = it
                binding.notifyChange()

                if(it.photo != null && it.photo != "") {
                    val imgUri = it.photo.toUri().buildUpon().scheme("https").build()
                    val imageView = binding.profileWalker

                    Glide.with(imageView.context)
                        .load(imgUri)
                        .into(imageView)
                }

                val rating = if(it.raters != 0) {
                    it.rating/it.raters
                } else 0

                if(!it.isVerified) {
                    binding.verifiedPicture.visibility = View.GONE
                } else {
                    binding.verifiedPicture.visibility = View.VISIBLE
                }

                if(!it.isRecommended) {
                    binding.recommendedPicture.visibility = View.GONE
                 } else {
                    binding.recommendedPicture.visibility = View.VISIBLE
                }

                if(it.gender.toLowerCase().trim() == "male") {
                    binding.genderWalkerPicture.setImageResource(R.drawable.male_icon)
                } else {
                    binding.genderWalkerPicture.setImageResource(R.drawable.female_icon)
                }

                binding.maxDistanceWalkerText.text = "Max Distance: ${it.travelDistance} km"
                binding.maxDogWeightWalkerText.text = "Max Dog Weight: ${it.maxDogSize} kg"
                binding.maxDurationWalkerText.text = "Max Duration: ${it.walkDuration} hour"
                binding.pricingWalkerText.text = "Pricing: Rp. ${it.pricing}/hr"

                binding.ratingWalker.rating = rating.toFloat()
            }
        })

        return binding.root
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        if(item.itemId == android.R.id.home) {
            val action = WalkerInfoFragmentDirections.actionWalkerInfoFragmentToWalkerOrderFragment()

            findNavController().navigate(action)
        }

        return super.onOptionsItemSelected(item)
    }
}