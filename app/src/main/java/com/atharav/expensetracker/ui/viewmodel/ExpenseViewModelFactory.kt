package com.atharav.expensetracker.ui.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.atharav.expensetracker.data.repository.ExpenseRepository

class ExpenseViewModelFactory(private val repo: ExpenseRepository) : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        return ExpenseViewModel(repo) as T
    }
}
