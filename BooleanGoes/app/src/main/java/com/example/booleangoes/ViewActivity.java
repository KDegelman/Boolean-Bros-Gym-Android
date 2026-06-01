package com.example.booleangoes;

import android.os.Bundle;
import android.widget.FrameLayout;

import androidx.appcompat.app.AppCompatActivity;
import android.view.View;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import android.content.Intent;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

public class ViewActivity extends AppCompatActivity {

    private RecyclerView memberRecyclerView;
    private ArrayList<Member> members;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view);

        connectViews();
        setupFakeMembers();
        setupRecyclerView();

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

    private void setupRecyclerView() {
        MemberAdapter adapter = new MemberAdapter(this, members);

        memberRecyclerView.setLayoutManager(new LinearLayoutManager(this));
        memberRecyclerView.setAdapter(adapter);
    }

}
