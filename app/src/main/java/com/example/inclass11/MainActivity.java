package com.example.inclass11;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.ItemTouchHelper;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.firebase.ui.firestore.FirestoreRecyclerAdapter;
import com.firebase.ui.firestore.FirestoreRecyclerOptions;
import com.firebase.ui.firestore.SnapshotParser;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.FirestoreRegistrar;
import com.google.firebase.firestore.ListenerRegistration;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.List;

import javax.annotation.Nullable;

public class MainActivity extends AppCompatActivity implements ExpenseAdapter.InteractActivity{

    private FloatingActionButton fab;
    public static TextView tv_add_note;
    public static TextView tv_no_expense_note;
    private RecyclerView rv_expense;
    private ExpenseAdapter adapter;
    private FirebaseFirestore db  = FirebaseFirestore.getInstance();

    public ArrayList<Expense> expenseArrayList;


    @Override
    protected void onResume() {
        super.onResume();

        if(adapter.getItemCount() == 0){
            tv_no_expense_note.setVisibility(View.VISIBLE);
            tv_add_note.setVisibility((View.VISIBLE));
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        setTitle("Expense App");


        tv_add_note = findViewById(R.id.tv_add_expense_note);
        tv_no_expense_note = findViewById(R.id.tv_no_expense);

        fab = findViewById(R.id.floating_action_button);
        fab.setImageDrawable(getDrawable(R.drawable.add));

        expenseArrayList = new ArrayList<>();

        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(MainActivity.this, AddExpenseActivity.class);
                startActivity(i);
            }
        });

        rv_expense = findViewById(R.id.expense_recycler);


        setUpRecyclerView();

        if(adapter.getItemCount() == 0){
            tv_no_expense_note.setVisibility(View.VISIBLE);
            tv_add_note.setVisibility(View.VISIBLE);
        }
        else{
            tv_no_expense_note.setVisibility(View.INVISIBLE);
            tv_add_note.setVisibility(View.INVISIBLE);
        }

        }


    private void setUpRecyclerView(){

        Query query = db.collection("Expenses");


        FirestoreRecyclerOptions<Expense> options = new FirestoreRecyclerOptions.Builder<Expense>()
                .setQuery(query, new SnapshotParser<Expense>() {
                    @NonNull
                    @Override
                    public Expense parseSnapshot(@NonNull DocumentSnapshot snapshot) {

                        Expense expense = snapshot.toObject(Expense.class);
                        expense.setId(snapshot.getId());
                        expenseArrayList.add(expense);
                        Log.d("expensearray", "parseSnapshot: "+expenseArrayList);
                        return expense;
                    }
                })
                .setLifecycleOwner(this)
                .build();



        adapter = new ExpenseAdapter(options, this);
        rv_expense.setHasFixedSize(true);
        rv_expense.setLayoutManager(new LinearLayoutManager(this));
        rv_expense.setAdapter(adapter);

    }

    @Override
    protected void onStart() {
        super.onStart();
        adapter.startListening();


    }

    @Override
    protected void onStop() {
        super.onStop();
        adapter.stopListening();
    }


    @Override
    public void deleteItem(int position, Expense expense) {

        Log.d("expense", "deleteItem: "+expense);
        DocumentReference expRef = db.collection("Expenses").document(expense.getId());

        expRef.delete()
                .addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {

                        if(task.isSuccessful()){

                            Toast.makeText(MainActivity.this, "Expense Deleted", Toast.LENGTH_SHORT).show();
                            if(adapter.getItemCount() > 0){
                                tv_no_expense_note.setVisibility(View.INVISIBLE);
                                tv_add_note.setVisibility(View.INVISIBLE);
                            }
                            else{

                                tv_no_expense_note.setVisibility(View.VISIBLE);
                                tv_add_note.setVisibility(View.VISIBLE);
                            }
                            adapter.notifyDataSetChanged();
                        }
                        else{
                            Toast.makeText(MainActivity.this, "Expense Not Deleted", Toast.LENGTH_SHORT).show();
                        }

                    }
                });
    }

}
