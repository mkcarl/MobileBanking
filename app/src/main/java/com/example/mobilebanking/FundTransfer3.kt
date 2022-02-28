package com.example.mobilebanking

import android.graphics.Bitmap
import android.graphics.Canvas
import android.graphics.pdf.PdfDocument
import android.os.Build
import android.os.Bundle
import android.util.DisplayMetrics
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.fragment.app.Fragment
import com.example.mobilebanking.entities.Transaction
import com.google.android.material.bottomnavigation.BottomNavigationView
import java.io.File
import java.io.FileOutputStream
import java.text.SimpleDateFormat
import java.time.format.DateTimeFormatter

// the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
private const val ARG_PARAM1 = "param1"
private const val ARG_PARAM2 = "param2"

/**
 * A simple [Fragment] subclass.
 * Use the [FundTransfer3.newInstance] factory method to
 * create an instance of this fragment.
 */
class FundTransfer3 : Fragment() {
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
        val transactionInvolved : Transaction? = requireArguments().getParcelable("transaction")
        val transferSuccess : Boolean = requireArguments().getBoolean("transfer_success")
        val myView = inflater.inflate(R.layout.fragment_fund_transfer3, container, false)
        val mainView = inflater.inflate(R.layout.activity_main_page, container, false)
        val btnDone: Button = myView.findViewById(R.id.button_transfer3Done)
        val btnPDF : Button = myView.findViewById(R.id.button_transfer3Pdf)
        val btnFav : Button = myView.findViewById(R.id.button_transfer3Favourite)

        btnDone.setOnClickListener {
            val nav = activity?.findViewById<BottomNavigationView>(R.id.bottomNavigation_mainNav)
//            nav?.menu?.findItem(R.id.menu_home)?.isChecked = true
            for (i in 0 until nav?.menu?.size()!!){
                nav.menu.getItem(i).isEnabled = true
                nav.menu.getItem(i).isCheckable = true
            }
            nav.selectedItemId = R.id.menu_home
            nav.performClick()
        }

        val imgIcon = myView.findViewById<ImageView>(R.id.img_transfer3TransferIcon)
        val txtStatus = myView.findViewById<TextView>(R.id.text_transfer3TransferStatus)

        if (transferSuccess){
            imgIcon.setImageResource(R.drawable.icon_done_transfer)
            txtStatus.text = getString(
                R.string.transfer3_transfer_success,
                transactionInvolved!!.amount,
                transactionInvolved.receiver_acc
            )
        }
        else {
            imgIcon.setImageResource(R.drawable.icon_fail_transfer)
            txtStatus.text = getString(
                R.string.transfer3_transfer_fail,
                transactionInvolved!!.amount,
                transactionInvolved.receiver_acc
            )
            btnFav.visibility = View.INVISIBLE
            btnPDF.visibility = View.INVISIBLE

        }
        btnPDF.setOnClickListener {
            if (transferSuccess) {
                val inflater = LayoutInflater.from(context)
                val formatter = SimpleDateFormat("dd/MM/yyyy HH:mm:ss")
                val view = inflater.inflate(R.layout.transaction_receipt_template, null)

                val txtDate: TextView = view.findViewById(R.id.text_PDFTEMPLATEdate)
                val txtSender: TextView = view.findViewById(R.id.text_PDFTEMPLATEsender)
                val txtReceiver: TextView = view.findViewById(R.id.text_PDFTEMPLATEreceiver)
                val txtAmount: TextView = view.findViewById(R.id.text_PDFTEMPLATEamount)

                txtDate.text = getString(
                    R.string.PDFTEMPLATE_date,
                    formatter.format(transactionInvolved.datetime.toDate())
                )
                txtSender.text =
                    getString(R.string.PDFTEMPLATE_sender, transactionInvolved.sender_acc)
                txtReceiver.text =
                    getString(R.string.PDFTEMPLATE_receiver, transactionInvolved.receiver_acc)
                txtAmount.text = getString(R.string.PDFTEMPLATE_amount, transactionInvolved.amount)

                val displayMetrics = DisplayMetrics()
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.R) {
                    requireContext().display?.getRealMetrics(displayMetrics)
                    displayMetrics.densityDpi
                }
                else{
                    requireActivity().windowManager.defaultDisplay.getMetrics(displayMetrics)
                }
                view.measure(
                    View.MeasureSpec.makeMeasureSpec(
                        displayMetrics.widthPixels, View.MeasureSpec.EXACTLY
                    ),
                    View.MeasureSpec.makeMeasureSpec(
                        displayMetrics.heightPixels, View.MeasureSpec.EXACTLY
                    )
                )

                view.layout(0,0,displayMetrics.widthPixels, displayMetrics.heightPixels)
                val bitmap = Bitmap.createBitmap(view.measuredWidth, view.measuredHeight, Bitmap.Config.ARGB_8888)
                val canvas = Canvas(bitmap)
                view.draw(canvas)

                val scaledBitmap = Bitmap.createScaledBitmap(bitmap, 595, 842, true)

                val pdfDocument = PdfDocument()
                val pageInfo = PdfDocument.PageInfo.Builder(595, 842, 1).create()
                val page = pdfDocument.startPage(pageInfo)
                page.canvas.drawBitmap(scaledBitmap, 0F, 0F, null)
                pdfDocument.finishPage(page)
                val filePath = File(requireContext().getExternalFilesDir(null), "${transactionInvolved.datetime.toDate()}.pdf")
                pdfDocument.writeTo(FileOutputStream(filePath))
                pdfDocument.close()

                Toast.makeText(
                    context, "Receipt saved.",
                    Toast.LENGTH_SHORT
                ).show()
                btnPDF.isEnabled = false
            }
        }
        btnFav.setOnClickListener {
            Toast.makeText(
                context, "Feature have not been implemented",
                Toast.LENGTH_SHORT
            ).show()
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