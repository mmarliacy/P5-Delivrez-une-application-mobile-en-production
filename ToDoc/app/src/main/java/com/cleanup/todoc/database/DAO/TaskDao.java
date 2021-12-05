package com.cleanup.todoc.database.DAO;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;

import com.cleanup.todoc.model.Task;

import java.util.List;

@Dao
public interface TaskDao {

    /**
     * Get Tasks List -->
     */
    @Query("SELECT * FROM task")
    LiveData<List<Task>> getAllTasks();

    /**
     * Insert one task into Task table -->
     */
    @Insert
    void createTask(Task task);

    /**
     * Delete one task from Task table according to its id -->
     */
    @Delete
    void deleteTask(Task task);
}
