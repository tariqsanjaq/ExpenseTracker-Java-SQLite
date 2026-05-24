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

public class MainActivity extends AppCompatActivity {

    private RadioGroup authToggleGroup;
    private EditText etEmail, etPassword;
    private TextView tvAuthError;
    private SharedPreferences sharedPreferences;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        sharedPreferences = getSharedPreferences("ExpenseTrackerPrefs", MODE_PRIVATE);

        // Auto-login if session is saved
        if (sharedPreferences.getBoolean("isLoggedIn", false)) {
            navigateToDashboard();
            return;
        }

        // Link UI Components matching the XML exactly
        authToggleGroup = findViewById(R.id.authToggleGroup);
        etEmail = findViewById(R.id.etEmail);
        etPassword = findViewById(R.id.etPassword);
        tvAuthError = findViewById(R.id.tvAuthError);
    }

    // Triggered by the Continue button (android:onClick="handleAuthenticationClick")
    public void handleAuthenticationClick(View view) {
        // Always hide the error text when the user tries again
        tvAuthError.setVisibility(View.GONE);

        String email = etEmail.getText().toString().trim();
        String password = etPassword.getText().toString().trim();

        if (TextUtils.isEmpty(email) || TextUtils.isEmpty(password)) {
            showError("Please enter email and password");
            return;
        }

        int selectedId = authToggleGroup.getCheckedRadioButtonId();

        if (selectedId == R.id.radioLogin) {
            // Mock Login Logic
            if (email.equals("user@student.com") && password.equals("123456")) {
                saveLoginSession();
                navigateToDashboard();
            } else {
                showError("Invalid credentials. Use user@student.com / 123456");
            }
        } else if (selectedId == R.id.radioRegister) {
            // Mock Registration Logic
            Toast.makeText(this, "Account created! Please log in.", Toast.LENGTH_SHORT).show();
            authToggleGroup.check(R.id.radioLogin);
            etPassword.setText(""); // Clear password field for safety
        }
    }

    // Helper method to display the red error text dynamically
    private void showError(String message) {
        tvAuthError.setText(message);
        tvAuthError.setVisibility(View.VISIBLE);
    }

    private void saveLoginSession() {
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putBoolean("isLoggedIn", true);
        editor.apply();
    }

    private void navigateToDashboard() {
        Intent intent = new Intent(this, Activity_dashboard.class);
        startActivity(intent);
        finish();
    }
}