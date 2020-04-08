package com.example.inclass11;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import com.firebase.ui.firestore.FirestoreRecyclerAdapter;
import com.firebase.ui.firestore.FirestoreRecyclerOptions;

public class ExpenseAdapter extends FirestoreRecyclerAdapter<Expense, ExpenseAdapter.ExpenseViewHolder> {

    InteractActivity interactActivity;

    public ExpenseViewHolder holder;
    private MainActivity mContext;

    public ExpenseAdapter(@NonNull FirestoreRecyclerOptions<Expense> options, MainActivity mContext) {
        super(options);
        this.mContext = mContext;
    }


    @Override
    protected void onBindViewHolder(@NonNull ExpenseViewHolder holder, final int i, @NonNull final Expense expense) {


        holder.tv_name.setText(expense.getExpense());
        holder.tv_price.setText("$" + expense.getAmount());

        holder.cardView.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {

                mContext.deleteItem(i, expense);
                return false;
            }
        });


        holder.cardView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(mContext, ShowExpenseActivity.class);
                i.putExtra("expense", expense);
                mContext.startActivity(i);
            }
        });
    }

    @Override
    public void onDataChanged() {
        super.onDataChanged();
        if(super.getItemCount() == 0){
            MainActivity.tv_add_note.setVisibility(View.VISIBLE);
            MainActivity.tv_no_expense_note.setVisibility(View.VISIBLE);
        }
        else {
            MainActivity.tv_add_note.setVisibility(View.INVISIBLE);
            MainActivity.tv_no_expense_note.setVisibility(View.INVISIBLE);
        }
    }

    @NonNull
    @Override
    public ExpenseViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.expense_list, parent, false);
        holder =  new ExpenseViewHolder(view);
        return holder;
    }

    @Override
    public int getItemCount() {
        return super.getItemCount();
    }

    public interface InteractActivity{

        void deleteItem(int position, Expense expense);

    }

    public class ExpenseViewHolder extends RecyclerView.ViewHolder {

        TextView tv_name, tv_price;
        CardView cardView;

        public ExpenseViewHolder(@NonNull View itemView) {
            super(itemView);

            tv_name = itemView.findViewById(R.id.tv_expense_name);
            tv_price = itemView.findViewById(R.id.tv_amount);
            cardView = itemView.findViewById(R.id.cardView);

        }

    }

}
