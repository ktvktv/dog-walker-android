package com.example.dogwalker

import android.Manifest
import android.app.Activity
import android.app.AlertDialog
import android.content.Intent
import android.content.pm.PackageManager
import android.graphics.drawable.Drawable
import android.os.Bundle
import android.provider.MediaStore
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.lifecycle.ViewModelProviders
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.dogwalker.adapter.BreedAdapter
import com.example.dogwalker.databinding.ActivityRegisterDogBinding
import com.example.dogwalker.view.InfoFragment
import com.example.dogwalker.viewmodel.RegisterDogViewModel
import java.io.File
import java.lang.Exception

class RegisterDogActivity : AppCompatActivity(), AdapterView.OnItemSelectedListener {

    private val TAG = RegisterDogActivity::class.java.simpleName
    private val PHOTO_PICKER = 100

    private val registerDogViewModel: RegisterDogViewModel by lazy {
        RegisterDogViewModel(this, breedAdapter)
    }

    private lateinit var binding: ActivityRegisterDogBinding
    private lateinit var alertDialog: AlertDialog
    private lateinit var pickImageIntent: Intent
    private lateinit var breedAdapter: BreedAdapter

    private var breedId = 1

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityRegisterDogBinding.inflate(LayoutInflater.from(this))

        breedAdapter = BreedAdapter(this)
        breedAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)

        //Recycler View
        binding.breedSpinnerView.adapter = breedAdapter
        binding.breedSpinnerView.onItemSelectedListener = this

        //Only trigger, will change it later
        registerDogViewModel.getBreedList()
//        if(breedData != null) {
//            breedAdapter.addAll(breedData)
//            breedAdapter.notifyDataSetChanged()
//        }

        pickImageIntent = Intent(Intent.ACTION_PICK)
        pickImageIntent.type = "image/*"

        Log.d(TAG, "Binding Register Dog")

        alertDialog = AlertDialog.Builder(this)
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

        binding.dogImage.setOnClickListener {
            uploadImage()
        }

        binding.registerDogButton.setOnClickListener {
            val errorMessage = checkValidity()
            if(errorMessage.equals("")) {
                //Call server

                //Insert DB

                //Back to info page.
            } else {
                Toast.makeText(this, errorMessage, Toast.LENGTH_SHORT).show()
                Log.d(TAG, errorMessage)
            }
        }

        setContentView(binding.root)
    }

    fun checkValidity(): String {
        if(binding.nameView.text.toString().equals("")) {
            return "Name must be filled"
        }

        if(binding.ageView.text.toString().equals("")) {
            return "Age must be filled"
        } else {
             try {
                 Integer.parseInt(binding.ageView.text.toString())
             } catch(e: Exception) {
                 return "Age must be numeric"
             }
        }

        if(binding.weightView.text.toString().equals("")) {
            return "Weight must be filled"
        } else {
            try {
                Integer.parseInt(binding.weightView.text.toString())
            } catch(e: Exception) {
                return "Weight must be numeric"
            }
        }

        if(!binding.maleRadioButton.isChecked && !binding.femaleRadioButton.isChecked) {
            return "Gender must be filled"
        }

        if(binding.dogImage.drawable == null) {
            return "Image must be filled"
        }

        return ""
    }

    fun uploadImage() {
        val mContext = this
        val mActivity = this

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
        val mActivity = this

        Log.d(TAG, "Asking a permission")
        ActivityCompat.requestPermissions(
            mActivity,
            arrayOf(
                Manifest.permission.READ_EXTERNAL_STORAGE,
                Manifest.permission.WRITE_EXTERNAL_STORAGE
            ),
            InfoFragment.READ_WRITE_STORAGE_PERMISSION
        )
    }

    private fun getBitmapFile(data: Intent): File? {
        val mActivity = this

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

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

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
                    binding.dogImage.setImageDrawable(Drawable.createFromPath(file.absolutePath))

                    //Send the image to the server
//                    coroutineScope.launch {
//                        hitAPI(file)
//                    }

                    //Register into the DB
                } else {
                    Toast.makeText(this, "Cancelled get the picture", Toast.LENGTH_SHORT).show()
                }
            }
        }
    }

    override fun onNothingSelected(parent: AdapterView<*>?) {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun onItemSelected(parent: AdapterView<*>?, view: View?, position: Int, id: Long) {
        Log.d(TAG, "${parent!!.getItemAtPosition(position)} - $position")
        breedId = position+1
    }
}