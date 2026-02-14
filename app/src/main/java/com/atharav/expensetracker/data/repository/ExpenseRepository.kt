package com.atharav.expensetracker.data.repository

import com.atharav.expensetracker.data.local.Expense
import com.atharav.expensetracker.data.local.ExpenseDao
import kotlinx.coroutines.flow.Flow

class ExpenseRepository(private val dao: ExpenseDao) {
    val expenses: Flow<List<Expense>> = dao.getAll()

    suspend fun add(expense: Expense) = dao.insert(expense)
    suspend fun update(expense: Expense) = dao.update(expense)
    suspend fun delete(expense: Expense) = dao.delete(expense)
    suspend fun deleteById(id: Long) = dao.deleteById(id)
}
