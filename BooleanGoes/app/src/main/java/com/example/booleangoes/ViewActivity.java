package com.example.booleangoes;

import android.os.Bundle;
import android.view.View;
import android.util.Log;

import android.widget.FrameLayout;
import android.widget.Toast;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

//Lets you create the scrolling menus
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;


public class ViewActivity extends AppCompatActivity {


    private RecyclerView memberRecyclerView;
    private ArrayList<Member> members;
    private FrameLayout btnBack;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view);
        View main = findViewById(R.id.main);

        //Attaches the recycler & buttons to ID's
        memberRecyclerView = findViewById(R.id.memberRecyclerView);

        btnBack = findViewById(R.id.btnBack);


        btnBack.setOnClickListener(v -> {
            finish();
        });


        ViewCompat.setOnApplyWindowInsetsListener(main, (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(0, systemBars.top, 0, 0);
            return insets;
        });


        //Fills the list with DB Members
        setupRecyclerView();
//Test cases
        //testReadAllRaw();
        //setupFakeMembers();
        //loadMembersFromServer();
    }

    private void setupRecyclerView() {
        members = new ArrayList<>();
        //Calls for MemberAdapter to turn the data in to something readable
        MemberAdapter adapter = new MemberAdapter(this, members);

        //Arrange cards vertically
        memberRecyclerView.setLayoutManager(new LinearLayoutManager(this));
        memberRecyclerView.setAdapter(adapter);
    }

    //When the page is opened this runs everytime(refreshes the page)
    @Override
    protected void onResume() {
        super.onResume();

        loadMembersFromServer();
    }

    //Sends a request to the server to grab the members entries
    private void loadMembersFromServer() {

        //Logcat entry
        Log.d("SERVER_COMMAND", "READALL");

        //Sends server the READALL command, which sends back all the member data
        ServerClient.sendCommand("READALL", new ServerClient.ServerCallback() {
            //If the server replies successfully, positive pop up and adds member to the array
            @Override
            public void onResult(ArrayList<String> lines) {
                runOnUiThread(() -> {
                    members.clear();

                    for (String line : lines) {
                        Member member = parseMemberLine(line);
                        if (member != null) {
                            members.add(member);
                        }
                    }

                    memberRecyclerView.getAdapter().notifyDataSetChanged();
                });
            }

            //If the server call is negative, negative popup with hopefully an explanation
            @Override
            public void onError(String error) {
                Log.e("SERVER_ERROR", error);
                runOnUiThread(() ->
                        Toast.makeText(ViewActivity.this, "Server error: " + error, Toast.LENGTH_LONG).show()
                );
            }
        });
    }

    //Takes the server data from the DB and parses it into a format that can be used
    private Member parseMemberLine(String line) {
        try {
            //Each line sends off a request for getBetween to find the data that's in-between two categories(collums) and then stores it in a string
            String memberNumber = getBetween(line, "MemberID: ", ", First Name:");
            String firstName = getBetween(line, "First Name: ", " LastName:");
            String lastName = getBetween(line, "LastName: ", " Date of Birth");
            String dob = getBetween(line, "Date of Birth", ", Email:");
            String email = getBetween(line, "Email: ", ", Phone Number:");
            String phone = getBetween(line, "Phone Number: ", ", Gender:");
            String gender = getBetween(line, "Gender: ", ", City of Residence:");
            String city = line.substring(line.indexOf("City of Residence:") + "City of Residence:".length()).trim();

            //It then pieces it all together here in the format that android is using
            return new Member(memberNumber, firstName + " " + lastName, dob, gender, phone, email, city);

        } catch (Exception e) {
            return null;
        }
    }

    //Gets given instructions to find the words in-between to points in the string
    private String getBetween(String text, String start, String end) {
        int startIndex = text.indexOf(start) + start.length();
        int endIndex = text.indexOf(end);

        return text.substring(startIndex, endIndex).trim();
    }

//For testing
    private void setupFakeMembers() {
        members = new ArrayList<>();

        members.add(new Member(
                "42069",
                "Fake User",
                "2000-01-1",
                "Male",
                "306 123 4567",
                "FakeEmail@email.com",
                "Regina"
        ));

        members.add(new Member(
                "42070",
                "Example Person",
                "1999-05-12",
                "Female",
                "306 555 1111",
                "example@email.com",
                "Saskatoon"
        ));
    }

    private void testReadAllRaw() {
        Log.d("SERVER_COMMAND", "READALL");
        ServerClient.sendCommand("READALL", new ServerClient.ServerCallback() {
            @Override
            public void onResult(ArrayList<String> lines) {
                runOnUiThread(() -> {
                    String raw = String.join("\n", lines);

                    new AlertDialog.Builder(ViewActivity.this)
                            .setTitle("READALL Raw Server Response")
                            .setMessage(raw)
                            .setPositiveButton("OK", null)
                            .show();
                });
            }

            @Override
            public void onError(String error) {
                runOnUiThread(() ->
                        Toast.makeText(ViewActivity.this, "Server error: " + error, Toast.LENGTH_LONG).show()
                );
            }
        });
    }

}
