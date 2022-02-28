package com.example.mobilebanking.data

import com.example.mobilebanking.entities.News

class NewsDatasource {

    fun loadNews():List<News>{
        return listOf<News>(
            News("Happy CNY", "Happy CNY", "https://cdn.discordapp.com/attachments/910082738638430251/942109300015382538/Happy_CNY.jpg"),
            News("Beware", "Beware of scam", "https://cdn.discordapp.com/attachments/910082738638430251/947451715337195520/banners.png"),
            News("New ATM", "New atm", "https://cdn.discordapp.com/attachments/910082738638430251/947452068791205988/banners_1.png")

        )
    }

}