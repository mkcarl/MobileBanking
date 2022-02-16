package com.example.mobilebanking

import android.content.DialogInterface
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import androidx.appcompat.app.AlertDialog
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentTransaction
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.google.android.material.navigation.NavigationBarMenuView

class MainPage : AppCompatActivity() {

    private fun setCurrentFragment(fragment:Fragment)=
        supportFragmentManager.beginTransaction().apply {
            replace(R.id.linear_main,fragment)
            commit()
        }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main_page)

        val username = intent.getStringExtra("username")
        val password = intent.getStringExtra("password")

        // https://www.geeksforgeeks.org/bottom-navigation-bar-in-android/
        val nav = findViewById<BottomNavigationView>(R.id.bottomNavigation_mainNav)
        val fragHomePage: HomePage = HomePage().apply { arguments = Bundle().apply { putString("username", username) } }
        val fragFundTransfer: FundTransfer1 = FundTransfer1()
        val fragHistory: TransactionHistoryPage = TransactionHistoryPage()
        val fragPayBills: PayBillsPage = PayBillsPage()
        val fragLiveChat: LiveChatPage = LiveChatPage()

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
