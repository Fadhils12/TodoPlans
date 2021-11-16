package com.example.todoplans;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.ItemTouchHelper;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.DialogInterface;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.todoplans.adapter.TodoAdapter;
import com.example.todoplans.model.TodoModel;
import com.example.todoplans.utils.DatabaseHandler;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Objects;

public class MainActivity extends AppCompatActivity implements DialogCloseListener{

    private RecyclerView rvPlan;
    private TodoAdapter planAdapter;
    private FloatingActionButton fab;


    private List<TodoModel> planList;
    DatabaseHandler db;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Objects.requireNonNull(getSupportActionBar()).hide();

        db = new DatabaseHandler(this);
        db.openDatabase();

        planList = new ArrayList<>();

        rvPlan = findViewById(R.id.rv_plan);
        rvPlan.setLayoutManager(new LinearLayoutManager(this));
        planAdapter = new TodoAdapter(db, this);
        rvPlan.setAdapter(planAdapter);


        fab = findViewById(R.id.fab);


        ItemTouchHelper itemTouchHelper = new ItemTouchHelper(new RvItemTouchHelper(planAdapter));
        itemTouchHelper.attachToRecyclerView(rvPlan);

        planList = db.getAllPlans();
        Collections.reverse(planList);
        planAdapter.setPlan(planList);

        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AddNewPlan.newInstance().show(getSupportFragmentManager(), AddNewPlan.TAG);
            }
        });
    }

    @Override
    public void handleDialogClose(DialogInterface dialog) {
        planList = db.getAllPlans();
        Collections.reverse(planList);
        planAdapter.setPlan(planList);
        planAdapter.notifyDataSetChanged();
    }
}