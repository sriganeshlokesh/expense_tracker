package com.example.inclass11;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

public class ShowExpenseActivity extends AppCompatActivity {


    private TextView tv_expense_name;
    private TextView tv_expense_category;
    private TextView tv_expense_amount;
    private TextView tv_expense_date;
    private Button bt_edit;
    private Button bt_cancel;

    Expense expense;

    String name;
    String category;
    String amount;
    String date;
    String cat_id;
    String id;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_show_expense);
        setTitle("Show Expense");

        tv_expense_name = findViewById(R.id.tv_name_display);
        tv_expense_category = findViewById(R.id.tv_display_category);
        tv_expense_amount = findViewById(R.id.tv_display_amount);
        tv_expense_date = findViewById(R.id.tv_display_date);
        bt_edit = findViewById(R.id.bt_edit);
        bt_cancel = findViewById(R.id.bt_cancel);

        if(!getIntent().getExtras().equals(null)){

            expense = (Expense) getIntent().getSerializableExtra("expense");

            id = expense.getId();
            name = expense.getExpense();
            category = expense.getCategory();
            amount = String.valueOf(expense.getAmount());
            date = expense.getDate();

            tv_expense_name.setText(name);
            tv_expense_category.setText(category);
            tv_expense_amount.setText(amount);
            tv_expense_date.setText(date);

            cat_id = expense.getCat_id();
            Log.d("expense_id", "onCreate: "+expense.getId());
            Log.d("old", "onCreate: "+cat_id);

        }
        else{
            Toast.makeText(this, "Content Not Found", Toast.LENGTH_SHORT).show();
        }

        bt_cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        bt_edit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Expense e = new Expense(name,Integer.parseInt(amount), category,date);
                e.setId(id);
                e.setCat_id(cat_id);
                Intent i = new Intent(ShowExpenseActivity.this, EditExpenseActivity.class);
                i.putExtra("edit", e);
                startActivity(i);

            }
        });

    }
}
