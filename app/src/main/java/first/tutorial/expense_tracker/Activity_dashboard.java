package first.tutorial.expense_tracker;

import android.app.AlertDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;
import java.util.ArrayList;
import java.util.List;
import first.tutorial.expense_tracker.database.Expense;
import first.tutorial.expense_tracker.database.ExpenseDAO;

public class Activity_dashboard extends AppCompatActivity {

    private TextView tvDashboardBalance;
    private ListView lvRecentTransactions;
    private ExpenseDAO expenseDAO;
    private List<Expense> currentExpenseList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dashboard);

        expenseDAO = new ExpenseDAO(this);
        expenseDAO.open();

        tvDashboardBalance = findViewById(R.id.tvDashboardBalance);
        lvRecentTransactions = findViewById(R.id.rvRecentTransactions);

        // 🌟 PERSONALIZE GREETING
        TextView tvWelcomeMessage = findViewById(R.id.tvWelcomeMessage);
        SharedPreferences prefs = getSharedPreferences("ExpenseTrackerPrefs", MODE_PRIVATE);
        String userName = prefs.getString("userName", "User");
        if (userName.contains(" ")) {
            userName = userName.substring(0, userName.indexOf(" "));
        }
        if (tvWelcomeMessage != null) {
            tvWelcomeMessage.setText("Hello, " + userName + " 👋");
        }

        // SIGN OUT
        Button btnSignOut = findViewById(R.id.btnSignOut);
        if (btnSignOut != null) {
            btnSignOut.setOnClickListener(v -> {
                SharedPreferences.Editor editor = prefs.edit();
                editor.clear(); // Clears all user data
                editor.apply();

                Toast.makeText(Activity_dashboard.this, "Signed Out Successfully", Toast.LENGTH_SHORT).show();
                Intent intent = new Intent(Activity_dashboard.this, MainActivity.class);
                startActivity(intent);
                finish();
            });
        }

        // 🌟 LONG CLICK: DELETE
        lvRecentTransactions.setOnItemLongClickListener((parent, view, position, id) -> {
            Expense selectedExpense = currentExpenseList.get(position);
            showDeleteConfirmation(selectedExpense);
            return true;
        });

        // 🌟 SHORT CLICK: EDIT
        lvRecentTransactions.setOnItemClickListener((parent, view, position, id) -> {
            Expense selectedExpense = currentExpenseList.get(position);
            Intent intent = new Intent(Activity_dashboard.this, Activity_add_expense.class);
            intent.putExtra("EXTRA_ID", selectedExpense.getId());
            intent.putExtra("EXTRA_TITLE", selectedExpense.getTitle());
            intent.putExtra("EXTRA_AMOUNT", selectedExpense.getAmount());
            intent.putExtra("EXTRA_CATEGORY", selectedExpense.getCategory());
            intent.putExtra("EXTRA_DATE", selectedExpense.getDate());
            intent.putExtra("EXTRA_IS_RECURRING", selectedExpense.getIsRecurring());
            startActivity(intent);
        });
    }

    private void showDeleteConfirmation(Expense expense) {
        new AlertDialog.Builder(this)
                .setTitle("Delete Expense")
                .setMessage("Are you sure you want to delete '" + expense.getTitle() + "'?")
                .setPositiveButton("Yes, Delete", (dialog, which) -> {
                    expenseDAO.deleteExpense(expense.getId());
                    Toast.makeText(this, "Expense Deleted", Toast.LENGTH_SHORT).show();
                    loadDashboardData();
                })
                .setNegativeButton("Cancel", null)
                .show();
    }

    @Override
    protected void onResume() {
        super.onResume();
        loadDashboardData();
    }

    private void loadDashboardData() {
        // 🌟 ONLY LOAD LOGGED IN USER DATA
        SharedPreferences prefs = getSharedPreferences("ExpenseTrackerPrefs", MODE_PRIVATE);
        int currentUserId = prefs.getInt("userId", -1);

        currentExpenseList = expenseDAO.getUserExpenses(currentUserId);

        double totalBalance = 0.0;
        List<String> displayList = new ArrayList<>();

        for (Expense exp : currentExpenseList) {
            totalBalance += exp.getAmount();
            displayList.add(exp.getDate() + " | " + exp.getTitle() + " | $" + exp.getAmount());
        }

        if (tvDashboardBalance != null) {
            tvDashboardBalance.setText(String.format("$%.2f", totalBalance));
        }

        if (lvRecentTransactions != null) {
            ArrayAdapter<String> adapter = new ArrayAdapter<>(this, android.R.layout.simple_list_item_1, displayList);
            lvRecentTransactions.setAdapter(adapter);
        }
    }

    public void goToAddExpense(View view) {
        Intent intent = new Intent(this, Activity_add_expense.class);
        startActivity(intent);
    }

    public void goToHistory(View view) {
        Intent intent = new Intent(this, Activity_history.class);
        startActivity(intent);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (expenseDAO != null) expenseDAO.close();
    }
}