package com.geeks.mybank.data.model

data class Account (
    val id: String? = null,
    val name: String,
    val balance: Int,
    val currency: String,
    val isActive: Boolean
)