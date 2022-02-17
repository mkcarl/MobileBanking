package com.example.mobilebanking

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import com.google.android.material.bottomnavigation.BottomNavigationView

// TODO: Rename parameter arguments, choose names that match
// the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
private const val ARG_PARAM1 = "param1"
private const val ARG_PARAM2 = "param2"

/**
 * A simple [Fragment] subclass.
 * Use the [FundTransfer3.newInstance] factory method to
 * create an instance of this fragment.
 */
class FundTransfer3 : Fragment() {
    // TODO: Rename and change types of parameters
    private var param1: String? = null
    private var param2: String? = null

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
        val myView = inflater.inflate(R.layout.fragment_fund_transfer3, container, false)
        val mainView = inflater.inflate(R.layout.activity_main_page, container, false)
        val btnDone: Button = myView.findViewById(R.id.button_transfer3Done)
        btnDone.setOnClickListener {
            val nav = activity?.findViewById<BottomNavigationView>(R.id.bottomNavigation_mainNav)
//            nav?.menu?.findItem(R.id.menu_home)?.isChecked = true
            for (i in 0 until nav?.menu?.size()!!){
                nav.menu.getItem(i).isEnabled = true
                nav.menu.getItem(i).isCheckable = true
            }
            nav?.selectedItemId = R.id.menu_home
            nav?.performClick()
        }
        return myView
    }

    companion object {
        /**
         * Use this factory method to create a new instance of
         * this fragment using the provided parameters.
         *
         * @param param1 Parameter 1.
         * @param param2 Parameter 2.
         * @return A new instance of fragment FundTransfer3.
         */
        // TODO: Rename and change types and number of parameters
        @JvmStatic
        fun newInstance(param1: String, param2: String) =
            FundTransfer3().apply {
                arguments = Bundle().apply {
                    putString(ARG_PARAM1, param1)
                    putString(ARG_PARAM2, param2)
                }
            }
    }
}