package com.cleanup.todoc.repositories;

import androidx.lifecycle.LiveData;

import com.cleanup.todoc.database.DAO.ProjectDao;
import com.cleanup.todoc.model.Project;

import java.util.List;

public class ProjectDataRepository {

    //-- Use to retrieve the SQL queries that will act on the project table -->
    private final ProjectDao projectDao;

    //-- Project Data Repository's Constructor -->
    public ProjectDataRepository(ProjectDao projectDao) {
        this.projectDao = projectDao;
    }

    //-- Of the type LiveData, returns all the projects in a persistent way --
    public LiveData<List<Project>> getAllProjects() {
        return this.projectDao.getAllProjects();
    }
}
