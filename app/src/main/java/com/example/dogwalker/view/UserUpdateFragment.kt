package com.example.dogwalker.view

import android.Manifest
import android.app.Activity
import android.app.AlertDialog
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.graphics.drawable.Drawable
import android.os.Bundle
import android.provider.MediaStore
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.core.net.toUri
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import com.bumptech.glide.Glide
import com.bumptech.glide.load.engine.DiskCacheStrategy
import com.bumptech.glide.request.RequestOptions
import com.example.dogwalker.PHOTO_PICKER
import com.example.dogwalker.R
import com.example.dogwalker.READ_STORAGE_PERMISSION
import com.example.dogwalker.SUCCESSFUL
import com.example.dogwalker.data.User
import com.example.dogwalker.databinding.FragmentUserUpdateBinding
import com.example.dogwalker.viewmodel.UserUpdateViewModel
import com.example.dogwalker.viewmodel.ViewModelFactory
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.launch
import java.io.File

class UserUpdateFragment : Fragment() {
    private val TAG = UserUpdateFragment::class.java.simpleName
    private val coroutineScope = CoroutineScope(Job() + Dispatchers.Main)
    private val userUpdateViewModel by lazy {
        ViewModelProviders.of(this, ViewModelFactory()).get(UserUpdateViewModel::class.java)
    }
    private val pickImageIntent = Intent(Intent.ACTION_PICK)
    private lateinit var binding: FragmentUserUpdateBinding

    private var file: File? = null
    private var alertDialog: AlertDialog? = null

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentUserUpdateBinding.inflate(inflater)

        val session = context!!.getSharedPreferences(getString(R.string.preferences_file_key), Context.MODE_PRIVATE)
            .getString(getString(R.string.session_cache), "")

        //Get the data
        coroutineScope.launch {
            userUpdateViewModel.getUserInfo(session)
        }

        binding.birthdateText.setOnClickListener {
            val calendarDialog = DateFragment(binding.birthdateText)

            calendarDialog.show(activity!!.supportFragmentManager, null)
        }

        userUpdateViewModel.user.observe(this, Observer {
            if(it != null && it.message == SUCCESSFUL) {
                binding.user = it.body

                if(it.body!!.gender.toLowerCase() == "male") {
                    binding.maleRadio.isChecked = true
                    binding.femaleRadio.isChecked = false
                } else {
                    binding.femaleRadio.isChecked = true
                    binding.maleRadio.isChecked = false
                }

                if(it.body.userImageUrl != null && it.body.userImageUrl != "") {
                    val imgUri = it.body.userImageUrl.toUri().buildUpon().scheme("https").build()
                    val imageView = binding.userProfileImage

                    Glide.with(imageView.context)
                        .load(imgUri)
                        .apply(RequestOptions.skipMemoryCacheOf(true))
                        .apply(RequestOptions.diskCacheStrategyOf(DiskCacheStrategy.NONE))
                        .into(imageView)
                }
            }
        })

        binding.button.setOnClickListener {
            val message = checkValidity()
            if(message != "") {
                Toast.makeText(context, message, Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }

            coroutineScope.launch {
                val gender = if(binding.maleRadio.isChecked) "male" else "female"
                userUpdateViewModel.updateUserInfo(session, User(
                    id = -1,
                    email = binding.emailText.text.toString(),
                    phoneNumber = binding.phonenumberText.text.toString(),
                    password = binding.passwordText.text.toString(),
                    name = binding.nameText.text.toString(),
                    nik = binding.nikText.text.toString(),
                    gender = gender,
                    address = binding.addressText.text.toString(),
                    isWalker = true,
                    type = "",
                    birthDate = binding.birthdateText.text.toString(),
                    birthPlace = binding.birthplaceText.text.toString(),
                    userImageUrl = null
                ), file)
            }
        }

        binding.userProfileImage.setOnClickListener {
            uploadImage()
        }

        userUpdateViewModel.updateResp.observe(this, Observer {
            if(it != null && it.message == SUCCESSFUL) {
                Toast.makeText(context, "Success update user information", Toast.LENGTH_SHORT).show()
                activity?.finish()
            }
        })

        return binding.root
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        pickImageIntent.type = "image/*"

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

    fun checkValidity(): String {
        if(binding.nameText.text.toString().equals("")) {
            return "Name must be filled"
        }

        if(binding.emailText.text.toString().equals("")) {
            return "Email must be filled"
        }

        if(binding.phonenumberText.text.toString().equals("")) {
            return "Phone number must be filled"
        }

        if(binding.addressText.text.toString().equals("")) {
            return "Address must be filled"
        }

        if(binding.birthdateText.text.toString().equals("")) {
            return "Birthdate must be filled"
        }

        if(binding.birthplaceText.text.toString().equals("")) {
            return "Birthplace must be filled"
        }

        if(!binding.maleRadio.isChecked && !binding.femaleRadio.isChecked) {
            return "Gender must be filled"
        }

        return ""
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
                    file = getBitmapFile(data) ?: return

                    //Set the picture in the page.
                    binding.userProfileImage.setImageDrawable(Drawable.createFromPath(file?.absolutePath))
                } else {
                    Toast.makeText(context, "Cancelled get the picture", Toast.LENGTH_SHORT).show()
                }
            }
            else -> {}
        }
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
}