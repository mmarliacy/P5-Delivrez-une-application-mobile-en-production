package com.cleanup.todoc.database.DAO;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;

import com.cleanup.todoc.model.Project;

import java.util.List;

@Dao
public interface ProjectDao {

    /**
     * Get Projects list -->
     */
    @Query("SELECT * FROM project")
    LiveData<List<Project>> getAllProjects();

    /**
     * Insert one project into Project table -->
     */
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void insertProject(Project project);

    /**
     * Delete one project from Project table -->
     */
    @Delete
    void deleteProject(Project project);
}
