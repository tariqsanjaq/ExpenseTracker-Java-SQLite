package first.tutorial.expense_tracker;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;

import java.util.ArrayList;
import java.util.List;

import first.tutorial.expense_tracker.database.Expense;
import first.tutorial.expense_tracker.database.ExpenseDAO;

public class Activity_history extends AppCompatActivity {

    private ListView lvCompleteLedger;
    private ExpenseDAO expenseDAO;
    private List<Expense> allExpenses;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_history);

        // 1. Open Database
        expenseDAO = new ExpenseDAO(this);
        expenseDAO.open();

        // 2. Link the ListView
        lvCompleteLedger = findViewById(R.id.lvCompleteLedger);

        // 3. Load data
        loadHistoryData();
    }

    private void loadHistoryData() {
        allExpenses = expenseDAO.getAllExpenses();
        List<String> displayList = new ArrayList<>();

        for (Expense exp : allExpenses) {
            String record = exp.getDate() + " | " + exp.getTitle() + "\n"
                    + "Category: " + exp.getCategory() + " | Amount: $" + exp.getAmount();
            displayList.add(record);
        }

        ArrayAdapter<String> adapter = new ArrayAdapter<>(
                this,
                android.R.layout.simple_list_item_1,
                displayList
        );
        lvCompleteLedger.setAdapter(adapter);
    }

    // -------------------------------------------------------------------
    // PROJECT REQUIREMENT: IMPLICIT INTENT 1 (SHARE DATA)
    // -------------------------------------------------------------------
    public void shareFinancialReport(View view) {
        if (allExpenses == null || allExpenses.isEmpty()) {
            Toast.makeText(this, "No records to share", Toast.LENGTH_SHORT).show();
            return;
        }

        StringBuilder report = new StringBuilder();
        report.append("--- My Financial Report ---\n\n");
        double total = 0;

        for (Expense exp : allExpenses) {
            report.append(exp.getDate()).append(": ").append(exp.getTitle())
                    .append(" ($").append(exp.getAmount()).append(")\n");
            total += exp.getAmount();
        }
        report.append("\nTotal Spent: $").append(String.format("%.2f", total));

        Intent sendIntent = new Intent();
        sendIntent.setAction(Intent.ACTION_SEND);
        sendIntent.putExtra(Intent.EXTRA_TEXT, report.toString());
        sendIntent.setType("text/plain");

        Intent shareIntent = Intent.createChooser(sendIntent, "Share Report via...");
        startActivity(shareIntent);
    }

    // -------------------------------------------------------------------
    // PROJECT REQUIREMENT: IMPLICIT INTENT 2 (CONTACT SUPPORT)
    // -------------------------------------------------------------------
    public void contactSupport(View view) {
        Intent emailIntent = new Intent(Intent.ACTION_SENDTO);
        emailIntent.setData(android.net.Uri.parse("mailto:support@expensetracker.com"));
        emailIntent.putExtra(Intent.EXTRA_SUBJECT, "Need Help with Expense Tracker");
        startActivity(emailIntent);
    }

    // Placeholder to prevent app crash when clicking the Date Filter button
    public void selectFilterDateRange(View view) {
        Toast.makeText(this, "Date filter feature coming soon!", Toast.LENGTH_SHORT).show();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        expenseDAO.close();
    }
}