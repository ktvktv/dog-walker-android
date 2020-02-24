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
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import com.bumptech.glide.Glide
import com.bumptech.glide.load.engine.DiskCacheStrategy
import com.bumptech.glide.request.RequestOptions
import com.example.dogwalker.PHOTO_PICKER
import com.example.dogwalker.R
import com.example.dogwalker.READ_STORAGE_PERMISSION
import com.example.dogwalker.SUCCESSFUL
import com.example.dogwalker.adapter.BreedAdapter
import com.example.dogwalker.data.Dog
import com.example.dogwalker.databinding.FragmentDogUpdateBinding
import com.example.dogwalker.viewmodel.DogUpdateViewModel
import com.example.dogwalker.viewmodel.ViewModelFactory
import kotlinx.android.synthetic.main.comment_item.view.*
import kotlinx.android.synthetic.main.fragment_dog_update.*
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.launch
import java.io.File

class DogUpdateFragment : Fragment() {
    private lateinit var binding: FragmentDogUpdateBinding
    private lateinit var listDog: List<Dog>

    private val dogUpdateViewModel by lazy {
        ViewModelProviders.of(this, ViewModelFactory()).get(DogUpdateViewModel::class.java)
    }
    private val pickImageIntent = Intent(Intent.ACTION_PICK)
    private val coroutineScope = CoroutineScope(Job() + Dispatchers.Main)
    private val TAG = DogUpdateFragment::class.java.simpleName

    private var currentPage = MutableLiveData<Int>()
    private var file: File? = null
    private var alertDialog: AlertDialog? = null

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
            dogUpdateViewModel.fetchBreedList()
        }

        dogUpdateViewModel.breedResponse.observe(this, Observer {
            if(it != null) {
                val breedAdapter = BreedAdapter(context!!)
                breedAdapter.addAll(it)
                binding.spinner.adapter = breedAdapter

                if(!listDog.isEmpty()) {
                    binding.spinner.setSelection(listDog[currentPage.value!!].breedId!!)
                }
            }
        })

        dogUpdateViewModel.listDogResponse.observe(this, Observer {
            if(it != null && it.message == SUCCESSFUL && it.body != null) {
                listDog = it.body
                if(listDog.isEmpty()) {
                    binding.dogUpdateConstraint.visibility = View.GONE
                }
                currentPage.value = 0
            }
        })

        currentPage.observe(this, Observer {
            if(listDog.isNotEmpty()) {
                if(listDog.size == 1) {
                    binding.previousButton.visibility = View.GONE
                    binding.nextButton.visibility = View.GONE
                } else {
                    when (it) {
                        0 -> {
                            binding.previousButton.visibility = View.GONE
                            binding.nextButton.visibility = View.VISIBLE
                        }
                        (listDog.size - 1) -> {
                            binding.nextButton.visibility = View.GONE
                            binding.previousButton.visibility = View.VISIBLE
                        }
                        else -> {
                            binding.nextButton.visibility = View.VISIBLE
                            binding.previousButton.visibility = View.VISIBLE
                        }
                    }
                }

                Log.d(TAG, "TEST")

                setData()
            }
        })

        dogUpdateViewModel.updateDogResponse.observe(this, Observer {
            if(it != null) {
                if (it.message == SUCCESSFUL) {
                    Toast.makeText(context, "Berhasil memperbarui data anjing!", Toast.LENGTH_SHORT).show()

                    activity?.finish()
                } else {
                    Toast.makeText(context, "Gagal memperbarui data anjing\n${it.message}", Toast.LENGTH_SHORT)
                        .show()
                }
            } else {
                Toast.makeText(context, "Terjadi kesalahan, mohon coba lagi", Toast.LENGTH_SHORT)
                    .show()
            }
        })

        dogUpdateViewModel.deleteDogResponse.observe(this, Observer {
            if(it != null) {
                if(it.message == SUCCESSFUL) {
                    Toast.makeText(context, "Berhasil menghapus anjing", Toast.LENGTH_SHORT).show()

                    activity?.finish()
                } else {
                    Toast.makeText(context, "Gagal menghapus anjing\n${it.message}", Toast.LENGTH_SHORT).show()
                }
            }
        })

        binding.nextButton.setOnClickListener {
            currentPage.value = currentPage.value as Int + 1
        }

        binding.previousButton.setOnClickListener {
            currentPage.value = currentPage.value as Int - 1
        }

        binding.dogPhoto.setOnClickListener {
            uploadImage()
        }

        binding.updateButton.setOnClickListener {
            var age = 0
            try {
                age = (Integer.parseInt(binding.yearAge.text.toString()) * 12) +
                        (Integer.parseInt(binding.monthAge.text.toString()))
            } catch (e: Exception) {
                Log.e(TAG, "Parsing error: ${e.message}")
                Toast.makeText(context, "Masukkan tahun dan bulan dalam angka", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }

            var weight = 0
            try {
               weight = Integer.parseInt(binding.dogWeightText.text.toString())
            } catch(e: Exception) {
                Log.e(TAG, "Parsing error: ${e.message}")
                Toast.makeText(context, "Masukkan berat dalam angka", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }

            val name = binding.nameText.text.toString()
            val gender = if(binding.maleRadio.isChecked) "male" else "female"

            coroutineScope.launch {
                dogUpdateViewModel.updateDog(session, Dog(
                    age = age,
                    name = name,
                    gender = gender,
                    photo = null,
                    weight = weight,
                    id = listDog[currentPage.value!!].id
                ), file, binding.spinner.selectedItemPosition+1)
            }
        }

        binding.deleteButton.setOnClickListener {
            coroutineScope.launch {
                dogUpdateViewModel.deleteDog(session, listDog[currentPage.value!!].id)
            }
        }

        return binding.root
    }

    private fun setData() {
        Log.d(TAG, "current page now: ${currentPage.value}")

        val currentPage = currentPage.value as Int
        binding.dog = listDog[currentPage]

        if (listDog[currentPage].photo != null && listDog[currentPage].photo != "") {
            val imgUri =
                listDog[currentPage].photo!!.toUri().buildUpon().scheme("https").build()
            val imageView = binding.dogPhoto

            Glide.with(imageView.context)
                .load(imgUri)
                .apply(RequestOptions.skipMemoryCacheOf(true))
                .apply(RequestOptions.diskCacheStrategyOf(DiskCacheStrategy.NONE))
                .into(imageView)
        }

        if (listDog[currentPage].gender.toLowerCase() == "male") {
            binding.maleRadio.isChecked = true
            binding.femaleRadio.isChecked = false
        } else {
            binding.maleRadio.isChecked = false
            binding.femaleRadio.isChecked = true
        }

        val age = listDog[currentPage].age

        binding.yearAge.setText("${age / 12}")
        binding.monthAge.setText("${age % 12}")

        Log.d(TAG, "SPINNER SELECTED, BreedID: ${listDog[currentPage].breedId!!}")
        binding.spinner.setSelection(listDog[currentPage].breedId!!)
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

                    Log.d(TAG, "Path: ${file?.absolutePath}")

                    //Set the picture in the page.
                    binding.dogPhoto.setImageDrawable(Drawable.createFromPath(file?.absolutePath))
                } else {
                    Toast.makeText(context, "Batal mengambil gambar", Toast.LENGTH_SHORT).show()
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