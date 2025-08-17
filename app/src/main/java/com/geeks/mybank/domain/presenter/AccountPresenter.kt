package com.geeks.mybank.domain.presenter

import com.geeks.mybank.data.model.Account
import com.geeks.mybank.data.network.ApiClient
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response


class AccountPresenter(private val view: AccountContract.View): AccountContract.Presenter {
    override fun loadAccounts() {
        ApiClient.accountApi.getAccounts().enqueue(object: Callback<List<Account>>{
            override fun onResponse(call: Call<List<Account>>, response: Response<List<Account>>) {
                view.showAccounts(response.body() ?: listOf())
            }

            override fun onFailure(call: Call<List<Account>>, t: Throwable) {
                TODO("Not yet implemented")
            }

        })
    }

    override fun addAccounts(account: Account) {
        ApiClient.accountApi.addAccount(account).enqueue(object: Callback<Unit>{
            override fun onResponse(call: Call<Unit>, response: Response<Unit>) {
                loadAccounts()
            }

            override fun onFailure(call: Call<Unit>, t: Throwable) {
                TODO("Not yet implemented")
            }

        })
    }
}