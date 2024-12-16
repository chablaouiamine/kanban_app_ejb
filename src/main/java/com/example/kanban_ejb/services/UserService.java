package com.example.kanban_ejb.services;

import com.example.kanban_ejb.entities.User;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

@Stateless
public class UserService {

    @PersistenceContext
    private EntityManager em;

    /**
     * Enregistre un nouvel utilisateur.
     *
     * @param user L'utilisateur à enregistrer.
     * @return true si l'utilisateur a été enregistré avec succès, sinon false.
     */
    public boolean register(User user) {
        try {
            // Vérifier si le nom d'utilisateur existe déjà
            Long count = em.createQuery("SELECT COUNT(u) FROM User u WHERE u.username = :username", Long.class)
                    .setParameter("username", user.getUsername())
                    .getSingleResult();

            if (count > 0) {
                // Nom d'utilisateur déjà pris
                return false;
            }

            // Hacher le mot de passe avant de sauvegarder
            user.setPassword(hashPassword(user.getPassword()));

            // Enregistrer l'utilisateur
            em.persist(user);
            return true;

        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    /**
     * Hache un mot de passe à l'aide de BCrypt.
     *
     * @param plainPassword Le mot de passe en clair.
     * @return Le mot de passe haché.
     */
    private String hashPassword(String plainPassword) {
        return org.mindrot.jbcrypt.BCrypt.hashpw(plainPassword, org.mindrot.jbcrypt.BCrypt.gensalt());
    }
}
