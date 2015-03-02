package com.acadgild.todo;


import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import java.util.ArrayList;
import java.util.List;

public class TaskDataBase extends SQLiteOpenHelper {

    private static final int DATABASE_VERSION = 1;
    private static final String DATABASE_NAME = "TasksManager";
    private static final String TABLE_TASKS = "tasks";

    private static final String KEY_ID = "id";
    private static final String KEY_DATE = "date";
    private static final String KEY_TITLE = "title";
    private static final String KEY_DESCRIPTION = "description";
    private static final String KEY_IMAGE = "image";

    public TaskDataBase(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        String CREATE_TASKS_TABLE = "CREATE TABLE " + TABLE_TASKS + "(" + KEY_ID + " INTEGER PRIMARY KEY,"
                + KEY_DATE + " TEXT," + KEY_TITLE + " TEXT,"
                + KEY_DESCRIPTION + " TEXT," + KEY_IMAGE + " INTEGER" + ")";
        db.execSQL(CREATE_TASKS_TABLE);
    }

    public void addTask(TaskInfo task) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(KEY_DATE, task.getDate());
        values.put(KEY_TITLE, task.getTitle());
        values.put(KEY_DESCRIPTION, task.getDescription());
        values.put(KEY_IMAGE, task.getImage());
        db.insert(TABLE_TASKS, null, values);
        db.close();
    }

    TaskInfo getTask(int id) {
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.query(TABLE_TASKS, new String[] {KEY_DATE, KEY_TITLE, KEY_DESCRIPTION, KEY_IMAGE}, KEY_ID + "=?",
                new String[] {String.valueOf(id)}, null, null, null, null);
        if (cursor != null)
            cursor.moveToFirst();
            TaskInfo taskInfo = new TaskInfo(id, cursor.getString(0),
                    cursor.getString(1), cursor.getString(3), Integer.parseInt(cursor.getString(3)));

            return taskInfo;
    }

    public List<TaskInfo> getAllTasks() {
        List<TaskInfo> allTasks = new ArrayList<>();
        String selectQuery = "SELECT * FROM " + TABLE_TASKS;
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery(selectQuery, null);

        if (cursor.moveToFirst()) {
            do {
                TaskInfo taskInfo = new TaskInfo();
                taskInfo.setID(Integer.parseInt(cursor.getString(0)));
                taskInfo.setDate(cursor.getString(1));
                taskInfo.setTitle(cursor.getString(2));
                taskInfo.setDescription(cursor.getString(3));
                taskInfo.setImage(Integer.parseInt(cursor.getString(4)));
                allTasks.add(taskInfo);
            } while (cursor.moveToNext());
        }
        return allTasks;
    }

    public int updateTask(TaskInfo task) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(KEY_DATE, task.getDate());
        values.put(KEY_TITLE, task.getTitle());
        values.put(KEY_DESCRIPTION, task.getDescription());
        values.put(KEY_IMAGE, task.getImage());

        return db.update(TABLE_TASKS, values, KEY_ID + " = ?",
                new String[] {String.valueOf(task.getID()) });
    }

/*
    public void deleteTask(TaskInfo task) {
        SQLiteDatabase db = this.getWritableDatabase();
        db.delete(TABLE_TASKS, KEY_ID + " = ?",
                new String[] { String.valueOf(task.getID())});
        db.close();
    }
*/

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

        db.execSQL("DROP TABLE IF EXISTS " + TABLE_TASKS);
        onCreate(db);
    }
}