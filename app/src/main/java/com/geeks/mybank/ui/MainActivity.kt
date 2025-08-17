package com.geeks.mybank.ui

import android.os.Bundle
import androidx.activity.enableEdgeToEdge
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

    override fun showAccounts(list: List<Account>) {
        adapter.submitList(list)

    }
}