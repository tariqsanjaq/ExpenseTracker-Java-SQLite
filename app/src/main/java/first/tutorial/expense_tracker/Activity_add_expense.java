package first.tutorial.expense_tracker;

import android.app.DatePickerDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;
import java.util.Calendar;
import first.tutorial.expense_tracker.database.Expense;
import first.tutorial.expense_tracker.database.ExpenseDAO;

public class Activity_add_expense extends AppCompatActivity {

    private EditText etAmount, etDescription;
    private Spinner spinnerCategory;
    private CheckBox cbIsRecurring;
    private Button btnSelectDate, btnSaveTransaction;
    private ExpenseDAO expenseDAO;

    private String selectedTransactionDate = "";
    private ArrayAdapter<String> adapter;

    private boolean isEditMode = false;
    private int editExpenseId = -1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_expense);

        expenseDAO = new ExpenseDAO(this);
        expenseDAO.open();

        etAmount = findViewById(R.id.etAmount);
        etDescription = findViewById(R.id.etDescription);
        spinnerCategory = findViewById(R.id.spinnerCategory);
        cbIsRecurring = findViewById(R.id.cbIsRecurring);
        btnSelectDate = findViewById(R.id.btnSelectDate);
        btnSaveTransaction = findViewById(R.id.btnSaveTransaction);

        String[] categories = {"Food", "Transport", "Utilities", "Entertainment", "Housing", "Other"};
        adapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_dropdown_item, categories);
        spinnerCategory.setAdapter(adapter);

        setDefaultDateToToday();
        checkIfModifying();
    }

    private void checkIfModifying() {
        Intent intent = getIntent();
        if (intent.hasExtra("EXTRA_ID")) {
            isEditMode = true;
            editExpenseId = intent.getIntExtra("EXTRA_ID", -1);

            etDescription.setText(intent.getStringExtra("EXTRA_TITLE"));
            etAmount.setText(String.valueOf(intent.getDoubleExtra("EXTRA_AMOUNT", 0.0)));
            selectedTransactionDate = intent.getStringExtra("EXTRA_DATE");
            btnSelectDate.setText("Selected Date: " + selectedTransactionDate);
            cbIsRecurring.setChecked(intent.getIntExtra("EXTRA_IS_RECURRING", 0) == 1);

            String category = intent.getStringExtra("EXTRA_CATEGORY");
            if (category != null) {
                int spinnerPosition = adapter.getPosition(category);
                spinnerCategory.setSelection(spinnerPosition);
            }

            btnSaveTransaction.setText("Update Expense");
        }
    }

    private void setDefaultDateToToday() {
        Calendar cal = Calendar.getInstance();
        int year = cal.get(Calendar.YEAR);
        int month = cal.get(Calendar.MONTH);
        int day = cal.get(Calendar.DAY_OF_MONTH);
        selectedTransactionDate = year + "-" + String.format("%02d", month + 1) + "-" + String.format("%02d", day);
        btnSelectDate.setText("Selected Date: " + selectedTransactionDate);
    }

    public void showDatePickerDialog(View view) {
        Calendar cal = Calendar.getInstance();
        DatePickerDialog dialog = new DatePickerDialog(this, (view1, year, month, dayOfMonth) -> {
            selectedTransactionDate = year + "-" + String.format("%02d", month + 1) + "-" + String.format("%02d", dayOfMonth);
            btnSelectDate.setText("Selected Date: " + selectedTransactionDate);
        }, cal.get(Calendar.YEAR), cal.get(Calendar.MONTH), cal.get(Calendar.DAY_OF_MONTH));
        dialog.show();
    }

    public void saveTransactionRecord(View view) {
        String amountStr = etAmount.getText().toString().trim();
        String description = etDescription.getText().toString().trim();
        String category = "Other";
        if (spinnerCategory != null && spinnerCategory.getSelectedItem() != null) {
            category = spinnerCategory.getSelectedItem().toString();
        }
        int isRecurring = (cbIsRecurring != null && cbIsRecurring.isChecked()) ? 1 : 0;

        if (TextUtils.isEmpty(amountStr) || TextUtils.isEmpty(description)) {
            Toast.makeText(this, "Please enter an amount and description", Toast.LENGTH_SHORT).show();
            return;
        }
        double amount = Double.parseDouble(amountStr);

        // 🌟 GET USER ID FROM MEMORY
        SharedPreferences prefs = getSharedPreferences("ExpenseTrackerPrefs", MODE_PRIVATE);
        int currentUserId = prefs.getInt("userId", -1);

        if (isEditMode) {
            Expense updatedExpense = new Expense(editExpenseId, description, amount, category, selectedTransactionDate, isRecurring);
            expenseDAO.updateExpense(updatedExpense);
            Toast.makeText(this, "Expense Updated Successfully!", Toast.LENGTH_SHORT).show();
        } else {
            Expense newExpense = new Expense(description, amount, category, selectedTransactionDate, isRecurring);
            // 🌟 PASS USER ID WHEN SAVING
            expenseDAO.insertExpense(newExpense, currentUserId);
            Toast.makeText(this, "Expense Saved Successfully!", Toast.LENGTH_SHORT).show();
        }
        finish();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        expenseDAO.close();
    }
}