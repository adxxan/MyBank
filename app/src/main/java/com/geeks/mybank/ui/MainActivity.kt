package com.geeks.mybank.ui

import android.os.Bundle
import android.view.LayoutInflater
import android.widget.EditText
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.recyclerview.widget.LinearLayoutManager
import com.geeks.mybank.R
import com.geeks.mybank.data.model.Account
import com.geeks.mybank.databinding.ActivityMainBinding
import com.geeks.mybank.domain.presenter.AccountContract
import com.geeks.mybank.domain.presenter.AccountPresenter
import com.geeks.mybank.ui.adapter.AccountAdapter

class MainActivity : AppCompatActivity(), AccountContract.View {

    private var _binding: ActivityMainBinding? = null
    private val binding get() = _binding!!

    private lateinit var adapter: AccountAdapter
    private lateinit var presenter: AccountContract.Presenter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        _binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
        initAdapter()
        initClicks()

        presenter = AccountPresenter(this)
    }

    override fun onResume() {
        super.onResume()
        presenter.loadAccounts()
    }

    private fun initAdapter() = with(binding){
        adapter = AccountAdapter()
        recyclerView.layoutManager = LinearLayoutManager(this@MainActivity)
        recyclerView.adapter = adapter
    }

    private fun initClicks() = with(binding){
        btnAdd.setOnClickListener{showAddDialog()}
    }

    override fun showAccounts(list: List<Account>) {
        adapter.submitList(list)

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

                    presenter.addAccounts(account)

                }

                .setNegativeButton("Отмена", null)
                .show()
        }
    }
}