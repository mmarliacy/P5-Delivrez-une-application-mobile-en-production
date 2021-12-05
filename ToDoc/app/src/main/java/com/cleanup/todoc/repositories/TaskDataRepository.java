package com.cleanup.todoc.repositories;

import androidx.lifecycle.LiveData;

import com.cleanup.todoc.database.DAO.TaskDao;
import com.cleanup.todoc.model.Task;

import java.util.List;

public class TaskDataRepository {

    //-- Use to retrieve the SQL queries that will act on the task table -->
    private final TaskDao taskDao;

    //-- Task Data Repository's Constructor -->
    public TaskDataRepository(TaskDao taskDao) {
        this.taskDao = taskDao;
    }

    //-- Of the type LiveData, returns all tasks in a persistent way --
    public LiveData<List<Task>> getTasks() {
        return this.taskDao.getAllTasks();
    }

    //-- Create task in task table, in a persistent way --
    public void createTask(Task task) {
        this.taskDao.createTask(task);
    }

    //-- Delete Task from task table, in a persistent way --
    public void deleteTask(Task task) {
        taskDao.deleteTask(task);
    }
}
