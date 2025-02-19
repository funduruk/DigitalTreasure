package com.DigitalShop.DigitalTreasure.Controllers;

import com.DigitalShop.DigitalTreasure.Components.JwtTokenProvider;
import com.DigitalShop.DigitalTreasure.Entities.AuthRequestEntity;
import com.DigitalShop.DigitalTreasure.Entities.JwtAuthenticationResponse;
import com.DigitalShop.DigitalTreasure.Entities.UserEntity;
import com.DigitalShop.DigitalTreasure.Services.AuthService;
import jakarta.servlet.http.HttpServletRequest;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.csrf.CsrfToken;
import org.springframework.web.bind.annotation.*;

import java.util.Collections;

@RestController
@Data
@AllArgsConstructor
@Slf4j
@RequestMapping("/auth")
public class AuthControllers {
    private final AuthService authService;
    private final AuthenticationManager authenticationManager;
    private final JwtTokenProvider jwtTokenProvider;
    @GetMapping("/signup")
    public ResponseEntity<String> signupPage() {
        return ResponseEntity.ok("signup page");
    }

    @GetMapping("/loginup")
    public ResponseEntity<String> loginPage() {
        return ResponseEntity.ok("login page");
    }

    @PostMapping("/signup")
    public ResponseEntity<?> signup(@RequestBody UserEntity user) {
        try {
            log.info("Signup user: {}", user);
            authService.saveUser(user);
            log.info("Signup completed");
        } catch (Exception e) {
            log.error("Signup failed: ", e);
        }
        return ResponseEntity.ok().build();
    }

    @PostMapping("/loginup")
    private ResponseEntity<?> loginup(@RequestBody AuthRequestEntity user) {
        log.info("Login up user: {}", user);
        try {
            log.debug("Starting authentication");
            Authentication auth = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(user.getUsername(), user.getPassword())
            );
            log.debug("Authentication completed");
            SecurityContextHolder.getContext().setAuthentication(auth);

            String token = jwtTokenProvider.generateToken(auth);
            System.out.println(token);
            return ResponseEntity.ok(new JwtAuthenticationResponse(token));

        } catch (AuthenticationException e) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(e.getMessage());
        }
    }
}
