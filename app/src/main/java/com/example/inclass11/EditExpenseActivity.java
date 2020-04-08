package com.example.inclass11;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.Timestamp;
import com.google.firebase.firestore.FirebaseFirestore;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class EditExpenseActivity extends AppCompatActivity implements AdapterView.OnItemSelectedListener{

    private EditText et_edit_name;
    private Spinner category_spinner;
    private EditText et_edit_amount;
    private Button bt_save;
    private Button bt_cancel;

    Expense expense;
    private FirebaseFirestore db;

    String name;
    String category;
    String amount;
    String date;
    String id;
    String expense_id;

    String categoryValue;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_expense);
        setTitle("Edit Expense");

        et_edit_name = findViewById(R.id.et_edit_name);
        category_spinner = findViewById(R.id.category_spinner);
        et_edit_amount = findViewById(R.id.et_edit_amount);
        bt_save = findViewById(R.id.bt_save);
        bt_cancel = findViewById(R.id.bt_cancel_edit);

        category_spinner.setOnItemSelectedListener(this);

        db = FirebaseFirestore.getInstance();

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
        category_spinner.setAdapter(dataAdapter);

        if(!getIntent().getExtras().equals(null)){

            expense = (Expense) getIntent().getSerializableExtra("edit");

            id = expense.getCat_id();
            Log.d("cat_id", "onCreate: "+id);
            expense_id = expense.getId();
            Log.d("expense_id", "onCreate: "+expense_id);
            name = expense.getExpense();
            category = expense.getCategory();
            amount = String.valueOf(expense.getAmount());

            et_edit_name.setText(name);
            category_spinner.setSelection(Integer.parseInt(id));
            et_edit_amount.setText(amount);
        }
        else{
            Toast.makeText(this, "Content Not Found", Toast.LENGTH_SHORT).show();
        }

        bt_cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(EditExpenseActivity.this, MainActivity.class);
                startActivity(i);
            }
        });


        bt_save.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if(et_edit_name.getText().toString().equals("") || !et_edit_name.getText().toString().matches("[a-zA-Z_]+")){
                    et_edit_name.setError("Enter Valid Expense Name");
                    return;
                }
                else{
                    name = et_edit_name.getText().toString();
                }

                String category = categoryValue;

                if(et_edit_amount.getText().toString().equals("")){
                    et_edit_amount.setError("Enter Expense Amount");
                    return;
                }
                else{
                    amount = et_edit_amount.getText().toString();
                }

                String cat_id = String.valueOf(category_spinner.getSelectedItemId());
                Timestamp timestamp = Timestamp.now();
                Date d = timestamp.toDate();
                date = parseDateToddMMyyyy(d.toString());

                Expense e = new Expense(name, Integer.parseInt(amount),category,date);
                e.setCat_id(cat_id);

                db.collection("Expenses").document(expense_id)
                        .set(e)
                        .addOnCompleteListener(new OnCompleteListener<Void>() {
                            @Override
                            public void onComplete(@NonNull Task<Void> task) {
                                
                                if(task.isSuccessful()){

                                    Toast.makeText(EditExpenseActivity.this, "Expense Updated", Toast.LENGTH_SHORT).show();
                                    Intent i = new Intent(EditExpenseActivity.this,MainActivity.class);
                                    startActivity(i);
                                }
                                else{
                                    Toast.makeText(EditExpenseActivity.this, "Expense Not Updated", Toast.LENGTH_SHORT).show();
                                }
                                
                            }
                        }).addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Toast.makeText(EditExpenseActivity.this, "Expense Not Updated", Toast.LENGTH_SHORT).show();
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

        //categoryValue = parent.getItemAtPosition(Integer.parseInt(id)).toString();
    }
}
