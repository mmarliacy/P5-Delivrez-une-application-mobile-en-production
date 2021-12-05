package com.cleanup.todoc.ui;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.ViewModel;

import com.cleanup.todoc.model.Project;
import com.cleanup.todoc.model.Task;
import com.cleanup.todoc.repositories.ProjectDataRepository;
import com.cleanup.todoc.repositories.TaskDataRepository;

import java.util.List;
import java.util.concurrent.Executor;

public class TaskViewModel extends ViewModel {

    //-- REPOSITORIES & EXECUTOR -->
    private final TaskDataRepository taskDataRepository;
    private final ProjectDataRepository projectDataRepository;
    private final Executor executor;

    //-- DATA -->
    private LiveData<List<Project>> allProjects;

    //-- Task View Model's Constructor -->
    public TaskViewModel(TaskDataRepository taskDataRepository, ProjectDataRepository projectDataRepository
                        , Executor executor){
        this.taskDataRepository = taskDataRepository;
        this.projectDataRepository = projectDataRepository;
        this.executor = executor;
    }

    //-- Init projects --
    public void init() {
        if ( this.allProjects != null) {
            return;
        }
        allProjects = projectDataRepository.getAllProjects();
    }

    //-----------------
    //-- For Project --
    //-----------------

    //-- RETURNS ALL PROJECTS --
    public LiveData<List<Project>> getAllProjects(){
        return this.allProjects;
    }

    //-----------------
    //-- For Task --
    //-----------------

    //-- RETURNS ALL TASKS --
    public LiveData<List<Task>> getTasks(){
        return this.taskDataRepository.getTasks();
    }

    //-- Execute the creation of a task --
    public void createTask(Task task) {
        executor.execute(() -> taskDataRepository.createTask(task));
    }

    //-- Execute the deletion of a task --
    public void deleteTask(Task task) {
        executor.execute(() -> taskDataRepository.deleteTask(task));
    }
}
