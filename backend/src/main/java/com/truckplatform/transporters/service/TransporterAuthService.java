package com.truckplatform.transporters.service;

import com.truckplatform.auth.util.JwtTokenUtil;
import com.truckplatform.transporters.dto.TransporterLoginRequest;
import com.truckplatform.transporters.dto.TransporterLoginResponse;
import com.truckplatform.transporters.entity.Transporter;
import com.truckplatform.transporters.repository.TransporterRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Collections;

@Service
public class TransporterAuthService {

    @Autowired
    private TransporterRepository transporterRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Autowired
    private JwtTokenUtil jwtTokenUtil;

    public TransporterLoginResponse login(TransporterLoginRequest request) {
        Transporter transporter = transporterRepository.findByEmailIgnoreCase(request.getEmail())
                .orElseThrow(() -> new RuntimeException("Invalid email or password"));

        if (!passwordEncoder.matches(request.getPassword(), transporter.getPasswordHash())) {
            throw new RuntimeException("Invalid email or password");
        }

        UserDetails userDetails = new org.springframework.security.core.userdetails.User(
                transporter.getEmail(),
                transporter.getPasswordHash(),
                Collections.singletonList(new SimpleGrantedAuthority("TRANSPORTER"))
        );

        String token = jwtTokenUtil.generateTokenWithClaims(userDetails, null, true, transporter.getId(), "TRANSPORTER");

        return new TransporterLoginResponse(
                token,
                transporter.getId(),
                transporter.getEmail(),
                transporter.getName(),
                transporter.getCompanyName(),
                "Bearer"
        );
    }
}
