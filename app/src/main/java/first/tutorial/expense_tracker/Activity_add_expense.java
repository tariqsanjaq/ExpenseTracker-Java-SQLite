package first.tutorial.expense_tracker;



import android.os.Bundle;
import android.view.View;
import android.widget.AutoCompleteTextView;
import android.widget.CheckBox;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;

public class Activity_add_expense extends AppCompatActivity {
    EditText amountInput, descInput;
    Spinner catSpinner;
    CheckBox recurringCheck;
    AutoCompleteTextView tagsInput;
    DatePicker transactionDate;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_expense);

        amountInput = findViewById(R.id.etAmount);
        descInput = findViewById(R.id.etDescription);
        catSpinner = findViewById(R.id.spinnerCategory);
        recurringCheck = findViewById(R.id.cbIsRecurring);
        tagsInput = findViewById(R.id.actvTags);
        transactionDate = findViewById(R.id.datePickerTransaction);
    }

    public void saveTransactionRecord(View v) {
        Toast.makeText(this, "Saved successfully!", Toast.LENGTH_SHORT).show();
        finish();
    }
}