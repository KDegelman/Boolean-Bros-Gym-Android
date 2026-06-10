package com.example.booleangoes;

import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;
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

import java.util.ArrayList;

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
    private FrameLayout loadingOverlay;
    private FrameLayout btnBack;
    //Tracks if entered page from view or edit
    private boolean openedFromView = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit);
        View main = findViewById(R.id.main);

        editSearchCluster = findViewById(R.id.editSearchCluster);
        editMemberCluster = findViewById(R.id.editMemberCluster);
        loadingOverlay = findViewById(R.id.loadingOverlay);

        //Search page fields
        searchMemberNum = findViewById(R.id.searchMemberNum);
        searchFullName = findViewById(R.id.searchFullName);
        searchDateOfBirth = findViewById(R.id.searchDateOfBirth);
        searchPhoneNumber = findViewById(R.id.searchPhoneNumber);
        searchEmail = findViewById(R.id.searchEmail);
        //Edit page fields
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
        btnBack = findViewById(R.id.btnBack);


        btnBack.setOnClickListener(v -> {
            finish();
        });


        String[] genderOptions = {"N/A", "Male", "Female"};

        ArrayAdapter<String> adapter = new ArrayAdapter<>(
                this,
                android.R.layout.simple_spinner_item,
                genderOptions
        );

        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

        spinnerGender.setAdapter(adapter);

        ViewCompat.setOnApplyWindowInsetsListener(main, (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(0, systemBars.top, 0, 0);
            return insets;
        });


        setupButtons();
        checkForIncomingMember();
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

    //(this method needs streamlining)
    private void checkForIncomingMember() {

        //Was this page opened from the view page
        openedFromView = getIntent().getBooleanExtra("fromViewPage", false);

        //Store loaded member#
        String memberNumber = getIntent().getStringExtra("memberNumber");

        //If there is no loaded member number exit
        if (memberNumber == null)
            return;

        //hold on to member#
        currentMemberNumber = memberNumber;

        //Set pulled data
        editMemberNum.setText(memberNumber);
        editFullName.setText(getIntent().getStringExtra("fullName"));
        editDateOfBirth.setText(getIntent().getStringExtra("dateOfBirth"));
        editPhoneNumber.setText(getIntent().getStringExtra("phoneNumber"));
        editEmail.setText(getIntent().getStringExtra("email"));
        editCity.setText(getIntent().getStringExtra("city"));

        String gender = getIntent().getStringExtra("gender");

        //gender dropdown parser
        if ("Male".equalsIgnoreCase(gender))
            spinnerGender.setSelection(1);
        else if ("Female".equalsIgnoreCase(gender))
            spinnerGender.setSelection(2);
        else
            spinnerGender.setSelection(0);

        //Switch between pages
        editSearchCluster.setVisibility(View.GONE);
        editMemberCluster.setVisibility(View.VISIBLE);
    }

    private Member parseMemberLine(String line) {
        try {
            String memberNumber = getBetween(line, "MemberID: ", ", First Name:");
            String firstName = getBetween(line, "First Name: ", " LastName:");
            String lastName = getBetween(line, "LastName: ", "Date of Birth");
            String dob = getBetween(line, "Date of Birth", ", Email:");
            String email = getBetween(line, "Email: ", ", Phone Number:");
            String phone = getBetween(line, "Phone Number: ", ", Gender:");
            String gender = getBetween(line, "Gender: ", ", City of Residence:");
            String city = line.substring(line.indexOf("City of Residence:") + "City of Residence:".length()).trim();

            return new Member(memberNumber, firstName + " " + lastName, dob, gender, phone, email, city);
        } catch (Exception e) {
            return null;
        }
    }

    private String getBetween(String text, String start, String end) {
        int startIndex = text.indexOf(start) + start.length();
        int endIndex = text.indexOf(end);
        return text.substring(startIndex, endIndex).trim();
    }

    //The start of a DB call for a members search
    private void attemptSearchMember() {
        String memberNum = searchMemberNum.getText().toString().trim();
        String name = searchFullName.getText().toString().trim();
        String dob = searchDateOfBirth.getText().toString().trim();
        String phone = searchPhoneNumber.getText().toString().trim();
        String email = searchEmail.getText().toString().trim();

        if (name.contains(",") || email.contains(",")) {
            Toast.makeText(this, "Fields cannot contain commas.", Toast.LENGTH_SHORT).show();
            return;
        }

        if (!memberNum.isEmpty()) {
            //This whole section is unsued b/c the server DB was never set up for the two fields search
            if (!name.isEmpty()) searchFullName.setHintTextColor(Color.RED);
            if (!dob.isEmpty()) searchDateOfBirth.setHintTextColor(Color.RED);
            if (!phone.isEmpty()) searchPhoneNumber.setHintTextColor(Color.RED);
            if (!email.isEmpty()) searchEmail.setHintTextColor(Color.RED);

            //Stores the member# with the command to get ready
            String command = "READ," + memberNum;

            Log.d("SERVER_COMMAND", command);

            //Sends the command READ to the server to find and read the member of the specific #
            ServerClient.sendCommand(command, new ServerClient.ServerCallback() {
                //Did the server react
                @Override
                public void onResult(ArrayList<String> lines) {
                    runOnUiThread(() -> {
                        if (lines.isEmpty() || lines.get(0).contains("not found")) {
                            showNotFoundDialog();
                            return;
                        }

                        Member member = parseMemberLine(lines.get(0));

                        if (member == null) {
                            Toast.makeText(EditActivity.this, "Could not read server response.", Toast.LENGTH_LONG).show();
                            return;
                        }

                        //Fills all the fields with the data pulled
                        loadMemberIntoEditFields(member);
                    });
                    //Get rid of loading overlay
                    loadingOverlay.setVisibility(View.GONE);
                }

                @Override
                public void onError(String error) {
                    runOnUiThread(() ->
                            Toast.makeText(EditActivity.this, "Server error: " + error, Toast.LENGTH_LONG).show()
                    );
                    //Get rid of loading overlay
                    loadingOverlay.setVisibility(View.GONE);
                }
            });

//For testing
//            if (memberNum.equals("42069")) {
//                loadFakeUser();
//            }
//            else {
//                showNotFoundDialog();
//            }

            return;
        }

        //This whole section is unsued b/c the server DB was never set up for the two fields search
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
//        boolean fakeMatch =
//                name.equalsIgnoreCase("Fake User") || dob.equals("2000-01-1") || phone.equals("306 123 4567") || email.equalsIgnoreCase("FakeEmail@email.com");
//
//        if (fakeMatch) {
//            loadFakeUser();
//        } else {
//            showNotFoundDialog();
//        }
    }

    private void showNotFoundDialog() {
        new AlertDialog.Builder(this)
                .setTitle("Member Not Found")
                .setMessage("No member matched the search information. Please check the fields and try again.")
                .setPositiveButton("Try Again", null)
                .show();
    }

    //Fills the fields
    private void loadMemberIntoEditFields(Member member) {
        currentMemberNumber = member.memberNumber;

        editMemberNum.setText(member.memberNumber);
        editFullName.setText(member.fullName);
        editDateOfBirth.setText(member.dateOfBirth);
        editPhoneNumber.setText(member.phoneNumber);
        editEmail.setText(member.email);
        editCity.setText(member.city);

        if ("Male".equalsIgnoreCase(member.gender))
            spinnerGender.setSelection(1);
        else if ("Female".equalsIgnoreCase(member.gender))
            spinnerGender.setSelection(2);
        else
            spinnerGender.setSelection(0);

        clearSearchFields();

        editSearchCluster.setVisibility(View.GONE);
        editMemberCluster.setVisibility(View.VISIBLE);
    }

    private void clearSearchFields() {
        searchMemberNum.setText("");
        searchFullName.setText("");
        searchDateOfBirth.setText("");
        searchPhoneNumber.setText("");
        searchEmail.setText("");
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

        //Contructs the command/String for the server DB
        String fullName = editFullName.getText().toString().trim();
        String dob = editDateOfBirth.getText().toString().trim();
        String gender = spinnerGender.getSelectedItem().toString();
        String phone = editPhoneNumber.getText().toString().trim();
        String email = editEmail.getText().toString().trim();
        String city = editCity.getText().toString().trim();

        String message =
                "Member #: " + currentMemberNumber + "\n" + "Full Name: " + fullName + "\n" + "Date of Birth: " + dob + "\n" + "Gender: " + gender + "\n" + "Phone Number: " + phone + "\n" + "Email: " + email + "\n" + "City: " + city;

        //Confirmation
        new AlertDialog.Builder(this)
                .setTitle("Confirm Edit")
                .setMessage(message)
                .setNegativeButton("Cancel", null)
                .setPositiveButton("Update", (dialog, which) -> {
                    updateMember(fullName, dob, gender, phone, email, city);
                })
                .show();
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

    //This sends the actual server command/request
    private void updateMember(String fullName, String dob, String gender, String phone, String email, String city) {

        //covers screen with an overlay so the user doesn't do anything while the long command takes place
        loadingOverlay.setVisibility(View.VISIBLE);

        String[] nameParts = splitFullName(fullName);
        String firstName = nameParts[0];
        String lastName = nameParts[1];

        ArrayList<String> commands = new ArrayList<>();

        //It does everything, b/c I have no way to decern between any of the data due to the way the DB is set up
        commands.add("UPDATE," + currentMemberNumber + ",First_Name," + firstName);
        commands.add("UPDATE," + currentMemberNumber + ",Last_Name," + lastName);
        commands.add("UPDATE," + currentMemberNumber + ",Date_of_Birth," + dob);
        commands.add("UPDATE," + currentMemberNumber + ",Email," + email);
        commands.add("UPDATE," + currentMemberNumber + ",Phone_Number," + phone);
        commands.add("UPDATE," + currentMemberNumber + ",Gender," + gender);
        commands.add("UPDATE," + currentMemberNumber + ",City_of_Residence," + city);

        for (String command : commands) {
            Log.d("SERVER_COMMAND", command);
        }

        sendUpdateCommands(commands, 0);

        //Toast.makeText(this, "Update hook reached.", Toast.LENGTH_SHORT).show();

// TODO What is this!?! I forget
//        if (openedFromView)
//            finish();
//        else {
//            clearEditFields();
//            editMemberCluster.setVisibility(View.GONE);
//            editSearchCluster.setVisibility(View.VISIBLE);
//        }
    }

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

    //Sends the individual commands to the DB(I want to fix this later)
    private void sendUpdateCommands(ArrayList<String> commands, int index) {
        if (index >= commands.size()) {
            runOnUiThread(() -> {
                Toast.makeText(EditActivity.this, "Update commands sent.", Toast.LENGTH_LONG).show();
                finish();
            });
            return;
        }

        String command = commands.get(index);

        Log.d("SERVER_COMMAND", command);

        ServerClient.sendCommand(command, new ServerClient.ServerCallback() {
            @Override
            public void onResult(ArrayList<String> lines) {
                sendUpdateCommands(commands, index + 1);

            }

            @Override
            public void onError(String error) {
                runOnUiThread(() -> {
                    Toast.makeText(
                            EditActivity.this,
                            "Server error: " + error,
                            Toast.LENGTH_LONG
                    ).show();
                });

            }
        });
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

}
