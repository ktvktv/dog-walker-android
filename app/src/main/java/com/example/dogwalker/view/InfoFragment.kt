package com.example.dogwalker.view

import android.Manifest
import android.app.Activity
import android.app.AlertDialog
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.provider.MediaStore
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.example.dogwalker.data.CommonResponse
import com.example.dogwalker.databinding.FragmentInfoBinding
import com.example.dogwalker.network.DogWalkerServiceApi
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import okhttp3.MultipartBody
import okhttp3.RequestBody
import java.io.File
import java.lang.Exception
import androidx.core.app.ActivityCompat
import android.content.pm.PackageManager
import android.graphics.drawable.Drawable
import android.widget.Toast
import androidx.core.content.ContextCompat
import androidx.core.net.toUri
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import androidx.recyclerview.widget.LinearLayoutManager
import com.bumptech.glide.Glide
import com.example.dogwalker.*
import com.example.dogwalker.adapter.InfoAdapter
import com.example.dogwalker.data.User
import com.example.dogwalker.viewmodel.InfoViewModel
import com.example.dogwalker.viewmodel.ViewModelFactory
import kotlinx.coroutines.launch

class InfoFragment : Fragment() {

    private val TAG = InfoFragment::class.java.simpleName
    private lateinit var infoAdapter: InfoAdapter

    //Static Value
    companion object Constants {
        //Permission constants
        const val READ_STORAGE_PERMISSION = 100

        //Intent constants
        const val PHOTO_PICKER = 1

        val uploadImageFirstTimePermission = MutableLiveData<Boolean>()
    }

    private lateinit var pickImageIntent: Intent
    private lateinit var binding: FragmentInfoBinding
    private var alertDialog: AlertDialog? = null
    private val infoViewModel: InfoViewModel by lazy {
        ViewModelProviders.of(this, ViewModelFactory()).get(InfoViewModel::class.java)
    }
    private val coroutineScope = CoroutineScope(Job() + Dispatchers.Main)

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentInfoBinding.inflate(inflater)

        binding.userPicture.setOnClickListener {
            //Get the picture from gallery and set it.
            uploadImage()
        }

        //Get user and its dog data
        val session = context?.getSharedPreferences(getString
            (R.string.preferences_file_key), Context.MODE_PRIVATE)
            ?.getString(getString(R.string.session_cache), "")

        coroutineScope.launch {
            infoViewModel.getUserInformation(session!!)
            infoViewModel.getDogInformation(session!!)
        }

        //Go to add dog page.
        binding.addDogImage.setOnClickListener {
            val intent = Intent(context, RegisterDogActivity::class.java)

            startActivity(intent)
        }

        //Change user type
        binding.changeInfoButton.setOnClickListener{
            val intent = Intent(context, WalkerDashboard::class.java)
            intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK

            startActivity(intent)
        }

        binding.registerWalkerButton.setOnClickListener {
            val intent = Intent(context, RegisterWalkerActivity::class.java)

            startActivity(intent)
        }

        infoViewModel.dogResponse.observe(this, Observer {
            val dogData = it.body

            if(dogData == null) {
                Toast.makeText(context, "Network error, please refresh", Toast.LENGTH_SHORT).show()
                return@Observer
            }

            infoAdapter.listData = dogData
            infoAdapter.notifyDataSetChanged()
        })

        infoViewModel.infoResponse.observe(this, Observer {
            if(it != null && it.message == LOGIN_SUCCESSFUL) {
                val userData = it.body

                if(userData == null) {
                    Toast.makeText(context, "Network error, please refresh", Toast.LENGTH_SHORT).show()
                    return@Observer
                }

                binding.user = userData

                if(userData.gender.trim() == "Male") {
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
                if(userData.userImageUrl != null && !userData.userImageUrl.equals("")) {
                    val imgUri = userData.userImageUrl.toUri().buildUpon().scheme("https").build()
                    val imageView = binding.userPicture

                    Glide.with(imageView.context)
                        .load(imgUri)
                        .into(imageView)
                }
            }
        })

        //If in any case context is null, then don't set adapter to the recycler view, it'll make the app crash.
        val mContext = context
        if(mContext != null) {
            binding.infoRecyclerView.layoutManager =
                LinearLayoutManager(context, LinearLayoutManager.HORIZONTAL, false)
            infoAdapter = InfoAdapter(listOf(), mContext)
            binding.infoRecyclerView.adapter = infoAdapter
        }

        return binding.root
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        pickImageIntent = Intent(Intent.ACTION_PICK)
        pickImageIntent.type = "image/*"

        //If the user first time give permission and he accept it, then do upload intent
        uploadImageFirstTimePermission.observe(this, Observer {
            uploadImage()
        })

        if(context == null) {
            return
        }

        //Dialog to get the permission initialization
        alertDialog = AlertDialog.Builder(context)
            .setMessage(R.string.read_permission)
            .setPositiveButton(
                R.string.positive_permission
            ) { dialog, id ->
                Log.d(TAG, "Positive Permission clicked.")
                dialog.dismiss()

                //If the user agree, ask it again.
                askPermission()
            }
            .setNegativeButton(
                R.string.negative_permission
            ) { dialog, which ->
                Log.d(TAG, "Negative permission clicked.")
                dialog.dismiss()
            }.create()
    }

    override fun onResume() {
        val session = context?.getSharedPreferences(getString
            (R.string.preferences_file_key), Context.MODE_PRIVATE)
            ?.getString(getString(R.string.session_cache), "")

        coroutineScope.launch {
            infoViewModel.getUserInformation(session!!)
            infoViewModel.getDogInformation(session!!)
        }
        super.onResume()
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        when(requestCode) {
            PHOTO_PICKER -> {
                if (resultCode == Activity.RESULT_OK) {

                    //Check data validity.
                    if(data == null) {
                        Log.e(TAG, "Data is null")
                        return
                    }

                    //Get the file from the device
                    val file = getBitmapFile(data) ?: return

                    //Set the picture in the page.
                    binding.userPicture.setImageDrawable(Drawable.createFromPath(file.absolutePath))

                    val session = context?.getSharedPreferences(getString(R.string.preferences_file_key), Context.MODE_PRIVATE)
                        ?.getString(getString(R.string.session_cache), "")

                    //TODO:Network operation to upload the image into server
                    coroutineScope.launch {
                        infoViewModel.uploadImage(file, session!!)
                    }
                } else {
                    Toast.makeText(context, "Cancelled get the picture", Toast.LENGTH_SHORT).show()
                }
            }
            else -> {}
        }
    }

    fun uploadImage() {
        val mContext = context ?: return
        val mActivity = activity ?: return

        //Check permission for read image into device storage
        if (ContextCompat.checkSelfPermission(
                mContext,
                Manifest.permission.READ_EXTERNAL_STORAGE
            ) != PackageManager.PERMISSION_GRANTED
        ) {
            //If not granted, give explanation and ask it again
            if(ActivityCompat.shouldShowRequestPermissionRationale(mActivity, Manifest.permission.READ_EXTERNAL_STORAGE)) {
                Log.d(TAG, "User denied the permission request")

                if(alertDialog != null) {
                    alertDialog!!.show()
                } else {
                    Toast.makeText(context, R.string.read_permission, Toast.LENGTH_SHORT).show()
                }
            } else {
                askPermission()
            }
        } else {
            startActivityForResult(pickImageIntent, PHOTO_PICKER)
        }
    }

    //Ask permission to read image in the device storage
    private fun askPermission() {
        val mActivity = activity ?: return

        Log.d(TAG, "Asking a permission")
        ActivityCompat.requestPermissions(
            mActivity,
            arrayOf(
                Manifest.permission.READ_EXTERNAL_STORAGE
            ),
            READ_STORAGE_PERMISSION
        )
    }

    //TODO:Find out the logic behind this
    private fun getBitmapFile(data: Intent): File? {
        val mActivity = activity ?: return null

        val selectedImage = data.data ?: return null

        val cursor = mActivity.contentResolver.query(
            selectedImage,
            arrayOf(MediaStore.Images.ImageColumns.DATA),
            null,
            null,
            null
        ) ?: return null

        cursor.moveToFirst()

        val idx = cursor.getColumnIndex(MediaStore.Images.ImageColumns.DATA)
        val selectedImagePath = cursor.getString(idx)
        cursor.close()

        return File(selectedImagePath)
    }
}