package com.example.titossycleaningservicesapp.core

data class FAQItem(
    val question: String,
    val answer: String,
    val isExpanded: Boolean = false
)
