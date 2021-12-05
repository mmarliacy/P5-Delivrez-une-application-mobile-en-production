package com.cleanup.todoc;


import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import androidx.arch.core.executor.testing.InstantTaskExecutorRule;
import androidx.room.Room;
import androidx.test.platform.app.InstrumentationRegistry;
import androidx.test.runner.AndroidJUnit4;

import com.cleanup.todoc.Utils.LiveDataTestUtils;
import com.cleanup.todoc.database.DAO.ProjectDao;
import com.cleanup.todoc.database.DAO.TaskDao;
import com.cleanup.todoc.database.ToDocDatabase;
import com.cleanup.todoc.model.Project;
import com.cleanup.todoc.model.Task;

import org.junit.After;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

import java.util.List;
import java.util.Objects;

@RunWith(AndroidJUnit4.class)
public class DaoTest {

    //-- DATA -->
    private ToDocDatabase dataBase;
    private TaskDao mTaskDao;
    private ProjectDao mProjectDao;
    List<Project> projects;
    List<Task> tasks;
    private static final long PROJECT_ID = 1;
    private static final Project PROJECT_DEMO = new Project(PROJECT_ID, "test", 0xFFEADAD1);
    private static final Task TASK_DEMO = new Task(PROJECT_ID, "task_demo", 1593545213);


    //-------------------
    //-- INITIATING... --
    //-------------------

    //-- Initialize JUnit Test Rule to execute task synchronously --
    @Rule
    public InstantTaskExecutorRule instantTaskExecutorRule = new InstantTaskExecutorRule();

    //-- Init database --
    @Before
    public void initDb() {
        dataBase = Room.inMemoryDatabaseBuilder(InstrumentationRegistry.getInstrumentation().getContext(),
                ToDocDatabase.class).allowMainThreadQueries().build();
        mTaskDao = dataBase.taskDao();
        mProjectDao = dataBase.projectDao();
    }
    //-- Close database --
    @After
    public void closeDb() {
        dataBase.close();
    }

    //--------------------
    //-- ABOUT PROJECTS --
    //--------------------

    //-- Test : insert project into project table and check with assert function --
    @Test
    public void insertProject(){
        mProjectDao.insertProject(PROJECT_DEMO);
        projects = LiveDataTestUtils.getValue(this.dataBase.projectDao().getAllProjects());
        assertEquals(1, projects.size());
        assertEquals("test",projects.get(0).getName());
    }
    //-- Test : insert project into project table --
    @Test
    public void deleteProjectDeleteAssociatedTasks(){
        //-- create task and verify if task and project are really assigned --
        mProjectDao.insertProject(PROJECT_DEMO);
        mTaskDao.createTask(TASK_DEMO);
        tasks = LiveDataTestUtils.getValue(mTaskDao.getAllTasks());
        assertEquals("task_demo",tasks.get(0).getName());
        assertEquals(0xFFEADAD1, Objects.requireNonNull(tasks.get(0).getProject()).getColor());
        //-- get "PROJECT_DEMO" value and delete it --
        Project projectDemo = LiveDataTestUtils.getValue(mProjectDao.getAllProjects()).get(0);
        mProjectDao.deleteProject(projectDemo);
        //-- Check if the associated task has been deleted after project deletion --
        tasks = LiveDataTestUtils.getValue(mTaskDao.getAllTasks());
        assertEquals(0, tasks.size());
    }

    //------------------
    //-- ABOUT TASKS--
    //------------------

    //-- Test : get Task List even if the database is empty --
    @Test
    public void getTasksWhenDatabaseIsEmpty(){
        tasks = LiveDataTestUtils.getValue(mTaskDao.getAllTasks());
        assertTrue(tasks.isEmpty());
    }

    //-- Test : insert task into the task table and check with assert function --
    @Test
    public void createTask(){
        mProjectDao.insertProject(PROJECT_DEMO);
        mTaskDao.createTask(TASK_DEMO);
        Task taskDemo = LiveDataTestUtils.getValue(mTaskDao.getAllTasks()).get(0);
        assertEquals(TASK_DEMO.getName(), taskDemo.getName());
    }

    //-- Test : delete task from the task table and check with assert function --
    @Test
    public void deleteTask(){
        mProjectDao.insertProject(PROJECT_DEMO);
        mTaskDao.createTask(TASK_DEMO);
        Task taskDemo = LiveDataTestUtils.getValue(mTaskDao.getAllTasks()).get(0);
        mTaskDao.deleteTask(taskDemo);
        tasks = LiveDataTestUtils.getValue(mTaskDao.getAllTasks());
        assertEquals(0, tasks.size());
    }
}
