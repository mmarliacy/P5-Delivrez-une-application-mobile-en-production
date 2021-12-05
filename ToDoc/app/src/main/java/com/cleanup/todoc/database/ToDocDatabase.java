package com.cleanup.todoc.database;

import android.content.ContentValues;
import android.content.Context;

import androidx.annotation.NonNull;
import androidx.room.Database;
import androidx.room.OnConflictStrategy;
import androidx.room.Room;
import androidx.room.RoomDatabase;
import androidx.sqlite.db.SupportSQLiteDatabase;

import com.cleanup.todoc.database.DAO.ProjectDao;
import com.cleanup.todoc.database.DAO.TaskDao;
import com.cleanup.todoc.model.Project;
import com.cleanup.todoc.model.Task;

@Database(entities = {Task.class, Project.class}, version = 1, exportSchema = false)
public abstract class ToDocDatabase extends RoomDatabase {

    //-- SINGLETON -->
    private static volatile ToDocDatabase INSTANCE;

    //-- DAO -->
    public abstract ProjectDao projectDao();
    public abstract TaskDao taskDao();

    //-- INSTANCE -->
    public static ToDocDatabase getInstance(Context context) {
        if (INSTANCE == null) {
            synchronized (ToDocDatabase.class) {
                if (INSTANCE == null) {
                    INSTANCE = Room.databaseBuilder(context.getApplicationContext(),
                            ToDocDatabase.class, "ToDocDatabase.db").
                            addCallback(prepopulateDatabase()).
                            build();
                }
            }
        }
        return INSTANCE;
    }

    //-- When we launch the app for the first time it prepopulate our database --
    private static Callback prepopulateDatabase(){
        return new Callback() {

            @Override
            public void onCreate(@NonNull SupportSQLiteDatabase db) {
                super.onCreate(db);
                getEachProjects(db);
            }
        };
    }

    //-- Method that get back all projects and make them enter to Project table --
    private static void getEachProjects(SupportSQLiteDatabase db) {
        Project[] projects = Project.getAllProjects();
        for (Project project : projects) {
            ContentValues values = new ContentValues();
            values.put("id", project.getId());
            values.put("name", project.getName());
            values.put("color", project.getColor());
            db.insert("project", OnConflictStrategy.IGNORE, values);
        }
    }
}
