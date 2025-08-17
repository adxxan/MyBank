package com.geeks.mybank.domain.presenter

import android.adservices.adid.AdId
import com.geeks.mybank.data.model.Account

interface AccountContract {
    interface View{
        fun showAccounts(list: List<Account>)
    }

    interface Presenter{
        fun loadAccounts()
        fun addAccounts(account: Account)
        fun deleteAccounts(accountId: String)
        fun updateAccountsFully(account: Account)
        fun patchAccountStatus(accountId: String, isActive: Boolean)
    }
}