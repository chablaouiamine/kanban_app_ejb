package com.example.kanban_ejb.beans;

import com.example.kanban_ejb.entities.User;
import com.example.kanban_ejb.services.UserService;

import javax.ejb.EJB;
import javax.faces.application.FacesMessage;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.SessionScoped;
import javax.faces.context.FacesContext;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.TypedQuery;

import org.mindrot.jbcrypt.BCrypt;

@ManagedBean(name = "authBean")
@SessionScoped
public class AuthBean {
    private String username;
    private String password;
    private String email;

    @EJB
    private UserService userService;

    @PersistenceContext
    private EntityManager em;

    // Login method with database validation
//    public String login() {
//        try {
//            // Query to find user by username
//            TypedQuery<User> query = em.createQuery(
//                    "SELECT u FROM User u WHERE u.username = :username", User.class);
//            query.setParameter("username", username);
//
//            // Try to get the user
//            User user = query.getSingleResult();
//
//            // Verify password
//            if (user != null && BCrypt.checkpw(password, user.getPassword())) {
//                // Successful login
//                FacesContext.getCurrentInstance().addMessage(null,
//                        new FacesMessage(FacesMessage.SEVERITY_INFO,
//                                "Login Successful", "Welcome " + username));
//                return "kanban.xhtml?faces-redirect=true";
//            } else {
//                // Invalid credentials
//                FacesContext.getCurrentInstance().addMessage(null,
//                        new FacesMessage(FacesMessage.SEVERITY_ERROR,
//                                "Login Failed", "Invalid username or password"));
//                return null;
//            }
//        } catch (Exception e) {
//            // User not found
//            FacesContext.getCurrentInstance().addMessage(null,
//                    new FacesMessage(FacesMessage.SEVERITY_ERROR,
//                            "Login Failed", "User not found"));
//            return null;
//        }
//    }
    public String login() {
        try {
            // Query to find user by username
            TypedQuery<User> query = em.createQuery(
                    "SELECT u FROM User u WHERE u.username = :username", User.class);
            query.setParameter("username", username);

            User user = query.getSingleResult();

            // Verify password
            if (user != null && BCrypt.checkpw(password, user.getPassword())) {
                // Store user in session
                FacesContext.getCurrentInstance().getExternalContext()
                        .getSessionMap().put("loggedInUser", user);

                FacesContext.getCurrentInstance().addMessage(null,
                        new FacesMessage(FacesMessage.SEVERITY_INFO,
                                "Login Successful", "Welcome " + username));
                return "kanban.xhtml?faces-redirect=true";
            } else {
                FacesContext.getCurrentInstance().addMessage(null,
                        new FacesMessage(FacesMessage.SEVERITY_ERROR,
                                "Login Failed", "Invalid username or password"));
                return null;
            }
        } catch (Exception e) {
            FacesContext.getCurrentInstance().addMessage(null,
                    new FacesMessage(FacesMessage.SEVERITY_ERROR,
                            "Login Failed", "User not found"));
            return null;
        }
    }


    // Register method integrating with UserService
    public String register() {
        // Create a new User entity
        User newUser = new User();
        newUser.setUsername(username);
        newUser.setPassword(password);
        newUser.setEmail(email);

        // Use UserService to register
        boolean registrationSuccess = userService.register(newUser);

        if (registrationSuccess) {
            FacesContext.getCurrentInstance().addMessage(null,
                    new FacesMessage(FacesMessage.SEVERITY_INFO,
                            "Registration Successful", "You can now log in"));
            return "login.xhtml?faces-redirect=true";
        } else {
            FacesContext.getCurrentInstance().addMessage(null,
                    new FacesMessage(FacesMessage.SEVERITY_ERROR,
                            "Registration Failed", "Username may already exist"));
            return null;
        }
    }

    // Getters and Setters
    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }
}