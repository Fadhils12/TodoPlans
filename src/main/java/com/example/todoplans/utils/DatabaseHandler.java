package com.example.todoplans.utils;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import com.example.todoplans.model.TodoModel;

import java.util.ArrayList;
import java.util.List;

public class DatabaseHandler extends SQLiteOpenHelper {
    private static final int VERSION = 1;
    private static final String NAME = "todo_database";
    private static final String TODO_TABLE = "todo";
    private static final String ID = "id";
    private static final String PLAN = "task";
    private static final String STATUS = "status";
    private static final String CREATE_TODO_TABLE = "CREATE TABLE " + TODO_TABLE + "(" + ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " + PLAN + " TEXT, "
            + STATUS + " INTEGER)";


    private SQLiteDatabase db;

    public DatabaseHandler(Context context){
        super(context, NAME, null, VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
                db.execSQL(CREATE_TODO_TABLE);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS " + TODO_TABLE);
        onCreate(db);
    }

    public void openDatabase(){
        db = this.getWritableDatabase();
    }

    public void insertPlan(TodoModel plan){
        ContentValues contentValues = new ContentValues();
        contentValues.put(PLAN, plan.getPlan());
        contentValues.put(STATUS, 0);
        db.insert(TODO_TABLE, null, contentValues);
    }

    public List<TodoModel> getAllPlans(){
        List<TodoModel> planList = new ArrayList<>();
        Cursor cursor = null;
        db.beginTransaction();
        try {
            cursor = db.query(TODO_TABLE, null, null, null, null, null, null, null);
            if (cursor != null){
                if (cursor.moveToFirst()){
                    do {
                        TodoModel plan = new TodoModel();
                        plan.setId(cursor.getInt(cursor.getColumnIndex(ID)));
                        plan.setPlan(cursor.getString(cursor.getColumnIndex(PLAN)));
                        plan.setStatus(cursor.getInt(cursor.getColumnIndex(STATUS)));
                        planList.add(plan);
                    } while (cursor.moveToNext());
                }
            }
        } finally {
            db.endTransaction();
            cursor.close();
        }
        return planList;
    }

    public void updateStatus(int id, int status){
        ContentValues contentValues =  new ContentValues();
        contentValues.put(STATUS, status);
        db.update(TODO_TABLE, contentValues, ID + "= ?", new String[] {String.valueOf(id)});
    }

    public void updatePlan(int id, String plan){
        ContentValues contentValues =  new ContentValues();
        contentValues.put(PLAN, plan);
        db.update(TODO_TABLE, contentValues, ID + "= ?", new String[] {String.valueOf(id)});
    }

    public void deletePlan(int id){
        db.delete(TODO_TABLE, ID + "= ?", new String[] {String.valueOf(id)});

    }
}
