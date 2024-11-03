package com.example.fitgether.data.model

import androidx.annotation.DrawableRes
import com.example.fitgether.R

data class Category(
    val categoryTitle : String,
    val categoryImage : Int
)


val categoryList = listOf(
    Category(categoryTitle = "Doğa Yürüyüşü,Trekking", categoryImage = R.drawable.trekking ),
    Category(categoryTitle = "Tırmanma ve Kaya Tırmanışı", categoryImage = R.drawable.climbing ),
    Category(categoryTitle = "Bisiklet Turları", categoryImage = R.drawable.bicycle ),
    Category(categoryTitle = "Koşu ve Maraton", categoryImage = R.drawable.marathon ),
    Category(categoryTitle = "Su Sporları", categoryImage = R.drawable.skating ),
    Category(categoryTitle = "Su Altı Sporları", categoryImage = R.drawable.scuba_diving ),
    Category(categoryTitle = "Kamp ve Kampçılık Aktiviteleri", categoryImage = R.drawable.camping ),
    Category(categoryTitle = "Yoga ve Fitness", categoryImage = R.drawable.yoga ),
    Category(categoryTitle = "Kış Sporları", categoryImage = R.drawable.snow_skating  ),
    Category(categoryTitle = "Takım Sporları ve Etkinlikler", categoryImage = R.drawable.team_sports ),
    Category(categoryTitle = "Açık Hava Etkinlikleri", categoryImage = R.drawable.outdoor ),
    Category(categoryTitle = "Rüzgar ve Uçuş Sporları", categoryImage = R.drawable.bungee_jumping ),
    Category(categoryTitle = "Diğer", categoryImage = R.drawable.other_card_bg )
)
