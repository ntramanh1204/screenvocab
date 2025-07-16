package com.ntramanh1204.screenvocab.core.utils;

public class ValidationUtils {
    
    /**
     * Validates full name
     * @param name The name to validate
     * @return true if valid, false otherwise
     */
    public static boolean isValidName(String name) {
        return name != null && name.trim().length() >= 2;
    }
    
    /**
     * Validates email format
     * @param email The email to validate
     * @return true if valid, false otherwise
     */
    public static boolean isValidEmail(String email) {
        // this using Android Framework - only avai on android devices, not jvm
        // return email != null && Patterns.EMAIL_ADDRESS.matcher(email).matches();
        return email != null && email.matches("^[A-Za-z0-9+_.-]+@[A-Za-z0-9.-]+$");
    }
    
    /**
     * Validates password strength
     * @param password The password to validate
     * @return true if valid, false otherwise
     */
    public static boolean isValidPassword(String password) {
        return password != null && password.length() >= 6;
    }
    
    /**
     * Checks if passwords match
     * @param password Original password
     * @param confirmPassword Confirmation password
     * @return true if they match, false otherwise
     */
    public static boolean doPasswordsMatch(String password, String confirmPassword) {
        return password != null && password.equals(confirmPassword);
    }
    
    /**
     * Validates all form fields
     * @param name Full name
     * @param email Email address
     * @param password Password
     * @param confirmPassword Confirm password
     * @return true if all fields are valid, false otherwise
     */
    public static boolean isFormValid(String name, String email, String password, String confirmPassword) {
        return isValidName(name) && 
               isValidEmail(email) && 
               isValidPassword(password) && 
               doPasswordsMatch(password, confirmPassword);
    }

    /**
     * Checks if string is null or empty after trimming
     * @param text The text to check
     * @return true if empty, false otherwise
     */
    public static boolean isEmpty(String text) {
        return text == null || text.trim().isEmpty();
    }
}