package com.example.dogwalker

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
import android.view.MenuItem
import android.view.View
import android.widget.AdapterView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import com.example.dogwalker.adapter.BreedAdapter
import com.example.dogwalker.data.RegisterDogRequest
import com.example.dogwalker.databinding.ActivityRegisterDogBinding
import com.example.dogwalker.view.InfoFragment
import com.example.dogwalker.viewmodel.RegisterDogViewModel
import com.example.dogwalker.viewmodel.ViewModelFactory
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.launch
import java.io.File
import java.lang.Exception

class RegisterDogActivity : AppCompatActivity(), AdapterView.OnItemSelectedListener {

    private val TAG = RegisterDogActivity::class.java.simpleName
    private val PHOTO_PICKER = 100

    private val registerDogViewModel: RegisterDogViewModel by lazy {
        ViewModelProviders.of(this, ViewModelFactory()).get(RegisterDogViewModel::class.java)
    }
    private val coroutineScope = CoroutineScope(Job() + Dispatchers.Main)

    private lateinit var binding: ActivityRegisterDogBinding
    private lateinit var alertDialog: AlertDialog
    private lateinit var pickImageIntent: Intent
    private lateinit var breedAdapter: BreedAdapter

    private var breedId = 1
    private var age = 0
    private var weight = 0
    private var file: File? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        supportActionBar?.setDisplayHomeAsUpEnabled(true)

        binding = ActivityRegisterDogBinding.inflate(LayoutInflater.from(this))

        breedAdapter = BreedAdapter(this)
        breedAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)

        //Recycler View
        binding.breedSpinnerView.adapter = breedAdapter
        binding.breedSpinnerView.onItemSelectedListener = this

        registerDogViewModel.listBreed.observe(this, Observer {
            if(it != null) {
                breedAdapter.addAll(it)
                breedAdapter.notifyDataSetChanged()
            }
        })

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

        registerDogViewModel.registerResponse.observe(this, Observer {
            var isSuccess = false
            var message = "Terjadi kesalahan."
            if(it != null) {
                if(it.message == SUCCESSFUL) {
                    message = "Registrasi berhasil!"
                    isSuccess = true
                } else {
                    message = it.message ?: "Terjadi kesalahan"
                }
            }

            Toast.makeText(this, message, Toast.LENGTH_SHORT).show()

            if(isSuccess) {
                finish()
            }
        })

        binding.registerDogButton.setOnClickListener {
            val errorMessage = checkValidity()

            val gender = if(binding.maleRadioButton.isChecked) "Male" else "Female"

            val session = getSharedPreferences(getString(R.string.preferences_file_key), Context.MODE_PRIVATE)
                .getString(getString(R.string.session_cache), "")
            if(errorMessage.equals("")) {
                coroutineScope.launch {
                    registerDogViewModel.registerDog(
                        RegisterDogRequest(
                            breedId = breedId,
                            age = age,
                            weight = weight,
                            gender = gender,
                            name = binding.nameView.text.toString(),
                            specialNeeds = binding.specialNeedText.text.toString(),
                            photo = null
                        ), file!!, session
                    )
                }
            } else {
                Toast.makeText(this, errorMessage, Toast.LENGTH_SHORT).show()
                Log.d(TAG, errorMessage)
            }
        }

        setContentView(binding.root)
    }

    fun checkValidity(): String {
        if(binding.nameView.text.toString().equals("")) {
            return "Nama harus diisi!"
        }

        if(binding.ageView.text.toString().equals("") || binding.ageMonthView.text.toString().equals("")) {
            return "Umur harus diisi!"
        } else {
             try {
                 age = Integer.parseInt(binding.ageView.text.toString()) * 12 + Integer.parseInt(binding.ageMonthView.text.toString())
             } catch(e: Exception) {
                 return "Umur harus angka!"
             }
        }

        if(binding.weightView.text.toString().equals("")) {
            return "Berat harus diisi!"
        } else {
            try {
                weight = Integer.parseInt(binding.weightView.text.toString())
            } catch(e: Exception) {
                return "Berat harus angka!"
            }
        }

        if(!binding.maleRadioButton.isChecked && !binding.femaleRadioButton.isChecked) {
            return "Jenis kelamin harus dipilih!"
        }

        if(binding.dogImage.drawable == null) {
            return "Harus mengambil foto!"
        }

        if(file == null) {
            return "Tolong pilih foto"
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
            READ_STORAGE_PERMISSION
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
                    file = getBitmapFile(data) ?: return

                    //Set the picture in the page.
                    binding.dogImage.setImageDrawable(Drawable.createFromPath(file!!.absolutePath))

                    //Send the image to the server
//                    coroutineScope.launch {
//                        hitAPI(file)
//                    }

                    //Register into the DB
                } else {
                    Toast.makeText(this, "Batal mengambil foto", Toast.LENGTH_SHORT).show()
                }
            }
        }
    }

    override fun onNothingSelected(parent: AdapterView<*>?) {
        //Not implemented right now
    }

    override fun onItemSelected(parent: AdapterView<*>?, view: View?, position: Int, id: Long) {
        Log.d(TAG, "${parent!!.getItemAtPosition(position)} - $position")
        breedId = position+1
    }

    override fun onOptionsItemSelected(item: MenuItem?): Boolean {
        if(item?.itemId == android.R.id.home) {
            finish()
        }

        return super.onOptionsItemSelected(item)
    }
}