package com.cleanup.todoc;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotEquals;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertSame;

import com.cleanup.todoc.model.Task;

import org.junit.Test;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;

/**
 * Unit tests for tasks
 *
 * @author Gaëtan HERFRAY
 */
public class TaskUnitTest {

    //--------------------
    //-- ABOUT PROJECTS --
    //--------------------

    //-- Test : Create 4 tasks and verify if project has been associated correctly to task --
    @Test
    public void test_projects() {
        final Task task1 = new Task( 1, "task 1", new Date().getTime());
        final Task task2 = new Task( 2, "task 2", new Date().getTime());
        final Task task3 = new Task( 3, "task 3", new Date().getTime());
        final Task task4 = new Task( 4, "task 4", new Date().getTime());

        assertEquals("Projet Tartampion", task1.getProject().getName());
        assertEquals("Projet Lucidia", task2.getProject().getName());
        assertEquals("Projet Circus", task3.getProject().getName());
        assertNull(task4.getProject());
    }

    //--------------------
    //--   ABOUT TASKS  --
    //--------------------

    //-- Test : Create 3 tasks and verify if task has been correctly created --
    @Test
    public void createTasks() {
        final Task task1 = new Task( 1, "task 1", new Date().getTime());
        final Task task2 = new Task( 2, "task 2", new Date().getTime());
        final Task task3 = new Task( 3, "task 3", new Date().getTime());

        assertEquals("task 1", task1.getName());
        assertEquals("task 2", task2.getName());
        assertNotEquals("task 4", task3.getName());
    }

    //-- Test : Create 3 tasks and verify if tasks have been correctly sort from A to Z --
    @Test
    public void test_az_comparator() {
        final Task task1 = new Task(1,  "aaa", 123);
        final Task task2 = new Task( 2, "zzz", 124);
        final Task task3 = new Task(  3, "hhh", 125);

        final ArrayList<Task> tasks = new ArrayList<>();
        tasks.add(task1);
        tasks.add(task2);
        tasks.add(task3);
        Collections.sort(tasks, new Task.TaskAZComparator());

        assertSame(tasks.get(0), task1);
        assertSame(tasks.get(1), task3);
        assertSame(tasks.get(2), task2);
    }

    //-- Test : Create 3 tasks and verify if tasks have been correctly sort from Z to A --
    @Test
    public void test_za_comparator() {
        final Task task1 = new Task( 1,"aaa", 123);
        final Task task2 = new Task(  2,"zzz", 124);
        final Task task3 = new Task(   3,"hhh", 125);

        final ArrayList<Task> tasks = new ArrayList<>();
        tasks.add(task1);
        tasks.add(task2);
        tasks.add(task3);
        Collections.sort(tasks, new Task.TaskZAComparator());

        assertSame(tasks.get(0), task2);
        assertSame(tasks.get(1), task3);
        assertSame(tasks.get(2), task1);
    }

    //-- Test : Create 3 tasks and verify if tasks have been sort from last to first element  --
    @Test
    public void test_recent_comparator() {
        final Task task1 = new Task( 1,  "aaa", 123);
        final Task task2 = new Task(  2, "zzz", 124);
        final Task task3 = new Task(   3, "hhh", 125);

        final ArrayList<Task> tasks = new ArrayList<>();
        tasks.add(task1);
        tasks.add(task2);
        tasks.add(task3);
        Collections.sort(tasks, new Task.TaskRecentComparator());

        assertSame(tasks.get(0), task3);
        assertSame(tasks.get(1), task2);
        assertSame(tasks.get(2), task1);
    }

    //-- Test : Create 3 tasks and verify if tasks have been sort from first to last element  --
    @Test
    public void test_old_comparator() {
        final Task task1 = new Task( 1,  "aaa", 123);
        final Task task2 = new Task(  2, "zzz", 124);
        final Task task3 = new Task(   3, "hhh", 125);

        final ArrayList<Task> tasks = new ArrayList<>();
        tasks.add(task1);
        tasks.add(task2);
        tasks.add(task3);
        Collections.sort(tasks, new Task.TaskOldComparator());

        assertSame(tasks.get(0), task1);
        assertSame(tasks.get(1), task2);
        assertSame(tasks.get(2), task3);
    }
}