package com.example.kanban_ejb.services;

import com.example.kanban_ejb.entities.User;
import org.mindrot.jbcrypt.BCrypt;

import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.NoResultException;
import javax.persistence.PersistenceContext;
import java.io.Serializable;
import java.util.List;

@Stateless
public class UserService implements Serializable {
    private static final long serialVersionUID = 1L;
    @PersistenceContext
    private EntityManager em;

    public User authenticate(String username, String password) {
        try {
            // Query to find user by username
            List<User> users = em.createQuery("SELECT u FROM User u WHERE u.username = :username", User.class)
                    .setParameter("username", username)
                    .getResultList();

            // If no user is found, return null
            if (users.isEmpty()) {
                return null;
            }

            // Assuming there's only one user with the given username (should be unique)
            User user = users.get(0);

            // Verify the password using bcrypt
            if (verifyPassword(password, user.getPassword())) {
                return user;
            } else {
                return null;
            }
        } catch (Exception e) {
            // Handle exception (log it or rethrow)
            return null;
        }
    }


    // Hash the password using bcrypt
    public String hashPassword(String password) {
        return BCrypt.hashpw(password, BCrypt.gensalt());
    }

    // Verify the password using bcrypt
    public boolean verifyPassword(String inputPassword, String storedHashedPassword) {
        return BCrypt.checkpw(inputPassword, storedHashedPassword);
    }


    public boolean register(User user) {
        try {
            // Check if username already exists
            Long count = em.createQuery("SELECT COUNT(u) FROM User u WHERE u.username = :username", Long.class)
                    .setParameter("username", user.getUsername())
                    .getSingleResult();

            if (count > 0) {
                return false; // Username already exists
            }

            // Persist the new user
            user.setPassword(hashPassword(user.getPassword())); // Hash password before saving
            em.persist(user);
            return true;
        } catch (Exception e) {
            return false;
        }
    }

//    private String hashPassword(String password) {
//        // Example hashing logic (use libraries like bcrypt in production)
//        return Integer.toHexString(password.hashCode());
//    }
}


