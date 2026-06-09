package com.example.booleangoes;

import android.os.Bundle;
import android.widget.FrameLayout;

import androidx.appcompat.app.AppCompatActivity;
import android.view.View;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import android.content.Intent;
import android.widget.Toast;
import androidx.appcompat.app.AlertDialog;

import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import android.util.Log;

public class ViewActivity extends AppCompatActivity {

    private RecyclerView memberRecyclerView;
    private ArrayList<Member> members;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view);

        connectViews();
        //loadMembersFromServer();
        setupRecyclerView();
//Test cases
        //testReadAllRaw();
        //setupFakeMembers();

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
    private void connectViews() {
        memberRecyclerView = findViewById(R.id.memberRecyclerView);
    }

    private Member parseMemberLine(String line) {
        try {
            String memberNumber = getBetween(line, "MemberID: ", ", First Name:");
            String firstName = getBetween(line, "First Name: ", " LastName:");
            String lastName = getBetween(line, "LastName: ", " Date of Birth");
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
    private void loadMembersFromServer() {
        Log.d("SERVER_COMMAND", "READALL");
        ServerClient.sendCommand("READALL", new ServerClient.ServerCallback() {
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

            @Override
            public void onError(String error) {
                runOnUiThread(() ->
                        Toast.makeText(ViewActivity.this, "Server error: " + error, Toast.LENGTH_LONG).show()
                );
            }
        });
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

    private void setupRecyclerView() {
        members = new ArrayList<>();
        MemberAdapter adapter = new MemberAdapter(this, members);

        memberRecyclerView.setLayoutManager(new LinearLayoutManager(this));
        memberRecyclerView.setAdapter(adapter);
    }

    @Override
    protected void onResume() {
        super.onResume();

        loadMembersFromServer();
    }

}
