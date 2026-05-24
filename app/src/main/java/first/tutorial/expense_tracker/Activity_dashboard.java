package first.tutorial.expense_tracker;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;
import androidx.appcompat.app.AppCompatActivity;

import java.util.ArrayList;
import java.util.List;

import first.tutorial.expense_tracker.database.Expense;
import first.tutorial.expense_tracker.database.ExpenseDAO;

public class Activity_dashboard extends AppCompatActivity {

    private TextView tvDashboardBalance;
    private ListView lvRecentTransactions;
    private ExpenseDAO expenseDAO;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dashboard);

        // 1. Open Database Connection
        expenseDAO = new ExpenseDAO(this);
        expenseDAO.open();

        // 2. Link UI Components exactly as named in the XML
        tvDashboardBalance = findViewById(R.id.tvDashboardBalance);
        lvRecentTransactions = findViewById(R.id.rvRecentTransactions);
    }

    // 3. Load data every time the screen appears
    @Override
    protected void onResume() {
        super.onResume();
        loadDashboardData();
    }

    private void loadDashboardData() {
        List<Expense> expenseList = expenseDAO.getAllExpenses();
        double totalBalance = 0.0;
        List<String> displayList = new ArrayList<>();

        for (Expense exp : expenseList) {
            totalBalance += exp.getAmount();
            displayList.add(exp.getTitle() + " | $" + exp.getAmount() + "\n" + exp.getCategory() + " - " + exp.getDate());
        }

        // Update the big blue text view to show the total
        tvDashboardBalance.setText(String.format("$%.2f", totalBalance));

        // Plug the strings into the ListView using a basic adapter
        ArrayAdapter<String> adapter = new ArrayAdapter<>(
                this,
                android.R.layout.simple_list_item_1,
                displayList
        );
        lvRecentTransactions.setAdapter(adapter);
    }

    // 4. Navigation Method: Linked directly to android:onClick="goToAddExpense" in XML
    public void goToAddExpense(View view) {
        Intent intent = new Intent(this, Activity_add_expense.class);
        startActivity(intent);
    }

    // 5. Navigation Method: Linked directly to android:onClick="goToHistory" in XML
    public void goToHistory(View view) {
        Intent intent = new Intent(this, Activity_history.class);
        startActivity(intent);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        expenseDAO.close(); // Prevent memory leaks
    }
}