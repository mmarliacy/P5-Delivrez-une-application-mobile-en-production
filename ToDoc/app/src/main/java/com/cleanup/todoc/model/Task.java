package com.cleanup.todoc.model;


import androidx.annotation.NonNull;
import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.ForeignKey;
import androidx.room.PrimaryKey;

import java.util.Comparator;

/**
 * <p>Model for the tasks of the application.</p>
 *
 * @author Gaëtan HERFRAY
 */
@Entity(tableName = "task", foreignKeys = @ForeignKey
                            (entity = Project.class,
                            parentColumns = "id",
                            childColumns = "projectId",
                            onDelete = ForeignKey.CASCADE)
                            )
public class Task {

    /**
     * The unique identifier of the task
     */
    @PrimaryKey (autoGenerate = true)
    private long id;

    /**
     * The unique identifier of the project associated to the task
     */
    @ColumnInfo(name = "projectId", index = true)
    private long projectId;

    /**
     * The name of the task
     */
    public String name;

    /**
     * The timestamp when the task has been created
     */
    public long creationTimestamp;

    /**
     * Instantiates a new Task.
     * @param projectId         the unique identifier of the project associated to the task to set
     * @param name              the name of the task to set
     * @param creationTimestamp the timestamp when the task has been created to set
     */
    public Task(long projectId, @NonNull String name, long creationTimestamp) {
        this.setProjectId(projectId);
        this.setName(name);
        this.setCreationTimestamp(creationTimestamp);
    }

    /**
     * Returns the unique identifier of the task.
     */
    public long getId() {
        return id;
    }

    /**
     * Sets the unique identifier of the task.
     */
    public void setId(long id) {
        this.id = id;
    }

    /**
     * Set the unique identifier of the project associated to the task.
     */
    public void setProjectId(long projectId) {
        this.projectId = projectId;
    }

    /**
     * Get the unique identifier of the project associated to the task.
     */
    public long getProjectId() {
        return projectId;
    }

    /**
     * Returns the project associated to the task.
     */
    public Project getProject() {
        return Project.getProjectById(projectId);
    }

    /**
     * Returns the name of the task.
     */
    public String getName() {
        return name;
    }

    /**
     * Sets the name of the task.
     */
    public void setName(@NonNull String name) {
        this.name = name;
    }

    /**
     * Sets the timestamp when the task has been created.
     */
    public void setCreationTimestamp(long creationTimestamp) {
        this.creationTimestamp = creationTimestamp;
    }

    /**
     * Comparator to sort task from A to Z
     */
    public static class TaskAZComparator implements Comparator<Task> {
        @Override
        public int compare(Task left, Task right) {
            return left.name.compareTo(right.name);
        }
    }

    /**
     * Comparator to sort task from Z to A
     */
    public static class TaskZAComparator implements Comparator<Task> {
        @Override
        public int compare(Task left, Task right) {
            return right.name.compareTo(left.name);
        }
    }

    /**
     * Comparator to sort task from last created to first created
     */
    public static class TaskRecentComparator implements Comparator<Task> {
        @Override
        public int compare(Task left, Task right) {
            return (int) (right.creationTimestamp - left.creationTimestamp);
        }
    }

    /**
     * Comparator to sort task from first created to last created
     */
    public static class TaskOldComparator implements Comparator<Task> {
        @Override
        public int compare(Task left, Task right) {
            return (int) (left.creationTimestamp - right.creationTimestamp);
        }
    }
}
