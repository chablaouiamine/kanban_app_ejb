package com.example.kanban_ejb.dao;


import com.example.kanban_ejb.entities.Task;

import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import java.util.List;

@Stateless
public class TaskDAO {

    @PersistenceContext
    private EntityManager entityManager;

    public void createTask(Task task) {
        entityManager.persist(task);
    }

    public Task findTaskById(Long id) {
        return entityManager.find(Task.class, id);
    }

    public List<Task> findAllTasks() {
        return entityManager.createQuery("SELECT t FROM Task t", Task.class).getResultList();
    }

    public void updateTask(Task task) {
        entityManager.merge(task);
    }

    public void deleteTask(Long id) {
        Task task = findTaskById(id);
        if (task != null) {
            entityManager.remove(task);
        }
    }
}

