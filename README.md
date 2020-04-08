# Expense Tracker Using Firebase Firestore

## Project Objective

The objective of this project is to create an android application to keep track of the expenses list by using Google's Firebase Firestore

## Prerequisites 

* Android SDK v24
* Latest Android Build Tools
* Android Support Repository

## App Layout

1. The First Screen displays the expense list using a Recycler View. In Case there are no expenses, it displays a message to add a new list

<img src = "https://github.com/sriganeshlokesh/expense_tracker/blob/master/screen1.png" width ="300" height = "600"/>

2. Second Screen is used to add the expense list onto the Firebase Firestore

<img src = "https://github.com/sriganeshlokesh/expense_tracker/blob/master/screen2.png" width ="300" height = "600"/>

3. Third screen shows that the expense has been added to the firestore and displayed in the RecyclerView.

<img src = "https://github.com/sriganeshlokesh/expense_tracker/blob/master/screen3.png" width ="300" height = "600"/>

4. Fourth Screen displays the expense when tapped on the RecyclerView

<img src = "https://github.com/sriganeshlokesh/expense_tracker/blob/master/screen4.png" width ="300" height = "600"/>

5. Clicking on the edit expense button will display the edit expense screen where you can update the expense

<img src = "https://github.com/sriganeshlokesh/expense_tracker/blob/master/screen5.png" width ="300" height = "600"/>

6. Finally , once the expense is updated,it is reflected in the recyclerview and the firestore with a Toast message to validate.

<img src = "https://github.com/sriganeshlokesh/expense_tracker/blob/master/screen6.png" width ="300" height = "600"/>

7. Inorder to the delete a particular expense, we long click on the recycler view and a toast is displayed to validate the operation

<img src = "https://github.com/sriganeshlokesh/expense_tracker/blob/master/screen7.png" width ="300" height = "600"/>
