package com.example.booleangoes;

import android.graphics.Color;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.Toast;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

public class EditActivity extends AppCompatActivity {

    private LinearLayout editSearchCluster;
    private LinearLayout editMemberCluster;

    private EditText searchMemberNum;
    private EditText searchFullName;
    private EditText searchDateOfBirth;
    private EditText searchPhoneNumber;
    private EditText searchEmail;

    private EditText editFullName;
    private EditText editDateOfBirth;
    private Spinner spinnerGender;
    private EditText editPhoneNumber;
    private EditText editEmail;
    private EditText editCity;

    private Button btnEditSearch;
    private Button btnUpdateMember;

    private String currentMemberNumber = "";
    private Button btnEditCancel;

    private EditText editMemberNum;

    private boolean openedFromView = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit);

        connectViews();
        setupButtons();
        setupGenderSpinner();

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

        checkForIncomingMember();

    }

    private void checkForIncomingMember() {

        openedFromView = getIntent().getBooleanExtra("fromViewPage", false);

        String memberNumber = getIntent().getStringExtra("memberNumber");

        if (memberNumber == null)
            return;

        currentMemberNumber = memberNumber;

        editMemberNum.setText(memberNumber);
        editFullName.setText(getIntent().getStringExtra("fullName"));
        editDateOfBirth.setText(getIntent().getStringExtra("dateOfBirth"));
        editPhoneNumber.setText(getIntent().getStringExtra("phoneNumber"));
        editEmail.setText(getIntent().getStringExtra("email"));
        editCity.setText(getIntent().getStringExtra("city"));

        String gender = getIntent().getStringExtra("gender");

        if ("Male".equalsIgnoreCase(gender))
            spinnerGender.setSelection(1);
        else if ("Female".equalsIgnoreCase(gender))
            spinnerGender.setSelection(2);
        else
            spinnerGender.setSelection(0);

        editSearchCluster.setVisibility(View.GONE);
        editMemberCluster.setVisibility(View.VISIBLE);
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
        editSearchCluster = findViewById(R.id.editSearchCluster);
        editMemberCluster = findViewById(R.id.editMemberCluster);

        searchMemberNum = findViewById(R.id.searchMemberNum);
        searchFullName = findViewById(R.id.searchFullName);
        searchDateOfBirth = findViewById(R.id.searchDateOfBirth);
        searchPhoneNumber = findViewById(R.id.searchPhoneNumber);
        searchEmail = findViewById(R.id.searchEmail);

        editFullName = findViewById(R.id.editFullName);
        editDateOfBirth = findViewById(R.id.editDateOfBirth);
        spinnerGender = findViewById(R.id.spinnerGender);
        editPhoneNumber = findViewById(R.id.editPhoneNumber);
        editEmail = findViewById(R.id.editEmail);
        editCity = findViewById(R.id.editCity);
        editMemberNum = findViewById(R.id.editMemberNum);

        btnEditSearch = findViewById(R.id.btnEditSearch);
        btnUpdateMember = findViewById(R.id.btnUpdateMember);
        btnEditCancel = findViewById(R.id.btnEditCancel);
    }

    private void setupButtons() {
        btnEditSearch.setOnClickListener(v -> attemptSearchMember());

        btnUpdateMember.setOnClickListener(v -> attemptUpdateMember());

        btnEditCancel.setOnClickListener(v -> {

            if (openedFromView) {
                finish();
            } else {
                clearEditFields();
                editMemberCluster.setVisibility(View.GONE);
                editSearchCluster.setVisibility(View.VISIBLE);
            }

        });
    }

    private void attemptSearchMember() {
        String memberNum = searchMemberNum.getText().toString().trim();
        String name = searchFullName.getText().toString().trim();
        String dob = searchDateOfBirth.getText().toString().trim();
        String phone = searchPhoneNumber.getText().toString().trim();
        String email = searchEmail.getText().toString().trim();

        if (!memberNum.isEmpty()) {
            if (!name.isEmpty()) searchFullName.setHintTextColor(Color.RED);
            if (!dob.isEmpty()) searchDateOfBirth.setHintTextColor(Color.RED);
            if (!phone.isEmpty()) searchPhoneNumber.setHintTextColor(Color.RED);
            if (!email.isEmpty()) searchEmail.setHintTextColor(Color.RED);

            //For testing
            if (memberNum.equals("42069")) {
                loadFakeUser();
            } else {
                showNotFoundDialog();
            }

            return;
        }

        int filledCount = 0;
        if (!name.isEmpty()) filledCount++;
        if (!dob.isEmpty()) filledCount++;
        if (!phone.isEmpty()) filledCount++;
        if (!email.isEmpty()) filledCount++;

        if (filledCount < 2) {
            Toast.makeText(this, "Enter Member # or at least two search fields.", Toast.LENGTH_SHORT).show();
            return;
        }

        //For testing
        boolean fakeMatch =
                name.equalsIgnoreCase("Fake User") || dob.equals("2000-01-1") || phone.equals("306 123 4567") || email.equalsIgnoreCase("FakeEmail@email.com");

        if (fakeMatch) {
            loadFakeUser();
        } else {
            showNotFoundDialog();
        }
    }
    //For testing
    private void loadFakeUser() {
        currentMemberNumber = "42069";
        editMemberNum.setText(currentMemberNumber);

        editFullName.setText("Fake User");
        editDateOfBirth.setText("2000-01-1");
        spinnerGender.setSelection(1);
        editPhoneNumber.setText("306 123 4567");
        editEmail.setText("FakeEmail@email.com");
        editCity.setText("Regina");

        clearSearchFields();

        editSearchCluster.setVisibility(View.GONE);
        editMemberCluster.setVisibility(View.VISIBLE);
    }

    private void attemptUpdateMember() {
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

        String fullName = editFullName.getText().toString().trim();
        String dob = editDateOfBirth.getText().toString().trim();
        String gender = spinnerGender.getSelectedItem().toString();
        String phone = editPhoneNumber.getText().toString().trim();
        String email = editEmail.getText().toString().trim();
        String city = editCity.getText().toString().trim();

        String message =
                "Member #: " + currentMemberNumber + "\n" + "Full Name: " + fullName + "\n" + "Date of Birth: " + dob + "\n" + "Gender: " + gender + "\n" + "Phone Number: " + phone + "\n" + "Email: " + email + "\n" + "City: " + city;

        new AlertDialog.Builder(this)
                .setTitle("Confirm Edit")
                .setMessage(message)
                .setNegativeButton("Cancel", null)
                .setPositiveButton("Update", (dialog, which) -> {
                    updateMember(fullName, dob, gender, phone, email, city);
                })
                .show();
    }

    private void updateMember(String fullName, String dob, String gender, String phone, String email, String city) {
        // TODO: replace this with real database.

        Toast.makeText(this, "Update hook reached.", Toast.LENGTH_SHORT).show();

        if (openedFromView)
            finish();
        else {
            clearEditFields();
            editMemberCluster.setVisibility(View.GONE);
            editSearchCluster.setVisibility(View.VISIBLE);
        }
    }
    //TODO: I should remove the colour changing it's stupid in its current state.
    private boolean requireField(EditText field) {
        if (field.getText().toString().trim().isEmpty()) {
            //field.setHintTextColor(Color.RED);

            field.setOnFocusChangeListener((v, hasFocus) -> {
                if (hasFocus) {
                    //field.setHintTextColor(Color.GRAY);
                }
            });

            return false;
        }

        return true;
    }

    private void showNotFoundDialog() {
        new AlertDialog.Builder(this)
                .setTitle("Member Not Found")
                .setMessage("No member matched the search information. Please check the fields and try again.")
                .setPositiveButton("Try Again", null)
                .show();
    }

    private void clearSearchFields() {
        searchMemberNum.setText("");
        searchFullName.setText("");
        searchDateOfBirth.setText("");
        searchPhoneNumber.setText("");
        searchEmail.setText("");
    }

    private void clearEditFields() {
        editFullName.setText("");
        editDateOfBirth.setText("");
        spinnerGender.setSelection(0);
        editPhoneNumber.setText("");
        editEmail.setText("");
        editCity.setText("");
        currentMemberNumber = "";
    }
}
