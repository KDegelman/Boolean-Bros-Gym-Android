package com.example.booleangoes;

import android.os.Bundle;
import android.view.View;
import android.util.Log;
import android.graphics.Color;

import android.widget.FrameLayout;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;
import android.widget.Spinner;
import android.widget.ArrayAdapter;

import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import java.util.ArrayList;

//Creates the screen on load with default android behavior
public class CreateActivity extends AppCompatActivity {

    //Declaring the variables that are going to be used
    private EditText editFullName;
    private EditText editDateOfBirth;
    private Spinner spinnerGender;
    private EditText editPhoneNumber;
    private EditText editEmail;
    private EditText editCity;
    private Button btnAddForm;
    private Button btnClearAddForm;
    private FrameLayout btnBack;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create);
        //Gets page layout by id
        View main = findViewById(R.id.main);


        //Fills all the variables using their ID's
        editFullName = findViewById(R.id.editFullName);
        editDateOfBirth = findViewById(R.id.editDateOfBirth);
        editPhoneNumber = findViewById(R.id.editPhoneNumber);
        editEmail = findViewById(R.id.editEmail);
        editCity = findViewById(R.id.editCity);

        spinnerGender = findViewById(R.id.spinnerGender);

        btnAddForm = findViewById(R.id.btnAddForm);
        btnClearAddForm = findViewById(R.id.btnClearAddForm);
        btnBack = findViewById(R.id.btnBack);


        //Sets up listener hooks for the buttons
        btnClearAddForm.setOnClickListener(v -> clearForm());
        btnAddForm.setOnClickListener(v -> attemptCreateMember());
        //Listens for back button to be clicked(Go back to previous page)
        btnBack.setOnClickListener(v -> {
            finish();}
        );


        //Creates drop down options
        String[] genderOptions = {"N/A", "Male", "Female"};

        //Creates the object that lets the spinner access the options(first line is closed version, second line is open version
        ArrayAdapter<String> adapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_item, genderOptions);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

        //Attaches the options to the spinner id
        spinnerGender.setAdapter(adapter);


        //Keeps the app below the Android info bar by listening for spacing
        ViewCompat.setOnApplyWindowInsetsListener(main, (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());v.setPadding(0, systemBars.top, 0, 0);
            return insets;
        });
    }

    //Makes all the fields blank
    private void clearForm() {
        editFullName.setText("");
        editDateOfBirth.setText("");
        spinnerGender.setSelection(0);
        editPhoneNumber.setText("");
        editEmail.setText("");
        editCity.setText("");
    }

    //Starts the process of creating a new member
    private void attemptCreateMember() {
        //Fills a bunch of strings with the data from the fields (minor data uniformity with trim)
        String fullName = editFullName.getText().toString().trim();
        String dateOfBirth = editDateOfBirth.getText().toString().trim();
        String gender = spinnerGender.getSelectedItem().toString();
        String phoneNumber = editPhoneNumber.getText().toString().trim();
        String email = editEmail.getText().toString().trim();
        String city = editCity.getText().toString().trim();

        //Data validation for commas
        if (fullName.contains(",") || city.contains(",") || email.contains(",")) {
            Toast.makeText(this, "Fields cannot contain commas.", Toast.LENGTH_SHORT).show();
            return;
        }

        //Assumed innocent
        boolean valid = true;

        //Are all the string filled? if no the field wasn't occupied with data
        valid &= requireField(editFullName);
        valid &= requireField(editDateOfBirth);
        valid &= requireField(editPhoneNumber);
        valid &= requireField(editEmail);
        valid &= requireField(editCity);

        if (!valid) {
            Toast.makeText(this, "Please fill in all highlighted fields.", Toast.LENGTH_SHORT).show();
            return;
        }

        //Notification for user to confirm if data is correct before it gets sent on
        showCreateConfirmation(fullName, dateOfBirth, gender, phoneNumber, email, city);
    }

    //If field is empty, this makes it red
    private boolean requireField(EditText field) {
        if (field.getText().toString().trim().isEmpty()) {
            field.setHintTextColor(Color.RED);

            //If field has been clicked make field grey again
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

        //Makes msg about to be sent
        String message =
                "Full Name: " + fullName + "\n" + "Date of Birth: " + dateOfBirth + "\n" + "Gender: " + gender + "\n" + "Phone Number: " + phoneNumber + "\n" + "Email: " + email + "\n" + "City: " + city;

        //Sends msg with confirmation. If confirmed, will send info to DB hook
        new AlertDialog.Builder(this)
                .setTitle("Confirm New Member")
                .setMessage(message)
                .setNegativeButton("Cancel", null)
                .setPositiveButton("Create", (dialog, which) -> {
                    createMember(fullName, dateOfBirth, gender, phoneNumber, email, city);
                })
                .show();
    }

    //Is what sends the data to the DB to create the entry
    private void createMember(String fullName, String dateOfBirth, String gender, String phoneNumber, String email, String city) {

        //Since the format is different in the DB the name needs to get split before saved
        String[] nameParts = splitFullName(fullName);
        String firstName = nameParts[0];
        String lastName = nameParts[1];

        //Creates the string that is going to get sent to the server to be saved in the DB in the format the DB uses
        String command = "CREATE,0,"
                + firstName + ","
                + lastName + ","
                + dateOfBirth + ","
                + phoneNumber + ","
                + email + ","
                + gender + ","
                + city;

        //Prints the command in Logcat
        Log.d("SERVER_COMMAND", command);

        //Sends the command to the server socket
        ServerClient.sendCommand(command, new ServerClient.ServerCallback() {
            //If the server replies successfully, positive pop up and clear form
            @Override
            public void onResult(ArrayList<String> lines) {
                runOnUiThread(() -> {
                    Toast.makeText(CreateActivity.this, String.join("\n", lines), Toast.LENGTH_LONG).show();
                    clearForm();
                });
            }

            //If the server call is negative, negative popup with hopefully an explanation
            @Override
            public void onError(String error) {
                Log.e("SERVER_ERROR", error);
                runOnUiThread(() -> {
                    Toast.makeText(CreateActivity.this, "Server error: " + error, Toast.LENGTH_LONG).show();
                });
            }
        });

        //Notification for test purposes
        //Toast.makeText(this, "Member creation hook reached.", Toast.LENGTH_SHORT).show();

        //clear fields when done
        clearForm();
    }

    //Splits name into two parts
    private String[] splitFullName(String fullName) {
        String cleanedName = fullName.trim();

        String[] parts = cleanedName.split("\\s+", 2);

        String firstName = parts[0];
        String lastName = "";

        if (parts.length > 1) {
            lastName = parts[1];
        }

        return new String[]{firstName, lastName};
    }

}
