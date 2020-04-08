package com.example.inclass11;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.Timestamp;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class AddExpenseActivity extends AppCompatActivity implements AdapterView.OnItemSelectedListener {

    private EditText et_expense;
    private EditText et_amount;
    private Spinner spinner;
    private Button bt_add;
    private Button bt_cancel;

    private FirebaseFirestore db;

    String expense_name;
    String categoryValue;
    int amount;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_expense);
        setTitle("Add Expense");

        db = FirebaseFirestore.getInstance();

        et_expense = findViewById(R.id.et_expense);
        et_amount = findViewById(R.id.et_edit_amount);
        spinner = findViewById(R.id.spinner);
        bt_add = findViewById(R.id.bt_add);
        bt_cancel = findViewById(R.id.bt_cancel);

        spinner.setOnItemSelectedListener(this);

        List<String> categories = new ArrayList<String>();
        categories.add("Groceries");
        categories.add("Invoice");
        categories.add("Transportation");
        categories.add("Shopping");
        categories.add("Rent");
        categories.add("Trips");
        categories.add("Utilities");
        categories.add("Other");

        // Creating adapter for spinner
        ArrayAdapter<String> dataAdapter = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, categories);

        // Drop down layout style - list view with radio button
        dataAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

        // attaching data adapter to spinner
        spinner.setAdapter(dataAdapter);

        bt_cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        bt_add.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if(et_expense.getText().toString().equals("") || !et_expense.getText().toString().matches("[a-zA-Z_]+")){
                    et_expense.setError("Enter Valid Expense Name");
                    Toast.makeText(AddExpenseActivity.this, "Expense Name Missing", Toast.LENGTH_SHORT).show();
                    return;
                }
                else{
                    expense_name = et_expense.getText().toString();
                }

                if(et_amount.getText().toString().equals("")){
                    et_amount.setError("Enter Expense Amount");
                    Toast.makeText(AddExpenseActivity.this, "Expense Amount Missing", Toast.LENGTH_SHORT).show();
                    return;
                }
                else{
                    amount = Integer.parseInt(et_amount.getText().toString());
                }

                String category = categoryValue;

                Timestamp timestamp = Timestamp.now();
                Date current_date = timestamp.toDate();
                String time = parseDateToddMMyyyy(current_date.toString());
                String cat_id = String.valueOf(spinner.getSelectedItemId());
                Log.d("time", "onClick: "+time);

                CollectionReference expenses = db.collection("Expenses");

                Expense expense = new Expense(expense_name, amount, category, time);
                expense.setCat_id(cat_id);
                Log.d("cat_id", "onClick: "+expense.getCat_id());
                expenses.add(expense)
                        .addOnSuccessListener(new OnSuccessListener<DocumentReference>() {
                            @Override
                            public void onSuccess(DocumentReference documentReference) {

                                Toast.makeText(AddExpenseActivity.this, "Expense Added", Toast.LENGTH_SHORT).show();
                                finish();

                            }
                        }).addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {

                        Toast.makeText(AddExpenseActivity.this, "Expense Not Added", Toast.LENGTH_SHORT).show();
                        finish();
                    }
                });
            }
        });

    }
    public String parseDateToddMMyyyy(String time) {
        String inputPattern = "EEE MMM dd HH:mm:ss zzzz yyyy";
        String outputPattern = "MM-dd-yyyy";
        SimpleDateFormat inputFormat = new SimpleDateFormat(inputPattern);
        SimpleDateFormat outputFormat = new SimpleDateFormat(outputPattern);

        Date date = null;
        String str = null;

        try {
            date = inputFormat.parse(time);
            str = outputFormat.format(date);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return str;
    }

    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {

        categoryValue = parent.getItemAtPosition(position).toString();
    }

    @Override
    public void onNothingSelected(AdapterView<?> parent) {

    }
}
