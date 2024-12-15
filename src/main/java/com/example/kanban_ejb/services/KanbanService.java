package com.example.kanban_ejb.services;

import com.example.kanban_ejb.entities.Task;

import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import java.util.List;

@Stateless
public class KanbanService {
    @PersistenceContext
    private EntityManager em;

    public List<Task> getTasksByStatus(String status) {
        return em.createQuery("SELECT t FROM Task t WHERE t.status = :status", Task.class)
                .setParameter("status", status)
                .getResultList();
    }

    public void createTask(Task task) {
        em.persist(task);
    }

    public void updateTask(Task task) {
        em.merge(task);
    }
}

