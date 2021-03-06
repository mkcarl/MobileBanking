package com.example.mobilebanking

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.RecyclerView
import com.example.mobilebanking.adapter.TransactionAdapter

// TODO: Rename parameter arguments, choose names that match
// the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
private const val ARG_PARAM1 = "param1"
private const val ARG_PARAM2 = "param2"

/**
 * A simple [Fragment] subclass.
 * Use the [ReceiverHistory.newInstance] factory method to
 * create an instance of this fragment.
 */
class ReceiverHistory : Fragment() {
    // TODO: Rename and change types of parameters
    private var param1: String? = null
    private var param2: String? = null
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
        val myView  = inflater.inflate(R.layout.fragment_receiver_history, container, false)

        val recyclerTransaction = myView.findViewById<RecyclerView>(R.id.recycle_receiverHistory)
        val txtNotFound = myView.findViewById<TextView>(R.id.text_receiverNoTransaction)

        model.getReceivedTransactions().observe(viewLifecycleOwner, Observer{ tList ->
            if (tList.isEmpty()){
                txtNotFound.visibility = View.VISIBLE
                recyclerTransaction.visibility = View.INVISIBLE
            }else {
                txtNotFound.visibility = View.INVISIBLE
                recyclerTransaction.visibility = View.VISIBLE
                recyclerTransaction.adapter = TransactionAdapter(tList)

            }
        })

        return myView
    }

    companion object {
        /**
         * Use this factory method to create a new instance of
         * this fragment using the provided parameters.
         *
         * @param param1 Parameter 1.
         * @param param2 Parameter 2.
         * @return A new instance of fragment ReceiverHistory.
         */
        // TODO: Rename and change types and number of parameters
        @JvmStatic
        fun newInstance(param1: String, param2: String) =
            ReceiverHistory().apply {
                arguments = Bundle().apply {
                    putString(ARG_PARAM1, param1)
                    putString(ARG_PARAM2, param2)
                }
            }
    }
}