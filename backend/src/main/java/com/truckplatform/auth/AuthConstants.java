package com.truckplatform.auth;

/**
 * Authentication module
 * Handles JWT token generation, validation, and authentication logic
 */
public class AuthConstants {
    public static final String BEARER_PREFIX = "Bearer ";
    public static final String AUTHORIZATION_HEADER = "Authorization";
    public static final long JWT_EXPIRATION_MS = 86400000; // 24 hours
}
