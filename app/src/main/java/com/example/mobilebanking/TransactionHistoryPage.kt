package com.example.mobilebanking

import MyViewModel
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.RecyclerView
import androidx.viewpager2.widget.ViewPager2
import com.example.mobilebanking.adapter.TransactionAdapter
import com.example.mobilebanking.adapter.TransactionPagerAdapter
import com.google.android.material.tabs.TabLayout
import com.google.android.material.tabs.TabLayoutMediator
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase

// the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
private const val ARG_PARAM1 = "param1"
private const val ARG_PARAM2 = "param2"
private const val TAG = "TransactionHistoryPage"

/**
 * A simple [Fragment] subclass.
 * Use the [TransactionHistoryPage.newInstance] factory method to
 * create an instance of this fragment.
 */
class TransactionHistoryPage : Fragment() {
    private var param1: String? = null
    private var param2: String? = null
    val db = Firebase.firestore
    private val model : MyViewModel by activityViewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
            param1 = it.getString(ARG_PARAM1)
            param2 = it.getString(ARG_PARAM2)
        }


    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        Log.d(TAG, "onCreateView called")
        val myView = inflater.inflate(R.layout.fragment_transaction_history_page, container, false)
        val pager : ViewPager2 = myView.findViewById(R.id.pager_historyTransactions)
        val tabLayout : TabLayout = myView.findViewById(R.id.tabLayout_historyTransactions)
        pager.adapter = TransactionPagerAdapter(requireActivity(), tabLayout.tabCount)
        TabLayoutMediator(tabLayout, pager){ _,_ -> } .attach()
        val labelAccNum : TextView = myView.findViewById(R.id.text_historyAccNum)
        labelAccNum.text = getString(R.string.history_account_number, model.getAccountNumber())

        return myView
    }

    companion object {
        /**
         * Use this factory method to create a new instance of
         * this fragment using the provided parameters.
         *
         * @param param1 Parameter 1.
         * @param param2 Parameter 2.
         * @return A new instance of fragment TransactionHistoryPage.
         */
        @JvmStatic
        fun newInstance(param1: String, param2: String) =
            TransactionHistoryPage().apply {
                arguments = Bundle().apply {
                    putString(ARG_PARAM1, param1)
                    putString(ARG_PARAM2, param2)
                }
            }
    }
}