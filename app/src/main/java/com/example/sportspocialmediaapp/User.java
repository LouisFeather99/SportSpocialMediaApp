package com.example.sportspocialmediaapp;


/**
 * User class to represent user details throughout the application.
 */
public class User {
    private String name;
    private String email;
    private String username;

    /**
     * Constructor for User object.
     *
     * @param name The user's name.
     * @param email The user's email address.
     * @param username The user's username.
     */
    public User(String name, String email, String username) {
        this.name = name;
        this.email = email;
        this.username = username;
    }

    // Getters
    public String getName() {
        return name;
    }

    public String getEmail() {
        return email;
    }

    public String getUsername() {
        return username;
    }

    // Setters
    public void setName(String name) {
        this.name = name;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public void setUsername(String username) {
        this.username = username;
    }
}
