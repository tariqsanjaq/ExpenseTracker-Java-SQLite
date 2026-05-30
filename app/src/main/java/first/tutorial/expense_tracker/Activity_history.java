package first.tutorial.expense_tracker;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import androidx.appcompat.app.AppCompatActivity;

import java.util.ArrayList;
import java.util.List;

import first.tutorial.expense_tracker.database.Expense;
import first.tutorial.expense_tracker.database.ExpenseDAO;

public class Activity_history extends AppCompatActivity {

    private ListView lvCompleteLedger;
    private ExpenseDAO expenseDAO;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_history);

        expenseDAO = new ExpenseDAO(this);
        expenseDAO.open();

        // Make sure this ID matches your activity_history.xml file!
        lvCompleteLedger = findViewById(R.id.lvCompleteLedger);

        loadHistoryData();
    }

    private void loadHistoryData() {
        // 🌟 1. GET THE LOGGED-IN USER'S ID FROM MEMORY
        SharedPreferences prefs = getSharedPreferences("ExpenseTrackerPrefs", MODE_PRIVATE);
        int currentUserId = prefs.getInt("userId", -1);

        // 🌟 2. USE THE NEW METHOD TO FETCH ONLY THEIR EXPENSES
        List<Expense> allExpenses = expenseDAO.getUserExpenses(currentUserId);

        List<String> displayList = new ArrayList<>();

        for (Expense exp : allExpenses) {
            String record = exp.getDate() + " | " + exp.getTitle() + "\n"
                    + "Category: " + exp.getCategory() + " | Amount: $" + exp.getAmount();
            displayList.add(record);
        }

        if (lvCompleteLedger != null) {
            ArrayAdapter<String> adapter = new ArrayAdapter<>(
                    this,
                    android.R.layout.simple_list_item_1,
                    displayList
            );
            lvCompleteLedger.setAdapter(adapter);
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (expenseDAO != null) {
            expenseDAO.close();
        }
    }
}