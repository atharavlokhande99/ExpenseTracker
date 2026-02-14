package com.atharav.expensetracker

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import androidx.activity.result.contract.ActivityResultContracts
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.atharav.expensetracker.data.local.ExpenseDatabase
import com.atharav.expensetracker.data.repository.ExpenseRepository
import com.atharav.expensetracker.databinding.ActivityMainBinding
import com.atharav.expensetracker.ui.AddExpenseActivity
import com.atharav.expensetracker.ui.adapter.ExpenseAdapter
import com.atharav.expensetracker.ui.viewmodel.ExpenseViewModel
import com.atharav.expensetracker.ui.viewmodel.ExpenseViewModelFactory
import kotlinx.coroutines.launch

class MainActivity : AppCompatActivity() {

    private lateinit var b: ActivityMainBinding
    private lateinit var adapter: ExpenseAdapter

    private val viewModel: ExpenseViewModel by viewModels {
        val dao = ExpenseDatabase.getDatabase(applicationContext).expenseDao()
        val repo = ExpenseRepository(dao)
        ExpenseViewModelFactory(repo)
    }

    private val addEditLauncher =
        registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { res ->
            if (res.resultCode == Activity.RESULT_OK) {
                val data = res.data ?: return@registerForActivityResult
                val id = data.getLongExtra("id", 0L)
                val title = data.getStringExtra("title") ?: return@registerForActivityResult
                val category = data.getStringExtra("category") ?: return@registerForActivityResult
                val amount = data.getDoubleExtra("amount", 0.0)

                if (id == 0L) viewModel.addExpense(title, category, amount)
                else viewModel.updateExpense(id, title, category, amount)
            }
        }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        b = ActivityMainBinding.inflate(layoutInflater)
        setContentView(b.root)

        adapter = ExpenseAdapter { expense ->
            // click -> edit
            val i = Intent(this, AddExpenseActivity::class.java).apply {
                putExtra("id", expense.id)
                putExtra("title", expense.title)
                putExtra("category", expense.category)
                putExtra("amount", expense.amount)
            }
            addEditLauncher.launch(i)
        }

        b.rvExpenses.layoutManager = LinearLayoutManager(this)
        b.rvExpenses.adapter = adapter

        // collect list
        lifecycleScope.launch {
            viewModel.expenses.collect { list ->
                adapter.submitList(list)
            }
        }

        // FAB -> add
        b.fabAdd.setOnClickListener {
            addEditLauncher.launch(Intent(this, AddExpenseActivity::class.java))
        }

        // swipe delete
        ItemTouchHelper(object : ItemTouchHelper.SimpleCallback(0, ItemTouchHelper.LEFT or ItemTouchHelper.RIGHT) {
            override fun onMove(
                rv: RecyclerView, vh: RecyclerView.ViewHolder, target: RecyclerView.ViewHolder
            ) = false

            override fun onSwiped(vh: RecyclerView.ViewHolder, dir: Int) {
                val expense = adapter.currentList[vh.adapterPosition]
                viewModel.deleteExpense(expense)
            }
        }).attachToRecyclerView(b.rvExpenses)
    }
}
