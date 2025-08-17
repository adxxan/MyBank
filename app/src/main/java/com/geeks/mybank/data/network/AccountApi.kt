package com.geeks.mybank.data.network

import com.geeks.mybank.data.model.Account
import retrofit2.Call
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.POST

interface AccountApi {

    @GET("account")
    fun getAccounts(): Call<List<Account>>

    @POST ("account")
    fun addAccount(@Body account: Account): Call<Unit>

}