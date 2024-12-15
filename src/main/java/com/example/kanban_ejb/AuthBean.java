package com.example.kanban_ejb;


import com.example.kanban_ejb.entities.User;
import com.example.kanban_ejb.services.UserService;

import javax.enterprise.context.SessionScoped;
import javax.inject.Inject;
import javax.inject.Named;


import java.io.Serializable;

@Named
@SessionScoped
public class AuthBean implements Serializable {

    private static final long serialVersionUID = 1L; // Add a serialVersionUID for serialization

    private String username;
    private String password;
    private String email;
    private User loggedInUser;

    @Inject
    private UserService userService;

    // Getters and setters
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

    public String login() {
        User user = userService.authenticate(username, password);
        if (user != null) {
            loggedInUser = user;
            return "board?faces-redirect=true"; // Navigate to dashboard page
        } else {
            // Add a message for failed login
            javax.faces.context.FacesContext.getCurrentInstance()
                    .addMessage(null, new javax.faces.application.FacesMessage("Invalid username or password"));
            return null; // Stay on the same page
        }
    }

    public String logout() {
        loggedInUser = null;
        return "login?faces-redirect=true"; // Redirect to login page
    }

    // Registration method
    public String register() {
        User newUser = new User();
        newUser.setEmail(email);
        newUser.setUsername(username);
        newUser.setPassword(password); // The service will hash the password

        if (userService.register(newUser)) {
            System.out.println("User registered successfully: " + username);
            return "success"; // Redirect to a success page
        } else {
            System.out.println("Registration failed. Username might already exist.");
            return "register"; // Stay on the registration page
        }
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }
    public User getLoggedInUser() {
        return loggedInUser;
    }
}
