package com.truckplatform.auth.util;

import com.truckplatform.auth.AuthConstants;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;

import javax.crypto.SecretKey;
import java.util.Date;

@Component
@Slf4j
public class JwtTokenUtil {

    @Value("${app.jwt.secret:your-secret-key-change-this-in-production-at-least-256-bits-long}")
    private String jwtSecret;

    private SecretKey getSigningKey() {
        return Keys.hmacShaKeyFor(jwtSecret.getBytes());
    }

    /**
     * Generate JWT token from UserDetails
     */
    public String generateToken(UserDetails userDetails) {
        return Jwts.builder()
                .subject(userDetails.getUsername())
                .issuedAt(new Date())
                .expiration(new Date(System.currentTimeMillis() + AuthConstants.JWT_EXPIRATION_MS))
                .signWith(getSigningKey())
                .compact();
    }

    /**
     * Generate JWT token with custom claims
     */
    public String generateTokenWithClaims(UserDetails userDetails, String role) {
        return Jwts.builder()
                .subject(userDetails.getUsername())
                .claim("role", role)
                .issuedAt(new Date())
                .expiration(new Date(System.currentTimeMillis() + AuthConstants.JWT_EXPIRATION_MS))
                .signWith(getSigningKey())
                .compact();
    }

    public String generateTokenWithClaims(UserDetails userDetails, String role, boolean transporter, Long transporterId) {
        return generateTokenWithClaims(userDetails, role, transporter, transporterId, transporter ? "TRANSPORTER" : "USER");
    }

    public String generateTokenWithClaims(UserDetails userDetails, String role, boolean transporter, Long transporterId, String identityType) {
        return Jwts.builder()
                .subject(userDetails.getUsername())
                .claim("role", role)
                .claim("transporter", transporter)
                .claim("transporterId", transporterId)
                .claim("identityType", identityType)
                .issuedAt(new Date())
                .expiration(new Date(System.currentTimeMillis() + AuthConstants.JWT_EXPIRATION_MS))
                .signWith(getSigningKey())
                .compact();
    }

    /**
     * Extract email from JWT token
     */
    public String extractEmail(String token) {
        return getClaims(token).getSubject();
    }

    /**
     * Extract role from JWT token
     */
    public String extractRole(String token) {
        return getClaims(token).get("role", String.class);
    }

    public Boolean extractTransporter(String token) {
        return getClaims(token).get("transporter", Boolean.class);
    }

    public Long extractTransporterId(String token) {
        return getClaims(token).get("transporterId", Long.class);
    }

    public String extractIdentityType(String token) {
        return getClaims(token).get("identityType", String.class);
    }

    /**
     * Validate JWT token
     */
    public boolean validateToken(String token) {
        try {
            Jwts.parser()
                    .verifyWith(getSigningKey())
                    .build()
                    .parseSignedClaims(token);
            return true;
        } catch (Exception ex) {
            log.error("Invalid JWT token: {}", ex.getMessage());
            return false;
        }
    }

    /**
     * Get all claims from JWT token
     */
    private Claims getClaims(String token) {
        return Jwts.parser()
                .verifyWith(getSigningKey())
                .build()
                .parseSignedClaims(token)
                .getPayload();
    }

    /**
     * Check if token is expired
     */
    public boolean isTokenExpired(String token) {
        try {
            return getClaims(token).getExpiration().before(new Date());
        } catch (Exception ex) {
            return true;
        }
    }
}
