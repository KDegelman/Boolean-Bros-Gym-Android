package com.example.booleangoes;

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

public class RemoveActivity extends AppCompatActivity {

    private LinearLayout removeSearchCluster;
    private LinearLayout removeMemberCluster;

    private EditText searchMemberNum;
    private EditText searchFullName;
    private EditText searchDateOfBirth;
    private EditText searchPhoneNumber;
    private EditText searchEmail;

    private EditText removeFullName;
    private EditText removeDateOfBirth;
    private EditText removePhoneNumber;
    private EditText removeEmail;
    private EditText removeCity;

    private Spinner removeSpinnerGender;

    private Button btnRemoveSearch;
    private Button btnRemoveMember;
    private Button btnRemoveCancel;

    private String currentMemberNumber = "";

    private EditText removeMemberNum;

    private boolean openedFromView = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_remove);
        View main = findViewById(R.id.main);

        connectViews();
        setupButtons();
        setupGenderSpinner();

        //I had to add this to make it work, I got it from the internet so I don't fully understand why it works or why I need it
        //It fixes the app from being behind the top info bar
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

        removeMemberNum.setText(memberNumber);
        removeFullName.setText(getIntent().getStringExtra("fullName"));
        removeDateOfBirth.setText(getIntent().getStringExtra("dateOfBirth"));
        removePhoneNumber.setText(getIntent().getStringExtra("phoneNumber"));
        removeEmail.setText(getIntent().getStringExtra("email"));
        removeCity.setText(getIntent().getStringExtra("city"));

        String gender = getIntent().getStringExtra("gender");

        if ("Male".equalsIgnoreCase(gender))
            removeSpinnerGender.setSelection(1);
        else if ("Female".equalsIgnoreCase(gender))
            removeSpinnerGender.setSelection(2);
        else
            removeSpinnerGender.setSelection(0);

        removeSearchCluster.setVisibility(View.GONE);
        removeMemberCluster.setVisibility(View.VISIBLE);
    }

    private void setupGenderSpinner() {
        String[] genderOptions = {"N/A", "Male", "Female"};

        ArrayAdapter<String> adapter = new ArrayAdapter<>(
                this,
                android.R.layout.simple_spinner_item,
                genderOptions
        );

        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

        removeSpinnerGender.setAdapter(adapter);
    }
    private void connectViews() {
        removeSearchCluster = findViewById(R.id.removeSearchCluster);
        removeMemberCluster = findViewById(R.id.removeMemberCluster);

        searchMemberNum = findViewById(R.id.searchMemberNum);
        searchFullName = findViewById(R.id.searchFullName);
        searchDateOfBirth = findViewById(R.id.searchDateOfBirth);
        searchPhoneNumber = findViewById(R.id.searchPhoneNumber);
        searchEmail = findViewById(R.id.searchEmail);

        removeMemberNum = findViewById(R.id.removeMemberNum);
        removeFullName = findViewById(R.id.removeFullName);
        removeDateOfBirth = findViewById(R.id.removeDateOfBirth);
        removeSpinnerGender = findViewById(R.id.removeSpinnerGender);
        removePhoneNumber = findViewById(R.id.removePhoneNumber);
        removeEmail = findViewById(R.id.removeEmail);
        removeCity = findViewById(R.id.removeCity);

        btnRemoveSearch = findViewById(R.id.btnRemoveSearch);
        btnRemoveMember = findViewById(R.id.btnRemoveMember);
        btnRemoveCancel = findViewById(R.id.btnRemoveCancel);
    }

    private void setupButtons() {

        btnRemoveSearch.setOnClickListener(v -> attemptSearchMember());

        btnRemoveMember.setOnClickListener(v -> showRemoveConfirmation());

        btnRemoveCancel.setOnClickListener(v -> {

            if (openedFromView) {
                finish();
            } else {
                clearRemoveFields();
                removeMemberCluster.setVisibility(View.GONE);
                removeSearchCluster.setVisibility(View.VISIBLE);
            }

        });
    }

    private void attemptSearchMember() {
        String memberNum = searchMemberNum.getText().toString().trim();
        String name = searchFullName.getText().toString().trim();
        String dob = searchDateOfBirth.getText().toString().trim();
        String phone = searchPhoneNumber.getText().toString().trim();
        String email = searchEmail.getText().toString().trim();

        //For testing
        if (!memberNum.isEmpty()) {
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
        removeMemberNum.setText(currentMemberNumber);
        //removeMemberNum = "42069";
        //removeMemberNum.setText(removeMemberNum);


        removeFullName.setText("Fake User");
        removeDateOfBirth.setText("2000-01-1");
        removeSpinnerGender.setSelection(1);
        removePhoneNumber.setText("306 123 4567");
        removeEmail.setText("FakeEmail@email.com");
        removeCity.setText("Regina");

        clearSearchFields();

        removeSearchCluster.setVisibility(View.GONE);
        removeMemberCluster.setVisibility(View.VISIBLE);
    }

    private void showRemoveConfirmation() {
        String gender = removeSpinnerGender.getSelectedItem().toString();
        String message =
                "Member #: " + currentMemberNumber + "\n" + "Full Name: " + removeFullName.getText().toString() + "\n" + "Date of Birth: " + removeDateOfBirth.getText().toString() + "\n" + "Gender: " + gender + "\n" + "Phone Number: " + removePhoneNumber.getText().toString() + "\n" + "Email: " + removeEmail.getText().toString() + "\n" + "City: " + removeCity.getText().toString();

        new AlertDialog.Builder(this)
                .setTitle("Confirm Remove")
                .setMessage(message)
                .setNegativeButton("Cancel", null)
                .setPositiveButton("Remove", (dialog, which) -> removeMember())
                .show();
    }

    private void removeMember() {
        // TODO: replace this with real database.
        Toast.makeText(this, "Remove hook reached.", Toast.LENGTH_SHORT).show();

        if (openedFromView)
            finish();
        else {
            clearRemoveFields();
            removeMemberCluster.setVisibility(View.GONE);
            removeSearchCluster.setVisibility(View.VISIBLE);
        }
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

    private void clearRemoveFields() {
        removeFullName.setText("");
        removeDateOfBirth.setText("");
        removeSpinnerGender.setSelection(0);
        removePhoneNumber.setText("");
        removeEmail.setText("");
        removeCity.setText("");
        //removeMemberNum = "";
        removeMemberNum.setText("");
    }
}