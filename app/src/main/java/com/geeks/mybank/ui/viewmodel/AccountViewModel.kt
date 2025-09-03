package com.geeks.mybank.ui.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.geeks.mybank.data.model.Account
import com.geeks.mybank.data.model.AccountState
import com.geeks.mybank.data.network.AccountApi
import dagger.hilt.android.lifecycle.HiltViewModel
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import javax.inject.Inject

@HiltViewModel
class AccountViewModel @Inject constructor(
    private val accountApi: AccountApi
): ViewModel() {

    private val _accounts = MutableLiveData<List<Account>>()
    val accounts: LiveData<List<Account>> = _accounts

    private val _errorMessage = MutableLiveData<String>()
    val errorMessage: LiveData<String> = _errorMessage

    private val _successMessage = MutableLiveData<String>()
    val successMessage: LiveData<String> = _successMessage


    fun loadAccounts() {
        accountApi.getAccounts().handleResponce(
            onSuccess = {_accounts.value = it},
            onError = {errorMsg -> _errorMessage.value = errorMsg}
        )
    }

    fun addAccounts(account: Account) {
        accountApi.addAccount(account).handleResponce(
            onSuccess = {loadAccounts()},
            onError = {errorMsg -> _errorMessage.value = errorMsg}
        )
    }

    fun deleteAccounts(accountId: String) {
        accountApi.deleteAccount(accountId).handleResponce(
            onSuccess = {loadAccounts()},
            onError = {errorMsg -> _errorMessage.value = errorMsg}
        )
    }

    fun updateAccountsFully(account: Account) {
        accountApi.updateAccountFully(account.id ?: "", account).handleResponce(
            onSuccess = {loadAccounts()},
            onError = {errorMsg -> _errorMessage.value = errorMsg}
        )
    }

    fun patchAccountStatus(accountId: String, isActive: Boolean) {
        accountApi.patchAccountStatus(accountId, AccountState(isActive)).handleResponce(
            onSuccess = {
                loadAccounts()
                _successMessage.value = if (isActive) {
                    "Счёт активирован"
                } else {
                    "Счёт деактивирован"
                }
            },
            onError = { errorMsg -> _errorMessage.value = errorMsg }
        )
    }


    fun <T> Call<T>.handleResponce(
        onSuccess: (T) -> Unit,
        onError: (String) -> Unit = {}
    ){
        this.enqueue(object : Callback<T> {
            override fun onResponse(call: Call<T>, response: Response<T>) {
                val resultBody = response.body()
                if(response.isSuccessful && resultBody != null){
                    onSuccess(resultBody)
                }else{
                    onError(response.code().toString())
                }
            }

            override fun onFailure(call: Call<T>, t: Throwable) {
                onError(t.message.toString())
            }
        })
    }
}