package com.atharav.expensetracker.ui.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.atharav.expensetracker.data.local.Expense
import com.atharav.expensetracker.data.repository.ExpenseRepository
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch

class ExpenseViewModel(private val repo: ExpenseRepository) : ViewModel() {

    val expenses = repo.expenses.stateIn(
        viewModelScope,
        SharingStarted.WhileSubscribed(5000),
        emptyList()
    )

    fun addExpense(title: String, category: String, amount: Double) {
        viewModelScope.launch {
            repo.add(Expense(title = title, category = category, amount = amount))
        }
    }

    fun updateExpense(id: Long, title: String, category: String, amount: Double) {
        viewModelScope.launch {
            repo.update(Expense(id = id, title = title, category = category, amount = amount))
        }
    }

    fun deleteExpense(expense: Expense) {
        viewModelScope.launch { repo.delete(expense) }
    }
}
