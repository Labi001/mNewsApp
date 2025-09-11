package com.example.mnewsapp.domain

import androidx.annotation.DrawableRes
import com.example.mnewsapp.R

data class Page(

    val title: String,
    val description: String,
    @DrawableRes val image: Int

)

val pages = listOf(

    Page(

        title = "Stay Informed, Always",
        description = "Get the latest news from trusted sources—delivered in real-time, right to your fingertips.",
        image = R.drawable.onboarding1

    ),

    Page(

        title = "Personalized for You",
        description = "Customize your feed to follow topics that matter most—politics, tech, sports, or entertainment.",
        image = R.drawable.onboarding2

    ),

    Page(

        title = "News that Moves with You",
        description = "Whether you're commuting or relaxing, enjoy a seamless reading experience anytime, anywhere.",
        image = R.drawable.onboarding3

    )

)