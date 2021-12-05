package com.cleanup.todoc.injections;

import android.content.Context;

import com.cleanup.todoc.database.ToDocDatabase;
import com.cleanup.todoc.repositories.ProjectDataRepository;
import com.cleanup.todoc.repositories.TaskDataRepository;

import java.util.concurrent.Executor;
import java.util.concurrent.Executors;

public class Injection {

    //----------------------------------
    //-- Inject Task Object for Needs --
    //----------------------------------
    public static TaskDataRepository provideTaskDataSource(Context context) {
        ToDocDatabase database = ToDocDatabase.getInstance(context);
        return new TaskDataRepository(database.taskDao());
    }
    //-------------------------------------
    //-- Inject Project Object for Needs --
    //-------------------------------------

    public static ProjectDataRepository provideProjectDataSource(Context context) {
        ToDocDatabase database = ToDocDatabase.getInstance(context);
        return new ProjectDataRepository(database.projectDao());
    }

    //-- Inject Executor Object to execute runnable tasks asynchronously  --
    public static Executor provideExecutor() {
        return Executors.newSingleThreadExecutor();
    }

    //-- Returns ViewModelFactory's objects --
    public static ViewModelFactory provideViewModelFactory(Context context) {
        TaskDataRepository taskDataSource = provideTaskDataSource(context);
        ProjectDataRepository projectDataSource = provideProjectDataSource(context);
        Executor executor = provideExecutor();
        return new ViewModelFactory(taskDataSource, projectDataSource, executor);
    }
}

