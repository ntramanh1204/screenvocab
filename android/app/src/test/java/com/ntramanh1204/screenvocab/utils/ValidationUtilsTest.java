package com.ntramanh1204.screenvocab.utils;

import org.junit.Test;
import static org.junit.Assert.*;

public class ValidationUtilsTest {
    
    @Test
    public void testValidEmail() {
        assertTrue(ValidationUtils.isValidEmail("test@example.com"));
        assertFalse(ValidationUtils.isValidEmail("invalid-email"));
        assertFalse(ValidationUtils.isValidEmail(""));
        assertFalse(ValidationUtils.isValidEmail(null));
    }
    
    @Test
    public void testValidPassword() {
        assertTrue(ValidationUtils.isValidPassword("password123"));
        assertFalse(ValidationUtils.isValidPassword("123")); // Too short
        assertFalse(ValidationUtils.isValidPassword(""));
        assertFalse(ValidationUtils.isValidPassword(null));
    }
    
    @Test
    public void testValidName() {
        assertTrue(ValidationUtils.isValidName("John Doe"));
        assertTrue(ValidationUtils.isValidName("李明"));
        assertFalse(ValidationUtils.isValidName("A")); // Too short
        assertFalse(ValidationUtils.isValidName(""));
        assertFalse(ValidationUtils.isValidName(null));
    }
    
    @Test
    public void testPasswordsMatch() {
        assertTrue(ValidationUtils.doPasswordsMatch("password", "password"));
        assertFalse(ValidationUtils.doPasswordsMatch("password", "different"));
        assertFalse(ValidationUtils.doPasswordsMatch(null, "password"));
    }
    
    @Test
    public void testFormValidation() {
        assertTrue(ValidationUtils.isFormValid("John Doe", "john@example.com", "password123", "password123"));
        assertFalse(ValidationUtils.isFormValid("", "john@example.com", "password123", "password123"));
        assertFalse(ValidationUtils.isFormValid("John Doe", "invalid-email", "password123", "password123"));
        assertFalse(ValidationUtils.isFormValid("John Doe", "john@example.com", "123", "123"));
        assertFalse(ValidationUtils.isFormValid("John Doe", "john@example.com", "password123", "different"));
    }
}