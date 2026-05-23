package first.tutorial.expense_tracker;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Spinner;
import android.widget.TextView;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.RecyclerView;

public class Activity_dashboard extends AppCompatActivity {
    TextView balanceText;
    Spinner filterSpinner;
    RecyclerView recentList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dashboard);

        balanceText = findViewById(R.id.tvDashboardBalance);
        filterSpinner = findViewById(R.id.spinnerDashboardFilter);
        recentList = findViewById(R.id.rvRecentTransactions);
    }

    public void goToAddExpense(View v) {
        Intent intent = new Intent(Activity_dashboard.this, Activity_add_expense.class);
        startActivity(intent);
    }

    public void goToHistory(View v) {
        Intent intent = new Intent(Activity_dashboard.this, Activity_history.class);
        startActivity(intent);
    }
}