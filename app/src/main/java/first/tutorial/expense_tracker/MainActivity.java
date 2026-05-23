package first.tutorial.expense_tracker;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.RadioGroup;
import android.widget.TextView;
import androidx.appcompat.app.AppCompatActivity;

public class MainActivity extends AppCompatActivity {
    EditText emailField, passwordField;
    TextView errorDisplay;
    RadioGroup optionGroup;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        emailField = findViewById(R.id.etEmail);
        passwordField = findViewById(R.id.etPassword);
        errorDisplay = findViewById(R.id.tvAuthError);
        optionGroup = findViewById(R.id.authToggleGroup);
    }

    public void handleAuthentication(View v) {
        String userEmail = emailField.getText().toString();

        if (userEmail.isEmpty()) {
            errorDisplay.setVisibility(View.VISIBLE);
        } else {
            errorDisplay.setVisibility(View.GONE);
            Intent intent = new Intent(MainActivity.this, Activity_dashboard.class);
            startActivity(intent);
        }
    }
}