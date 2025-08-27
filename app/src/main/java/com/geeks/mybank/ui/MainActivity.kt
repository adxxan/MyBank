package com.geeks.mybank.ui

import android.os.Bundle
import android.view.LayoutInflater
import android.widget.EditText
import android.widget.Toast
import androidx.activity.viewModels
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModel
import androidx.recyclerview.widget.LinearLayoutManager
import com.geeks.mybank.R
import com.geeks.mybank.data.model.Account
import com.geeks.mybank.databinding.ActivityMainBinding
import com.geeks.mybank.ui.adapter.AccountAdapter
import com.geeks.mybank.ui.viewmodel.AccountViewModel
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : AppCompatActivity(){

    private var _binding: ActivityMainBinding? = null
    private val binding get() = _binding!!

    private lateinit var adapter: AccountAdapter
    private val viewModel: AccountViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        _binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
        initAdapter()
        initClicks()
        subscribeToLiveData()

    }

    override fun onResume() {
        super.onResume()
        viewModel.loadAccounts()
    }

    private fun initAdapter() = with(binding){
        adapter = AccountAdapter(
            onDelete = {id ->
                viewModel.deleteAccounts(id)
            },
            onEdit = {account ->
                showEditDialog(account)
            },
            onStatusToggle = {id, isChecked -> Unit
                viewModel.patchAccountStatus(id, isChecked)
            }
        )
        recyclerView.layoutManager = LinearLayoutManager(this@MainActivity)
        recyclerView.adapter = adapter
    }

    private fun initClicks() = with(binding){
        btnAdd.setOnClickListener{showAddDialog()}
    }

    private fun subscribeToLiveData(){
        viewModel.accounts.observe(this){
            adapter.submitList(it)
        }
        viewModel.errorMessage.observe(this) { error ->
            AlertDialog.Builder(this)
                .setTitle("Ошибка")
                .setMessage(error)
                .setPositiveButton("OK", null)
                .show()
        }
        viewModel.successMessage.observe(this) { message ->
            Toast.makeText(this, message, Toast.LENGTH_SHORT).show()
        }
    }

    private fun showAddDialog(){
        val dialogView = LayoutInflater.from(this).inflate(R.layout.dialog_add_account, null)
        with(dialogView){
            val nameInput = findViewById<EditText>(R.id.etName)
            val balanceInput = findViewById<EditText>(R.id.etBalance)
            val currencyInput = findViewById<EditText>(R.id.etCurrency)

            AlertDialog.Builder(this@MainActivity)
                .setTitle("Добавить счет")
                .setView(this)
                .setPositiveButton("Добавить"){_,_, ->

                    val account = Account(
                        name = nameInput.text.toString(),
                        balance = balanceInput.text.toString().toInt(),
                        currency = currencyInput.text.toString(),
                        isActive = true
                    )

                    viewModel.addAccounts(account)

                }

                .setNegativeButton("Отмена", null)
                .show()
        }
    }

    private fun showEditDialog(account: Account ){
        val dialogView = LayoutInflater.from(this).inflate(R.layout.dialog_add_account, null)
        with(dialogView){
            val nameInput = findViewById<EditText>(R.id.etName)
            val balanceInput = findViewById<EditText>(R.id.etBalance)
            val currencyInput = findViewById<EditText>(R.id.etCurrency)

            nameInput.setText(account.name)
            balanceInput.setText(account.balance.toString())
            currencyInput.setText(account.currency)

            AlertDialog.Builder(this@MainActivity)
                .setTitle("Редактировать счет")
                .setView(this)
                .setPositiveButton("Обновить"){_,_, ->

                    val updateAccount = account.copy(
                        name = nameInput.text.toString(),
                        balance = balanceInput.text.toString().toInt(),
                        currency = currencyInput.text.toString(),
                    )

                    viewModel.updateAccountsFully(updateAccount)
                }

                .setNegativeButton("Отмена", null)
                .show()
        }
    }
}