package com.example.inclass11;

import com.google.firebase.Timestamp;
import com.google.firebase.firestore.Exclude;

import java.io.Serializable;
import java.util.Date;

public class Expense implements Serializable {

    @Exclude
    private String id;
    @Exclude
    private String cat_id;
    String expense;
    int amount;
    String category;
    String date;

    public Expense() {
    }

    public Expense(String expense, int amount, String category, String date) {
        this.expense = expense;
        this.amount = amount;
        this.category = category;
        this.date = date;
    }

    public String getCat_id() {
        return cat_id;
    }

    public void setCat_id(String cat_id) {
        this.cat_id = cat_id;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getExpense() {
        return expense;
    }

    public void setExpense(String expense) {
        this.expense = expense;
    }

    public int getAmount() {
        return amount;
    }

    public void setAmount(int amount) {
        this.amount = amount;
    }

    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }
}
