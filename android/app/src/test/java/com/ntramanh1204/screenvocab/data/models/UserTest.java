package com.ntramanh1204.screenvocab.data.models;

import org.junit.Test;
import static org.junit.Assert.*;

public class UserTest {
    
    @Test
    public void testUserCreation() {
        User user = new User("user123", "John Doe", "john@example.com", false);
        
        assertEquals("user123", user.getUserId());
        assertEquals("John Doe", user.getDisplayName());
        assertEquals("john@example.com", user.getEmail());
        assertFalse(user.isGuest());
        assertTrue(user.getCreatedAt() > 0);
        assertTrue(user.getLastSyncAt() > 0);
    }
    
    @Test
    public void testGuestUser() {
        User guestUser = new User("guest123", "Guest User", "", true);
        
        assertEquals("guest123", guestUser.getUserId());
        assertTrue(guestUser.isGuest());
        assertEquals("", guestUser.getEmail());
    }
}