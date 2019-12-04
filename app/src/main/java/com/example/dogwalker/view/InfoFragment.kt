package com.example.dogwalker.view

import android.Manifest
import android.app.Activity
import android.app.AlertDialog
import android.content.Context
import android.content.DialogInterface
import android.content.Intent
import android.database.Cursor
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
import android.net.Uri
import android.provider.OpenableColumns
import kotlinx.coroutines.launch
import androidx.core.app.ActivityCompat
import android.content.pm.PackageManager
import android.graphics.drawable.Drawable
import android.widget.Toast
import androidx.core.content.ContextCompat
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.dogwalker.R
import com.example.dogwalker.adapter.InfoAdapter
import com.example.dogwalker.data.Dog
import com.example.dogwalker.viewmodel.InfoViewModel
import com.example.dogwalker.viewmodel.ViewModelFactory

class InfoFragment : Fragment() {

    private val TAG = InfoFragment::class.java.simpleName

    //Static Value
    companion object Constants {
        //Permission constants
        const val READ_WRITE_STORAGE_PERMISSION = 100
        //Intent constants
        const val PHOTO_PICKER = 1

        val uploadImageFirstTimePermission = MutableLiveData<Boolean>()
    }

    private lateinit var pickImageIntent: Intent
    private lateinit var binding: FragmentInfoBinding
    private lateinit var alertDialog: AlertDialog
    private lateinit var viewModelFactory: ViewModelFactory
    private val coroutineScope = CoroutineScope(Job() + Dispatchers.Main)
    private val infoViewModel: InfoViewModel by lazy {
        ViewModelProviders.of(this, viewModelFactory).get(InfoViewModel::class.java)
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentInfoBinding.inflate(inflater)

        binding.pictureInfoButton.setOnClickListener {
            Log.d(TAG, "Button clicked")
            uploadImage()
        }

        binding.nameInfoText.text = "Kevin Tigravictor"
        binding.addressInfoText.text = "Jl. Barleria VI B1/H5"
        binding.birthdateInfoText.text = "09 September 1998"
        binding.emailInfoText.text = "kevin.victor30@yahoo.com"
        binding.phoneInfoText.text = "+6281290001998"
        binding.genderInfoImage.setImageResource(R.drawable.male_icon)
        binding.roleInfoText.text = "Customer"

        //Dummy data
        val dogData = listOf(
            Dog(
                ownerId = 1,
                breedId = 1,
                age = 10,
                weight = 100,
                gender = "Male",
                name = "Willy",
                specialNeeds = "",
                photo = null),
            Dog(
                ownerId = 2,
                breedId = 2,
                age = 15,
                weight = 150,
                gender = "Female",
                name = "Darren",
                specialNeeds = "",
                photo = null)
        )

        binding.infoRecyclerView.layoutManager = LinearLayoutManager(context, LinearLayoutManager.HORIZONTAL, false)
        binding.infoRecyclerView.adapter = InfoAdapter(dogData, context!!)

        return binding.root
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        pickImageIntent = Intent(Intent.ACTION_PICK)
        pickImageIntent.type = "image/*"

        Log.d(TAG, "Upload image value: ${uploadImageFirstTimePermission.value}")
        uploadImageFirstTimePermission.observe(this, Observer {
            Log.d(TAG, "Upload image value: ${uploadImageFirstTimePermission.value}")
            uploadImage()
        })

        //ViewModelFactory initialize
        viewModelFactory = ViewModelFactory()

        if(context == null) {
            return
        }

        //Alert Dialog initialize
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

//                    coroutineScope.launch {
//                        hitAPI(file)
//                    }
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

        //Check permission for read and write data into device storage
        if (ContextCompat.checkSelfPermission(
                mContext,
                Manifest.permission.READ_EXTERNAL_STORAGE
            ) != PackageManager.PERMISSION_GRANTED || ContextCompat.checkSelfPermission(
                mContext,
                Manifest.permission.WRITE_EXTERNAL_STORAGE
            ) != PackageManager.PERMISSION_GRANTED
        ) {
            //If not granted, give explanation and ask it again
            if(ActivityCompat.shouldShowRequestPermissionRationale(mActivity, Manifest.permission.READ_EXTERNAL_STORAGE) &&
                ActivityCompat.shouldShowRequestPermissionRationale(mActivity, Manifest.permission.WRITE_EXTERNAL_STORAGE)) {
                Log.d(TAG, "User denied the permission request")
                alertDialog.show()
            } else {
                askPermission()
            }
        } else {
            startActivityForResult(pickImageIntent, PHOTO_PICKER)
        }
    }

    //Ask permission to read and write file in the device storage
    private fun askPermission() {
        val mActivity = activity ?: return

        Log.d(TAG, "Asking a permission")
        ActivityCompat.requestPermissions(
            mActivity,
            arrayOf(
                Manifest.permission.READ_EXTERNAL_STORAGE,
                Manifest.permission.WRITE_EXTERNAL_STORAGE
            ),
            READ_WRITE_STORAGE_PERMISSION
        )
    }

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

    private suspend fun hitAPI(file: File) {

//        val dogsData = Dog(
//            ownerId = 1,
//            breedId = 1,
//            age = 1,
//            weight = 1,
//            specialNeeds = "1",
//            name = "1",
//            gender = "1",
//            photo = RequestBody.create(MultipartBody.FORM, file)
//        )

//        val dogData = MultipartBody.Builder()
//            .addFormDataPart("owner_id", "1")
//            .addFormDataPart("breed_id", "1")
//            .addFormDataPart("age", "1")
//            .addFormDataPart("weight", "1")
//            .addFormDataPart("special_needs", "1")
//            .addFormDataPart("name", "1")
//            .addFormDataPart("gender", "1")
//            .addFormDataPart("photo", "dog.jpg", RequestBody.create(MultipartBody.FORM, file))
//            .setType(MultipartBody.FORM)
//            .build()

        var result: CommonResponse? = null
        try {
            result = DogWalkerServiceApi.DogWalkerService.registerDog(ownerId = 1,
                breedId = 1,
                age = 1,
                weight = 1,
                specialNeeds = "1",
                name = "1",
                gender = "1",
                photo = RequestBody.create(MultipartBody.FORM, file))!!.await()
        } catch(e: Exception) {
            Log.e("hitAPI", e.message)
            e.printStackTrace()
            return
        }

        Log.d("hitAPI", result.toString())
    }

}