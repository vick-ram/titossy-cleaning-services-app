package com.example.titossycleaningservicesapp.core

import com.example.titossycleaningservicesapp.R

class OnboardingItems(
    val title: String,
    val description: String,
    val image: Int
) {

    companion object {
        fun getData(): List<OnboardingItems> {
            return listOf(
                OnboardingItems(
                    "Welcome to TitosSy Cleaning Services",
                    "We provide the best cleaning services in the city. Our team of professionals will make your home shine.",
                    R.drawable.onboarding1
                ),
                OnboardingItems(
                    "Book a Service",
                    "Book a cleaning service in just a few clicks. Choose the date and time that works best for you.",
                    R.drawable.onboarding2
                ),
                OnboardingItems(
                    "Enjoy Your Clean Home",
                    "Sit back and relax while our team of professionals clean your home. Enjoy your clean home.",
                    R.drawable.onboarding3
                )
            )
        }
    }
}