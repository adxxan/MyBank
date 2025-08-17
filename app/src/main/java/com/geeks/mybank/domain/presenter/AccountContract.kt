package com.geeks.mybank.domain.presenter

import com.geeks.mybank.data.model.Account

interface AccountContract {
    interface View{
        fun showAccounts(list: List<Account>)
    }

    interface Presenter{
        fun loadAccounts()
        fun addAccounts(account: Account)
    }
}