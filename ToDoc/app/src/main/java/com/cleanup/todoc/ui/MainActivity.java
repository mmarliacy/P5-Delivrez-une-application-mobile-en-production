package com.cleanup.todoc.ui;

import android.content.DialogInterface;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProviders;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.cleanup.todoc.R;
import com.cleanup.todoc.injections.Injection;
import com.cleanup.todoc.injections.ViewModelFactory;
import com.cleanup.todoc.model.Project;
import com.cleanup.todoc.model.Task;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.List;

/**
 * <p>Home activity of the application which is displayed when the user opens the app.</p>
 * <p>Displays the list of tasks.</p>
 */
public class MainActivity extends AppCompatActivity implements TasksAdapter.DeleteTaskListener {


    //-- DATA --\
    /**
     * List of all current tasks of the application
     */
    @NonNull
    private final List<Task> tasks = new ArrayList<>();

    /**
     * List of all projects available in the application
     */
    private final List<Project> allProjects= new ArrayList<>();

    /**
     * The adapter which handles the list of tasks
     */
    private final TasksAdapter adapter = new TasksAdapter(tasks, this);

    /**
     * The sort method to be used to display tasks
     */
    private SortMethod sortMethod = SortMethod.NONE;

    /**
     * Dialog to create a new task
     */
    public AlertDialog dialog = null;

    /**
     * EditText that allows user to set the name of a task
     */
    private EditText dialogEditText = null;

    /**
     * Spinner that allows the user to associate a project to a task
     */
    private Spinner dialogSpinner = null;

    /**
     * The RecyclerView which displays the list of tasks
     */
    private RecyclerView listTasks;

    /**
     * The TextView displaying the empty state
     */
    private TextView lblNoTasks;

    /**
     * Task ViewModel for sending Data between UI and Database
     */
    private TaskViewModel taskViewModel;

    //-- ON CREATE -->>
    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        this.configureViewModel();
        getProjects();
        getTasks();
        configureRecyclerView();
        findViewById(R.id.fab_add_task).setOnClickListener(view -> showAddTaskDialog());
    }

    //-- Configure and Init conditions of View Model --
    private void configureViewModel() {
        ViewModelFactory viewModelFactory = Injection.provideViewModelFactory(this);
        this.taskViewModel = ViewModelProviders.of(this, viewModelFactory).get(TaskViewModel.class);
        this.taskViewModel.init();
    }

    //-- Returns and observe all Projects --
    private void getProjects(){
        this.taskViewModel.getAllProjects().observe(this, this::getAllProjects);
    }
    public void getAllProjects(List<Project> projects) {
        allProjects.clear();
        allProjects.addAll(projects);
    }

    //-- Returns and observe all Tasks --
    private void getTasks() {
        this.taskViewModel.getTasks().observe(this, this::updateTasks);
    }

    /**
     * Updates the list of tasks in the UI
     */
    public void updateTasks(List<Task> tasks) {
        if (tasks.size() == 0) {
            lblNoTasks.setVisibility(View.VISIBLE);
            listTasks.setVisibility(View.GONE);
        } else {
            lblNoTasks.setVisibility(View.GONE);
            listTasks.setVisibility(View.VISIBLE);
            switch (sortMethod) {
                case ALPHABETICAL:
                    Collections.sort(tasks, new Task.TaskAZComparator());
                    break;
                case ALPHABETICAL_INVERTED:
                    Collections.sort(tasks, new Task.TaskZAComparator());
                    break;
                case RECENT_FIRST:
                    Collections.sort(tasks, new Task.TaskRecentComparator());
                    break;
                case OLD_FIRST:
                    Collections.sort(tasks, new Task.TaskOldComparator());
                    break;
            }
            adapter.updateTasks(tasks);
        }
    }

    /**
     * Adds the given task to the list of created tasks.
     */
    private void addTask(@NonNull Task task) {
        this.taskViewModel.createTask(task);
    }

    /**
     * Deletes the given task from the list of created tasks.
     */
    @Override
    public void onDeleteTask(Task task) {
        this.taskViewModel.deleteTask(task);
    }

    //-- Configure RecyclerView --
    public void configureRecyclerView() {
        listTasks = findViewById(R.id.list_tasks);
        lblNoTasks = findViewById(R.id.lbl_no_task);
        listTasks.setAdapter(adapter);
        listTasks.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false));
    }

    /**
     * Shows the Dialog for adding a Task
     */
    private void showAddTaskDialog() {
        final AlertDialog dialog = getAddTaskDialog();

        dialog.show();

        dialogEditText = dialog.findViewById(R.id.txt_task_name);
        dialogSpinner = dialog.findViewById(R.id.project_spinner);

        populateDialogSpinner();
    }

    /**
     * Sets the data of the Spinner with projects to associate to a new task
     */
    private void populateDialogSpinner() {
        final ArrayAdapter<Project> adapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_item, allProjects);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        if (dialogSpinner != null) {
            dialogSpinner.setAdapter(adapter);
        }
    }

    /**
     * Returns the dialog allowing the user to create a new task.
     */
    @NonNull
    private AlertDialog getAddTaskDialog() {
        final AlertDialog.Builder alertBuilder = new AlertDialog.Builder(this, R.style.Dialog);

        alertBuilder.setTitle(R.string.add_task);
        alertBuilder.setView(R.layout.dialog_add_task);
        alertBuilder.setPositiveButton(R.string.add, null);
        alertBuilder.setOnDismissListener(dialogInterface -> {
            dialogEditText = null;
            dialogSpinner = null;
            dialog = null;
        });

        dialog = alertBuilder.create();

        // This instead of listener to positive button in order to avoid automatic dismiss
        dialog.setOnShowListener(dialogInterface -> {

            Button button = dialog.getButton(AlertDialog.BUTTON_POSITIVE);
            button.setOnClickListener(view -> onPositiveButtonClick(dialog));
        });
        return dialog;
    }

    /**
     * Called when the user clicks on the positive button of the Create Task Dialog.
     */
    private void onPositiveButtonClick(DialogInterface dialogInterface) {
        // If dialog is open
        if (dialogEditText != null && dialogSpinner != null) {
            //-- Get the name of the task --
            String taskName = dialogEditText.getText().toString();

            //-- Get the selected project to be associated to the task --
            Project taskProject = null;
            if (dialogSpinner.getSelectedItem() instanceof Project) {
                taskProject = (Project) dialogSpinner.getSelectedItem();
            }

            //-- If a name has not been set --
            if (taskName.trim().isEmpty()) {
                dialogEditText.setError(getString(R.string.empty_task_name));
            }
            //-- If both project and name of the task have been set --
            else if (taskProject != null) {
                Task task = new Task(
                        taskProject.getId(),
                        taskName,
                        new Date().getTime()
                );

                addTask(task);

                dialogInterface.dismiss();
            }
            //-- If name has been set, but project has not been set (this should never occur) --
            else{
                dialogInterface.dismiss();
            }
        }
        //-- If dialog is already closed --
        else {
            dialogInterface.dismiss();
        }
    }

    //-- Create Menu --
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.actions, menu);
        return true;
    }
    //-- According to position in menu, one of sort method is applied --
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();

        if (id == R.id.filter_alphabetical) {
            sortMethod = SortMethod.ALPHABETICAL;
        } else if (id == R.id.filter_alphabetical_inverted) {
            sortMethod = SortMethod.ALPHABETICAL_INVERTED;
        } else if (id == R.id.filter_oldest_first) {
            sortMethod = SortMethod.OLD_FIRST;
        } else if (id == R.id.filter_recent_first) {
            sortMethod = SortMethod.RECENT_FIRST;
        }
        getTasks();
        return super.onOptionsItemSelected(item);
    }

    /**
     * List of all possible sort methods for task
     */
    private enum SortMethod {
        /**
         * Sort alphabetical by name
         */
        ALPHABETICAL,
        /**
         * Inverted sort alphabetical by name
         */
        ALPHABETICAL_INVERTED,
        /**
         * Lastly created first
         */
        RECENT_FIRST,
        /**
         * First created first
         */
        OLD_FIRST,
        /**
         * No sort
         */
        NONE
    }
}
