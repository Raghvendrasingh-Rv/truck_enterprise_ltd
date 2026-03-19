package com.truckplatform.auth.filter;

import com.truckplatform.auth.AuthConstants;
import com.truckplatform.auth.util.JwtTokenUtil;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.lang.NonNull;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Set;

@Component
@Slf4j
public class JwtAuthenticationFilter extends OncePerRequestFilter {
    private static final Set<String> PUBLIC_AUTH_PATHS = Set.of("/auth/login", "/auth/register", "/transporter-auth/login");

    @Autowired
    private JwtTokenUtil jwtTokenUtil;

    @Override
    protected void doFilterInternal(@NonNull HttpServletRequest request, @NonNull HttpServletResponse response, 
                                    @NonNull FilterChain filterChain) throws ServletException, IOException {
        try {
            String token = extractTokenFromRequest(request);
            
            if (StringUtils.hasText(token) && jwtTokenUtil.validateToken(token)) {
                String email = jwtTokenUtil.extractEmail(token);
                String role = jwtTokenUtil.extractRole(token);
                Boolean transporter = jwtTokenUtil.extractTransporter(token);
                String identityType = jwtTokenUtil.extractIdentityType(token);
                
                Collection<SimpleGrantedAuthority> authorities = new ArrayList<>();
                if (role != null) {
                    authorities.add(new SimpleGrantedAuthority("ROLE_" + role));
                }
                if (Boolean.TRUE.equals(transporter) || "TRANSPORTER".equals(identityType)) {
                    authorities.add(new SimpleGrantedAuthority("TRANSPORTER"));
                }
                
                UsernamePasswordAuthenticationToken authentication = 
                        new UsernamePasswordAuthenticationToken(email, null, authorities);
                
                SecurityContextHolder.getContext().setAuthentication(authentication);
            }
        } catch (Exception ex) {
            log.debug("Skipping JWT authentication: {}", ex.getMessage());
        }
        
        filterChain.doFilter(request, response);
    }

    @Override
    protected boolean shouldNotFilter(@NonNull HttpServletRequest request) {
        return PUBLIC_AUTH_PATHS.contains(request.getServletPath());
    }

    private String extractTokenFromRequest(HttpServletRequest request) {
        String authHeader = request.getHeader(AuthConstants.AUTHORIZATION_HEADER);
        if (StringUtils.hasText(authHeader) && authHeader.startsWith(AuthConstants.BEARER_PREFIX)) {
            return authHeader.substring(AuthConstants.BEARER_PREFIX.length());
        }
        return null;
    }
}
