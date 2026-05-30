package first.tutorial.expense_tracker;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.EditText;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;
import first.tutorial.expense_tracker.database.DatabaseHelper;

public class MainActivity extends AppCompatActivity {

    private RadioGroup authToggleGroup;
    private EditText etName, etEmail, etPassword;
    private TextView tvAuthError;
    private SharedPreferences sharedPreferences;
    private DatabaseHelper dbHelper;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        dbHelper = new DatabaseHelper(this);
        sharedPreferences = getSharedPreferences("ExpenseTrackerPrefs", MODE_PRIVATE);

        if (sharedPreferences.getBoolean("isLoggedIn", false)) {
            navigateToDashboard();
            return;
        }

        authToggleGroup = findViewById(R.id.authToggleGroup);
        etName = findViewById(R.id.etName);
        etEmail = findViewById(R.id.etEmail);
        etPassword = findViewById(R.id.etPassword);
        tvAuthError = findViewById(R.id.tvAuthError);

        // Hide/Show name box
        authToggleGroup.setOnCheckedChangeListener((group, checkedId) -> {
            if (checkedId == R.id.radioRegister) {
                etName.setVisibility(View.VISIBLE);
            } else {
                etName.setVisibility(View.GONE);
            }
        });
    }

    public void handleAuthenticationClick(View view) {
        tvAuthError.setVisibility(View.GONE);
        String name = etName.getText().toString().trim();
        String email = etEmail.getText().toString().trim();
        String password = etPassword.getText().toString().trim();

        if (TextUtils.isEmpty(email) || TextUtils.isEmpty(password)) {
            showError("Please enter email and password");
            return;
        }

        int selectedId = authToggleGroup.getCheckedRadioButtonId();

        if (selectedId == R.id.radioLogin) {
            int loggedInUserId = dbHelper.authenticateUser(email, password);
            if (loggedInUserId != -1) {
                String userName = dbHelper.getUserName(loggedInUserId);
                saveLoginSession(loggedInUserId, userName);
                navigateToDashboard();
            } else {
                showError("Invalid email or password.");
            }
        } else if (selectedId == R.id.radioRegister) {
            if (TextUtils.isEmpty(name)) {
                showError("Please enter your name");
                return;
            }
            if (dbHelper.insertUser(name, email, password)) {
                Toast.makeText(this, "Account created! You can now sign in.", Toast.LENGTH_SHORT).show();
                authToggleGroup.check(R.id.radioLogin);
                etPassword.setText("");
                etName.setText("");
            } else {
                showError("This email is already registered!");
            }
        }
    }

    private void showError(String message) {
        tvAuthError.setText(message);
        tvAuthError.setVisibility(View.VISIBLE);
    }

    private void saveLoginSession(int userId, String userName) {
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putBoolean("isLoggedIn", true);
        editor.putInt("userId", userId);
        editor.putString("userName", userName);
        editor.apply();
    }

    private void navigateToDashboard() {
        Intent intent = new Intent(this, Activity_dashboard.class);
        startActivity(intent);
        finish();
    }
}