package com.example.todoplans.adapter;

import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.todoplans.AddNewPlan;
import com.example.todoplans.MainActivity;
import com.example.todoplans.R;
import com.example.todoplans.model.TodoModel;
import com.example.todoplans.utils.DatabaseHandler;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

public class TodoAdapter extends RecyclerView.Adapter<TodoAdapter.ViewHolder> {
    private List<TodoModel> todoList;
    private MainActivity mainActivity;
    private DatabaseHandler db;

    public TodoAdapter(DatabaseHandler db, MainActivity mainActivity) {
        this.db = db;
        this.mainActivity = mainActivity;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.list_item, parent, false);

        return new ViewHolder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        db.openDatabase();
        final TodoModel item = todoList.get(position);

        holder.checkBox.setText(item.getPlan());
        holder.checkBox.setChecked(toBool(item.getStatus()));
        holder.checkBox.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked){
                    db.updateStatus(item.getId(),1);
                } else {
                    db.updateStatus(item.getId(), 0);
                }
            }
        });
    }



    private boolean toBool(int bool){
        return bool != 0;
    }
    
    public void setPlan(List<TodoModel> todoList){
        this.todoList = todoList;
        notifyDataSetChanged();
    }

    @Override
    public int getItemCount() {
        return todoList.size();
    }

    public Context getContext(){
        return mainActivity;
    }

    public void deleteItem(int position){
        TodoModel item = todoList.get(position);
        db.deletePlan(item.getId());
        todoList.remove(position);
        notifyItemRemoved(position);
    }

    public void editItem(int position){
        TodoModel item = todoList.get(position);
        Bundle bundle = new Bundle();
        bundle.putInt("id", item.getId());
        bundle.putString("plan", item.getPlan());
        AddNewPlan fragment = new AddNewPlan();
        fragment.setArguments(bundle);
        fragment.show(mainActivity.getSupportFragmentManager(), AddNewPlan.TAG);
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        CheckBox checkBox;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);

            checkBox = itemView.findViewById(R.id.ceck_box);
        }
    }


}
