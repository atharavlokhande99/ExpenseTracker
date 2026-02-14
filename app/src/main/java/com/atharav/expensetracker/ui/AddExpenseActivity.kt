package com.atharav.expensetracker.ui

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.atharav.expensetracker.databinding.ActivityAddExpenseBinding

class AddExpenseActivity : AppCompatActivity() {

    private lateinit var b: ActivityAddExpenseBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        b = ActivityAddExpenseBinding.inflate(layoutInflater)
        setContentView(b.root)

        // If editing, prefill
        val id = intent.getLongExtra("id", 0L)
        if (id != 0L) {
            b.etTitle.setText(intent.getStringExtra("title") ?: "")
            b.etCategory.setText(intent.getStringExtra("category") ?: "")
            b.etAmount.setText(intent.getDoubleExtra("amount", 0.0).toString())
            b.btnSave.text = "Update"
        }

        b.btnSave.setOnClickListener {
            val title = b.etTitle.text.toString().trim()
            val category = b.etCategory.text.toString().trim()
            val amountStr = b.etAmount.text.toString().trim()

            if (title.isEmpty() || category.isEmpty() || amountStr.isEmpty()) {
                b.etTitle.error = "Required"
                return@setOnClickListener
            }

            val amount = amountStr.toDoubleOrNull()
            if (amount == null) {
                b.etAmount.error = "Enter valid number"
                return@setOnClickListener
            }

            val data = Intent().apply {
                putExtra("id", id)
                putExtra("title", title)
                putExtra("category", category)
                putExtra("amount", amount)
            }
            setResult(Activity.RESULT_OK, data)
            finish()
        }
    }
}
