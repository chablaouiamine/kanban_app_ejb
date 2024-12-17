package com.example.kanban_ejb.services;



import com.example.kanban_ejb.entities.Task;

import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import java.util.List;

@Stateless
public class TaskService {

    @PersistenceContext
    private EntityManager em;

//    public void createTask(Task task) {
//        em.persist(task);
//    }
    public void createTask(Task task) {
        if (task.getAssignedUser() != null) {
            em.persist(task);
        } else {
            System.out.println("Task cannot be saved: assignedUser is null!");
        }
    }

    public void updateTask(Task task) {
        em.merge(task);
    }

    public List<Task> getAllTasks() {
        return em.createQuery("SELECT t FROM Task t", Task.class).getResultList();
    }
}
