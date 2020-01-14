package com.example.dogwalker.view

import android.app.AlertDialog
import android.content.Context
import android.content.DialogInterface
import android.os.Bundle
import android.util.Log
import android.view.*
import android.widget.TextView
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProviders
import androidx.navigation.findNavController
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.dogwalker.R
import com.example.dogwalker.adapter.WalkerOrderAdapter
import com.example.dogwalker.databinding.FragmentWalkerOrderBinding
import com.example.dogwalker.viewmodel.ViewModelFactory
import com.example.dogwalker.viewmodel.WalkerOrderViewModel
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.launch
import java.lang.Exception
import java.text.SimpleDateFormat
import java.util.*

class WalkerOrderFragment : Fragment() {

    private val TAG = WalkerOrderFragment::class.java.simpleName
    private val walkerOrderViewModel by lazy {
        ViewModelProviders.of(this, ViewModelFactory()).get(WalkerOrderViewModel::class.java)
    }
    private val coroutineScope = CoroutineScope(Job() + Dispatchers.Main)
    private var walkerOrderAdapter = WalkerOrderAdapter(listOf())

    private val DatePickerTAG = "DatePicker"
    private val TimePickerTAG = "TimePicker"

    private lateinit var binding: FragmentWalkerOrderBinding

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentWalkerOrderBinding.inflate(inflater)

        setHasOptionsMenu(true)
        
        val currentDate = Calendar.getInstance().time
        binding.dogCalendarOrder.text = SimpleDateFormat("dd-MM-yyyy").format(currentDate)
        binding.timeOrderWalker.text = SimpleDateFormat("kk:mm:ss").format(currentDate)

        binding.dogCalendarOrder.setOnClickListener {
            Log.d(TAG, "Calendar picked")

            val newFragment = DateFragment(it as TextView)
            newFragment.show(fragmentManager!!, DatePickerTAG)
        }

        binding.timeOrderWalker.setOnClickListener {
            Log.d(TAG, "Time picked")

            val newFragment = TimeFragment(it as TextView)
            newFragment.show(fragmentManager!!, TimePickerTAG)
        }

        binding.walkerOrderButton.setOnClickListener {
           navigateToList(it)
        }

        binding.hoursText.setOnKeyListener { v, keyCode, event ->
            if(event.action == KeyEvent.ACTION_DOWN) {
                when(keyCode) {
                    KeyEvent.KEYCODE_ENTER -> {
                        navigateToList(v)
                        true
                    }
                    else -> false
                }
            } else false
        }

        walkerOrderViewModel.listDog.observe(this, androidx.lifecycle.Observer {
            if(it != null) {
                if(it.isEmpty()) {
                    AlertDialog.Builder(ContextThemeWrapper(context, android.R.style.Theme_DeviceDefault_Dialog_Alert))
                        .setTitle("Peringatan")
                        .setMessage("Anda tidak mempunyai anjing untuk dijalankan, " +
                                "tolong daftarkan anjing anda terlebih dahulu")
                        .setPositiveButton("OK") { dialogInterface, i ->
                            activity?.finish()
                        }.create().show()
                }

                walkerOrderAdapter.listDog = it
                walkerOrderAdapter.notifyDataSetChanged()
            }
        })

        binding.walkerOrderRecyclerView.adapter = walkerOrderAdapter
        binding.walkerOrderRecyclerView.layoutManager = LinearLayoutManager(context, LinearLayoutManager.HORIZONTAL, false)

        val session = context!!.getSharedPreferences(getString(R.string.preferences_file_key), Context.MODE_PRIVATE)
            .getString(getString(R.string.session_cache), "")

        coroutineScope.launch {
            walkerOrderViewModel.GetDog(session)
        }

        return binding.root
    }

    fun navigateToList(it: View) {
        try {
            val hours = Integer.parseInt(binding.hoursText.text.toString())

            val linearLayoutManager =
                binding.walkerOrderRecyclerView.layoutManager as LinearLayoutManager

            val action = WalkerOrderFragmentDirections.actionWalkerOrderFragmentToListOrderFragment(
                date = "${binding.timeOrderWalker.text} ${binding.dogCalendarOrder.text}",
                hours = hours,
                dogId = walkerOrderAdapter.listDog[linearLayoutManager.findFirstVisibleItemPosition()].id
            )

            it.findNavController().navigate(action)
        }
        catch(e :Exception) {
            Toast.makeText(context, "Hours must be numeric", Toast.LENGTH_SHORT).show()
        }
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        if(item.itemId == android.R.id.home) {
            activity?.finish()
        }
        return super.onOptionsItemSelected(item)
    }
}