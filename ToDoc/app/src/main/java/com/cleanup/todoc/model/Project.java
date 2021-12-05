package com.cleanup.todoc.model;


import androidx.annotation.ColorInt;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

/**
 * <p>Models for project in which tasks are included.</p>
 */

@Entity(tableName = "project")
public class Project {

    /**
     * The unique identifier of the project
     */
    @PrimaryKey
    private final long id;

    /**
     * The name of the project
     */
    @NonNull
    private final String name;

    /**
     * The hex (ARGB) code of the color associated to the project
     */
    @ColorInt
    private final int color;

    /**
     * Instantiates a new Project.
     * @param id    the unique identifier of the project to set
     * @param name  the name of the project to set
     * @param color the hex (ARGB) code of the color associated to the project to set
     */
    public Project(long id, @NonNull String name, @ColorInt int color) {
        this.id = id;
        this.name = name;
        this.color = color;
    }

    /**
     * @return all the projects of the application
     */
    @NonNull
    public static Project[] getAllProjects() {
        return new Project[]{
                new Project(1L, "Projet Tartampion", 0xFFEADAD1),
                new Project(2L, "Projet Lucidia", 0xFFB4CDBA),
                new Project(3L, "Projet Circus", 0xFFA3CED2),
        };
    }

    /**
     * Returns the project with the given unique identifier, or null if no project with that
     * identifier can be found.
     */
    @Nullable
    public static Project getProjectById(long id) {
        for (Project project : getAllProjects()) {
            if (project.id == id)
                return project;
        }
        return null;
    }

    /**
     * Returns the unique identifier of the project.
     */
    public long getId() {
        return id;
    }


    /**
     * Returns the name of the project.
     */
    @NonNull
    public String getName() {
        return name;
    }

    /**
     * Returns the hex (ARGB) code of the color associated to the project.
     */
    @ColorInt
    public int getColor() {
        return color;
    }

    /**
     * Returns the name of the project.
     */
    @Override
    @NonNull
    public String toString() {
        return getName();
    }
}
