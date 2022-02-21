package com.example.mobilebanking.adapter

import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentActivity
import androidx.viewpager2.adapter.FragmentStateAdapter
import com.example.mobilebanking.ReceiverHistory
import com.example.mobilebanking.SenderHistory

class TransactionPagerAdapter(fa : FragmentActivity, var totalTabs : Int) : FragmentStateAdapter(fa) {
    override fun getItemCount(): Int = totalTabs

    override fun createFragment(position: Int): Fragment {
        return when (position) {
            0 -> SenderHistory()
            1 -> ReceiverHistory()
            else -> createFragment(position)
        }
    }
}