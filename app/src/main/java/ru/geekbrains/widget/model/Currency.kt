package ru.geekbrains.widget.model

data class Currency(
    val privacy: String,
    val quotes: Quotes,
    val source: String,
    val success: Boolean,
    val terms: String,
    val timestamp: Int
)