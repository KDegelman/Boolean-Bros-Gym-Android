package com.example.booleangoes;

import android.os.Bundle;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;
import android.graphics.Color;
import android.widget.Spinner;
import android.widget.ArrayAdapter;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

public class CreateActivity extends AppCompatActivity {

    private EditText editFullName;
    private EditText editDateOfBirth;
    private Spinner spinnerGender;
    private EditText editPhoneNumber;
    private EditText editEmail;
    private EditText editCity;
    private Button btnAddForm;
    private Button btnClearAddForm;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create);

        connectViews();
        setupGenderSpinner();
        setupButtons();

        //I had to add this to make it work, I got it from the internet so I don't fully understand why it works or why I need it
        //It fixes the app from being behind the top info bar
        View main = findViewById(R.id.main);

        ViewCompat.setOnApplyWindowInsetsListener(main, (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(0, systemBars.top, 0, 0);
            return insets;
        });

        FrameLayout btnBack = findViewById(R.id.btnBack);

        btnBack.setOnClickListener(v -> {
            finish();
        });
    }

    private void setupGenderSpinner() {
        String[] genderOptions = {"N/A", "Male", "Female"};

        ArrayAdapter<String> adapter = new ArrayAdapter<>(
                this,
                android.R.layout.simple_spinner_item,
                genderOptions
        );

        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

        spinnerGender.setAdapter(adapter);
    }
    private void connectViews() {
        editFullName = findViewById(R.id.editFullName);
        editDateOfBirth = findViewById(R.id.editDateOfBirth);
        spinnerGender = findViewById(R.id.spinnerGender);
        editPhoneNumber = findViewById(R.id.editPhoneNumber);
        editEmail = findViewById(R.id.editEmail);
        editCity = findViewById(R.id.editCity);

        btnAddForm = findViewById(R.id.btnAddForm);
        btnClearAddForm = findViewById(R.id.btnClearAddForm);
    }

    private void setupButtons() {

        btnClearAddForm.setOnClickListener(v -> clearForm());

        btnAddForm.setOnClickListener(v -> attemptCreateMember());
    }

    private void attemptCreateMember() {
        String fullName = editFullName.getText().toString().trim();
        String dateOfBirth = editDateOfBirth.getText().toString().trim();
        String gender = spinnerGender.getSelectedItem().toString();
        String phoneNumber = editPhoneNumber.getText().toString().trim();
        String email = editEmail.getText().toString().trim();
        String city = editCity.getText().toString().trim();


        boolean valid = true;
        valid &= requireField(editFullName);
        valid &= requireField(editDateOfBirth);
        valid &= requireField(editPhoneNumber);
        valid &= requireField(editEmail);
        valid &= requireField(editCity);

        if (!valid) {
            Toast.makeText(this, "Please fill in all highlighted fields.", Toast.LENGTH_SHORT).show();
            return;
        }

        showCreateConfirmation(fullName, dateOfBirth, gender, phoneNumber, email, city);
    }

    private boolean requireField(EditText field) {
        if (field.getText().toString().trim().isEmpty()) {
            field.setHintTextColor(Color.RED);

            field.setOnFocusChangeListener((v, hasFocus) -> {
                if (hasFocus) {
                    field.setHintTextColor(Color.GRAY);
                }
            });

            return false;
        }
        return true;
    }

    private void showCreateConfirmation(String fullName, String dateOfBirth, String gender, String phoneNumber, String email, String city) {

        String message =
                "Full Name: " + fullName + "\n" + "Date of Birth: " + dateOfBirth + "\n" + "Gender: " + gender + "\n" + "Phone Number: " + phoneNumber + "\n" + "Email: " + email + "\n" + "City: " + city;

        new AlertDialog.Builder(this)
                .setTitle("Confirm New Member")
                .setMessage(message)
                .setNegativeButton("Cancel", null)
                .setPositiveButton("Create", (dialog, which) -> {
                    createMember(fullName, dateOfBirth, gender, phoneNumber, email, city);
                })
                .show();
    }

    private void createMember(String fullName, String dateOfBirth, String gender, String phoneNumber, String email, String city) {

        // TODO: replace this with actual database insert.
        // database.memberDao().insert(new Member(fullName, dateOfBirth, gender, phoneNumber, email, city));

        Toast.makeText(this, "Member creation hook reached.", Toast.LENGTH_SHORT).show();

        clearForm();
    }

    private void clearForm() {
        editFullName.setText("");
        editDateOfBirth.setText("");
        spinnerGender.setSelection(0);
        editPhoneNumber.setText("");
        editEmail.setText("");
        editCity.setText("");
    }
}
