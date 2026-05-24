package first.tutorial.expense_tracker;

import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.CheckBox;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;

import first.tutorial.expense_tracker.database.Expense;
import first.tutorial.expense_tracker.database.ExpenseDAO;

public class Activity_add_expense extends AppCompatActivity {

    private EditText etAmount, etDescription;
    private Spinner spinnerCategory;
    private CheckBox cbIsRecurring;
    private DatePicker datePickerTransaction;
    private ExpenseDAO expenseDAO;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_expense);

        // 1. Open database connection
        expenseDAO = new ExpenseDAO(this);
        expenseDAO.open();

        // 2. Link UI Components EXACTLY to Student 1's IDs
        etAmount = findViewById(R.id.etAmount);
        etDescription = findViewById(R.id.etDescription);
        spinnerCategory = findViewById(R.id.spinnerCategory);
        cbIsRecurring = findViewById(R.id.cbIsRecurring);
        datePickerTransaction = findViewById(R.id.datePickerTransaction);

        // Note: We don't need to link the Button here because of android:onClick in XML!
    }

    // 3. This method is triggered directly by the XML button
    public void saveTransactionRecord(View view) {
        String amountStr = etAmount.getText().toString().trim();
        String description = etDescription.getText().toString().trim();

        // Safe check for spinner
        String category = "Other";
        if (spinnerCategory != null && spinnerCategory.getSelectedItem() != null) {
            category = spinnerCategory.getSelectedItem().toString();
        }

        // Convert checkbox to SQLite integer
        int isRecurring = (cbIsRecurring != null && cbIsRecurring.isChecked()) ? 1 : 0;

        // 4. Extract Date from the Spinner DatePicker
        String date = "";
        if (datePickerTransaction != null) {
            int day = datePickerTransaction.getDayOfMonth();
            int month = datePickerTransaction.getMonth() + 1; // Add 1 because January is 0 in Java
            int year = datePickerTransaction.getYear();
            date = year + "-" + String.format("%02d", month) + "-" + String.format("%02d", day);
        }

        // 5. Input Validation
        if (TextUtils.isEmpty(amountStr) || TextUtils.isEmpty(description)) {
            Toast.makeText(this, "Please enter an amount and description", Toast.LENGTH_SHORT).show();
            return;
        }

        double amount = Double.parseDouble(amountStr);

        // 6. Save to Database
        Expense newExpense = new Expense(description, amount, category, date, isRecurring);
        long result = expenseDAO.insertExpense(newExpense);

        if (result != -1) {
            Toast.makeText(this, "Expense Saved Successfully!", Toast.LENGTH_SHORT).show();
            finish(); // Close screen, return to Dashboard automatically
        } else {
            Toast.makeText(this, "Database Error", Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        expenseDAO.close();
    }
}