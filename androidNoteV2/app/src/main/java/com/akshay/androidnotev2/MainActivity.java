package com.akshay.androidnotev2;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {
    private static final String TAG = "MainActivity";
    private static final String TITLE = "androidNotes";
    private final List<Notes> noteslist = new ArrayList<>();
    private static final int IF_NEW_NOTE = 0;
    private static final int IF_EXISTING_NOTE = 1;
    private boolean noteListChanged = false;

    private RecyclerView recyclerView;
    private NotesAdapter notesAdapter;
    Notes notes = new Notes();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        recyclerView = findViewById((R.id.recyclerView));
        notesAdapter = new NotesAdapter(noteslist, this);

        recyclerView.setAdapter(notesAdapter);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        notes = loadFile();
        setTitle(TITLE + " (" + noteslist.size() + ")");
    }

}