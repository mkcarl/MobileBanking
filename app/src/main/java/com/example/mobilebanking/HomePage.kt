package com.example.mobilebanking

import MyViewModel
import android.content.DialogInterface
import android.nfc.Tag
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.TextView
import androidx.appcompat.app.AlertDialog
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.RecyclerView
import com.example.mobilebanking.adapter.NewsAdapter
import com.example.mobilebanking.data.NewsDatasource
import com.google.firebase.auth.ktx.auth
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import kotlin.properties.Delegates

// the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
private const val ARG_PARAM1 = "param1"
private const val ARG_PARAM2 = "param2"
private const val TAG = "HomePage"
/**
 * A simple [Fragment] subclass.
 * Use the [HomePage.newInstance] factory method to
 * create an instance of this fragment.
 */
class HomePage : Fragment() {
    private var param1: String? = null
    private var param2: String? = null
    private lateinit var username : String
    private lateinit var labelWelcome : TextView
    private lateinit var labelBalance : TextView
    private lateinit var labelAccNum : TextView
    private val db = Firebase.firestore
    private val model : MyViewModel by activityViewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
            param1 = it.getString(ARG_PARAM1)
            param2 = it.getString(ARG_PARAM2)
        }
        model.getBalance().observe(this, Observer<Double>{ bal ->
            labelBalance.text = getString(R.string.home_balance, bal)
        })
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        username = arguments?.getString("username").toString()
        // Inflate the layout for this fragment
        val myView = inflater.inflate(R.layout.fragment_home_page, container, false)
        val btnLogout:Button = myView.findViewById(R.id.button_homeLogOut)
        val recyclerNews : RecyclerView = myView.findViewById(R.id.recycle_homeNews)
        labelWelcome = myView.findViewById(R.id.text_homeWelcome)
        labelAccNum = myView.findViewById(R.id.text_homeAccNum)
        labelBalance = myView.findViewById(R.id.text_homeBalance)
        recyclerNews.setHasFixedSize(true)
        recyclerNews.setItemViewCacheSize(20)
        btnLogout.setOnClickListener {
            // https://stackoverflow.com/a/59935762
            AlertDialog.Builder(requireContext())
                .setTitle("Quit")
                .setMessage("Are you sure you want to log out?")
                .setCancelable(false)
                .setPositiveButton("Yes") {
                        dialog: DialogInterface, _: Int ->
                    dialog.dismiss()
                    // for sending data to previous activity use
                    // setResult(response code, data)
                    activity?.finish()
                }
                .setNegativeButton("No") {
                        dialog: DialogInterface, _: Int ->
                    dialog.dismiss()
                }
                .show()
        }

        val news = NewsDatasource().loadNews()
        recyclerNews.adapter = NewsAdapter(news)

        labelWelcome.text = getString(R.string.home_welcome_user, model.getUsername())
        labelAccNum.text = getString(R.string.home_account_number, model.getAccountNumber())

        return myView
    }

    override fun onResume() {
        super.onResume()
        Log.d(TAG, "onResume called")

    }

    override fun onPause() {
        super.onPause()
        Log.d(TAG, "onPause called")
    }

    override fun onDestroyView() {
        super.onDestroyView()
        Log.d(TAG, "onDestroyView called")
    }

    override fun onDestroy() {
        super.onDestroy()
        Log.d(TAG, "onDestroy called")

    }

    companion object {
        /**
         * Use this factory method to create a new instance of
         * this fragment using the provided parameters.
         *
         * @param param1 Parameter 1.
         * @param param2 Parameter 2.
         * @return A new instance of fragment HomePage.
         */
        @JvmStatic
        fun newInstance(param1: String, param2: String) =
            HomePage().apply {
                arguments = Bundle().apply {
                    putString(ARG_PARAM1, param1)
                    putString(ARG_PARAM2, param2)
                }
            }
    }
}