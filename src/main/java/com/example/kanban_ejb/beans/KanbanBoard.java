package com.example.kanban_ejb.beans;



import com.example.kanban_ejb.entities.Task;
import com.example.kanban_ejb.entities.User;
import com.example.kanban_ejb.services.TaskService;
import com.example.kanban_ejb.services.UserService;

import javax.annotation.PostConstruct;
import javax.ejb.EJB;
import javax.enterprise.context.SessionScoped;
import javax.inject.Named;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

@Named
@SessionScoped
public class KanbanBoard implements Serializable {

    @EJB
    private TaskService taskService;

    @EJB
    private UserService userService; // Inject UserService to get the logged-in user.

    private User loggedInUser; // Store the currently logged-in user.
    @PersistenceContext
    private EntityManager em;

    private String newTaskTitle;
    private List<Task> todo;
    private List<Task> inProgress;
    private List<Task> done;

    @PostConstruct
    public void init() {
        System.out.println("/////////////////////////////// init called");
        // Assume you have some way to retrieve the logged-in user (e.g., from the session or security context).
        loggedInUser = userService.getLoggedInUser(); // Fetch the logged-in user.
        System.out.println("/////////////////////////////// getLoggedInUser called");

        refreshTasks();
    }
//    @PostConstruct
//    public void init() {
//        loggedInUser = userService.getLoggedInUser();
//
//        // TEMPORARY: Assign a default user for testing
//        if (loggedInUser == null) {
//            System.out.println("No user found. Assigning default test user...");
//            loggedInUser = em.createQuery("SELECT u FROM User u WHERE u.username = 'amine123'", User.class)
//                    .getSingleResult();
//        }
//
//        refreshTasks();
//    }


    public void refreshTasks() {
        List<Task> tasks = taskService.getAllTasks();
        todo = new ArrayList<>();
        inProgress = new ArrayList<>();
        done = new ArrayList<>();

        for (Task task : tasks) {
            switch (task.getStatus()) {
                case "TODO":
                    todo.add(task);
                    break;
                case "IN_PROGRESS":
                    inProgress.add(task);
                    break;
                case "DONE":
                    done.add(task);
                    break;
            }
        }
    }

    public void addToTodo() {
        if (newTaskTitle != null && !newTaskTitle.trim().isEmpty()) {
            Task task = new Task();
            task.setTitle(newTaskTitle);
            task.setStatus("TODO");
            task.setAssignedUser(loggedInUser); // Assign the logged-in user.
            taskService.createTask(task);
            newTaskTitle = null;
            refreshTasks();
        }
    }

    public void moveTaskToInProgress(Task task) {
        task.setStatus("IN_PROGRESS");
        taskService.updateTask(task);
        refreshTasks();
    }

    public void moveTaskToDone(Task task) {
        task.setStatus("DONE");
        taskService.updateTask(task);
        refreshTasks();
    }

    // Getters and Setters
    public String getNewTaskTitle() {
        return newTaskTitle;
    }

    public void setNewTaskTitle(String newTaskTitle) {
        this.newTaskTitle = newTaskTitle;
    }

    public List<Task> getTodo() {
        return todo;
    }

    public List<Task> getInProgress() {
        return inProgress;
    }

    public List<Task> getDone() {
        return done;
    }

    public User getLoggedInUser() {
        return loggedInUser;
    }

    public void setLoggedInUser(User loggedInUser) {
        this.loggedInUser = loggedInUser;
    }
}
