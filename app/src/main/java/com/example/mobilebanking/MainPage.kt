package com.example.mobilebanking

import MyViewModel
import android.content.DialogInterface
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import androidx.activity.viewModels
import androidx.appcompat.app.AlertDialog
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentTransaction
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.google.android.material.navigation.NavigationBarMenuView

private const val TAG = "MainPage"
class MainPage : AppCompatActivity() {
    // TODO : cache pages using fragment manager
    private fun setCurrentFragment(fragment:Fragment)=
        supportFragmentManager.beginTransaction().apply {
            replace(R.id.linear_main,fragment)
            commit()
        }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main_page)

        val username = intent.getStringExtra("username")
        if (username != null) {
            Log.d(TAG, username)
        }


        // https://www.geeksforgeeks.org/bottom-navigation-bar-in-android/
        val nav = findViewById<BottomNavigationView>(R.id.bottomNavigation_mainNav)
        val fragHomePage: HomePage = HomePage()
        val fragFundTransfer: FundTransfer1 = FundTransfer1()
        val fragHistory: TransactionHistoryPage = TransactionHistoryPage()
        val fragPayBills: PayBillsPage = PayBillsPage()
        val fragLiveChat: LiveChatPage = LiveChatPage()
        val model: MyViewModel by viewModels()
        intent.getStringExtra("username")?.let { model.setUsername(it) }
        intent.getStringExtra("account_number")?.let { model.setAccountNumber(it) }
        intent.getDoubleExtra("balance", -1.0).let { model.setBalance(it) }
        intent.getStringExtra("bank_name")?.let { model.setBankName(it) }
        setCurrentFragment(fragHomePage)

        nav.setOnItemSelectedListener { item ->
            when (item.itemId) {
                R.id.menu_home -> {
                    setCurrentFragment(fragHomePage)
                    true
                }
                R.id.menu_fund_transfer -> {
                    setCurrentFragment(fragFundTransfer)
                    true
                }
                R.id.menu_history -> {
                    setCurrentFragment(fragHistory)
                    true
                }
                R.id.menu_pay_bills -> {
                    setCurrentFragment(fragPayBills)
                    true
                }
                R.id.menu_live_chat -> {
                    setCurrentFragment(fragLiveChat)
                    true
                }
                else -> false
            }

        }

    }

    override fun onBackPressed() {
        // https://stackoverflow.com/a/59935762
        AlertDialog.Builder(this)
            .setTitle("Quit")
            .setMessage("Are you sure you want to log out?")
            .setCancelable(false)
            .setPositiveButton("Yes") {
                    dialog: DialogInterface, _: Int ->
                dialog.dismiss()
                // for sending data to previous activity use
                // setResult(response code, data)
                finish()
            }
            .setNegativeButton("No") {
                    dialog: DialogInterface, _: Int ->
                dialog.dismiss()
            }
            .show()


    }

}
