package first.tutorial.expense_tracker;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.RecyclerView;

public class Activity_history extends AppCompatActivity {
    EditText searchInput;
    Button rangeButton;
    RecyclerView ledgerList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_history);

        searchInput = findViewById(R.id.etSearchHistory);
        rangeButton = findViewById(R.id.btnFilterDateRange);
        ledgerList = findViewById(R.id.rvCompleteLedger);
    }

    public void selectFilterDateRange(View v) {
        Toast.makeText(this, "Filter popup goes here", Toast.LENGTH_SHORT).show();
    }
}