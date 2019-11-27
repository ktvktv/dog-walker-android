package com.example.dogwalker.view

import android.Manifest
import android.app.Activity
import android.app.AlertDialog
import android.content.Context
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
import kotlinx.coroutines.launch
import okhttp3.MultipartBody
import okhttp3.RequestBody
import java.io.File
import java.lang.Exception
import android.net.Uri
import android.provider.OpenableColumns
import android.provider.DocumentsContract
import android.content.ContentUris
import android.content.DialogInterface
import android.content.pm.PackageManager
import android.os.Build
import android.os.Environment
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat


class InfoFragment : Fragment() {

    private lateinit var binding: FragmentInfoBinding

    private lateinit var coroutineScope: CoroutineScope

    val PHOTO_PICKER = 1

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentInfoBinding.inflate(inflater)

        return binding.root
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        coroutineScope = CoroutineScope(Job() + Dispatchers.Main)

        val intent = Intent(Intent.ACTION_GET_CONTENT)
        intent.type = "image/*"
        startActivityForResult(intent, PHOTO_PICKER)
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        if (resultCode == Activity.RESULT_OK) {
//            val relativePath = data!!.data.path
            Log.d("onActivityResult", data!!.data.path)
//            Log.d("onActivityResult", data!!.data.toString())
//
//            val fileName = getPathFromUri(context!!, data!!.data)
//            Log.d("onActivityResult", fileName)
////                val inputStream = context!!.contentResolver.openInputStream(data!!.data)
//            val imageFile = File(fileName)
//
////            val imageFile = File(picturePath)
////
//            coroutineScope.launch {
//                hitAPI(imageFile)
//            }
        } else {
            Log.d("onActivityResult", "No data")
        }
    }

    fun getFileName(uri: Uri): String {
        var result: String? = null
        if (uri.scheme == "content") {
            val cursor = activity!!.contentResolver.query(uri, null, null, null, null)
            try {
                if (cursor != null && cursor!!.moveToFirst()) {
                    result =
                        cursor!!.getString(cursor!!.getColumnIndex(OpenableColumns.DISPLAY_NAME))
                }
            } finally {
                cursor!!.close()
            }
        }
        if (result == null) {
            result = uri.path
            val cut = result!!.lastIndexOf('/')
            if (cut != -1) {
                result = result.substring(cut + 1)
            }
        }
        return result
    }

    private suspend fun hitAPI(file: File) {
        val dogData = MultipartBody.Builder()
            .addFormDataPart("breed_id", "1")
            .addFormDataPart("age", "1")
            .addFormDataPart("weight", "1")
            .addFormDataPart("special_needs", "1")
            .addFormDataPart("photo", "dog.jpg", RequestBody.create(MultipartBody.FORM, file))
            .setType(MultipartBody.FORM)
            .build()

        val dogPart = MultipartBody.Part.create(dogData)

        var result: CommonResponse? = null
        try {
            result = DogWalkerServiceApi.DogWalkerService.registerDog(dogPart)!!.await()
        } catch(e: Exception) {
            Log.d("hitAPI", e.message)
            return
        }

        Log.d("hitAPI", result.toString())
    }
}