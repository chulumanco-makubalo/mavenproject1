/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.mycompany.mavenproject1;
public class Login {
    private String username;
    private String password;
    private String phoneNumber;
    private boolean loggedIn = false;
    // 1. CHECK USERNAME
    public boolean checkUserName(String username) {
        return username.contains("_") && username.length() <= 15;
    }
    // 2. CHECK PASSWORD COMPLEXITY
    public boolean checkPasswordComplexity(String password) {
        boolean hasCapital = false;
        boolean hasNumber = false;
        boolean hasSpecial = false;
        for (int i = 0; i < password.length(); i++) {
            char c = password.charAt(i);
            if (Character.isUpperCase(c)) {
                hasCapital = true;
            } else if (Character.isDigit(c)) {
                hasNumber = true;
            } else if (!Character.isLetterOrDigit(c)) {
                hasSpecial = true;
            }
        }
        return password.length() >= 8 && hasCapital && hasNumber && hasSpecial;
    }
    // 3. CHECK CELL PHONE NUMBER
    public boolean checkCellPhoneNumber(String phone) {
        return phone.startsWith("+27") && phone.length() == 12;
    }
    // 4. REGISTER USER
    public String registerUser(String firstName, String lastName, String username, String password, String phoneNumber) {
        if (!checkUserName(username)) {
            return "Username is not correctly formatted; please ensure it contains an underscore and is no more than 15 characters long.";
        }
        if (!checkPasswordComplexity(password)) {
            return "Password is not correctly formatted; please ensure it contains at least eight characters, a capital letter, a number, and a special character.";
        }
        if (!checkCellPhoneNumber(phoneNumber)) {
            return "Cell phone number incorrectly formatted or does not contain the international code.";
        }
        this.username = username;
        this.password = password;
        this.phoneNumber = phoneNumber;
        return "User registered successfully.";
    }
    // 5. LOGIN USER
    public String loginUser(String username, String password) {
        boolean success = this.username != null &&
                          this.username.equals(username) &&
                          this.password.equals(password);
        if (success) {
            this.loggedIn = true;
            return "Welcome " + this.username + ", it is great to see you again.";
        } else {
            this.loggedIn = false;
            return "Username or password incorrect, please try again.";
        }
    }
    // 6. IS LOGGED IN
    public boolean isLoggedIn() {
        return loggedIn;
    }
    // 7. RETURN LOGIN STATUS
    public String returnLoginStatus(boolean success) {
        if (success) {
            return "Welcome " + this.username + ", it is great to see you again.";
        } else {
            return "Username or password incorrect, please try again.";
        }
    }
}