package com.atharav.expensetracker.ui.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.atharav.expensetracker.data.local.Expense
import com.atharav.expensetracker.databinding.ItemExpenseBinding

class ExpenseAdapter(
    private val onClick: (Expense) -> Unit
) : ListAdapter<Expense, ExpenseAdapter.VH>(Diff) {

    object Diff : DiffUtil.ItemCallback<Expense>() {
        override fun areItemsTheSame(oldItem: Expense, newItem: Expense) = oldItem.id == newItem.id
        override fun areContentsTheSame(oldItem: Expense, newItem: Expense) = oldItem == newItem
    }

    inner class VH(val b: ItemExpenseBinding) : RecyclerView.ViewHolder(b.root)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): VH {
        val b = ItemExpenseBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return VH(b)
    }

    override fun onBindViewHolder(holder: VH, position: Int) {
        val item = getItem(position)
        holder.b.tvTitle.text = item.title
        holder.b.tvCategory.text = item.category
        holder.b.tvAmount.text = "₹${item.amount}"
        holder.b.root.setOnClickListener { onClick(item) }
    }
}
