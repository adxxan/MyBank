package com.geeks.mybank.domain.presenter

import com.geeks.mybank.data.model.Account

class AccountPresenter(private val view: AccountContract.View): AccountContract.Presenter {
    override fun loadAccounts() {

        val testAccountList = listOf(
            Account(
                id = "1",
                name = "O!Bank",
                balance = "1000",
                currency = "USD",
                isActive = true
            ),
            Account(
                id = "2",
                name = "mBank",
                balance = "10000",
                currency = "KGS",
                isActive = true
            ),
            Account(
                id = "3",
                name = "simBank",
                balance = "100",
                currency = "EUR",
                isActive = true
            )
        )
        view.showAccounts(testAccountList)

    }
}