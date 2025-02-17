package com.DigitalShop.DigitalTreasure.Configs;

import com.DigitalShop.DigitalTreasure.Components.JwtAuthenticationFilter;
import com.DigitalShop.DigitalTreasure.Entities.Enums.RoleUser;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.LogoutConfigurer;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.security.web.csrf.CookieCsrfTokenRepository;
import org.springframework.security.web.csrf.CsrfFilter;
import org.springframework.web.cors.CorsConfiguration;

@Configuration
@EnableWebSecurity
@RequiredArgsConstructor
public class SecurityConfig {
    @Bean
    protected SecurityFilterChain securityFilterChain(HttpSecurity http, JwtAuthenticationFilter jwtAuthenticationFilter) throws Exception {
        http
        .cors(cors -> cors.configurationSource(request -> {
                    CorsConfiguration config = new CorsConfiguration();
                    config.addAllowedOrigin("http://localhost:3000");  // Разрешаем доступ с фронтенд-сервера
                    config.addAllowedHeader("X-XSRF-TOKEN");
                    config.setAllowCredentials(true);
                    config.addAllowedMethod("GET");
                    config.addAllowedMethod("POST");
                    config.addAllowedMethod("PUT");
                    config.addAllowedMethod("DELETE");
                    return config;
                }))
                .csrf(csrf -> csrf
                        .csrfTokenRepository(CookieCsrfTokenRepository.withHttpOnlyFalse())
                        .ignoringRequestMatchers("/auth/signup", "/**")  // Отключаем CSRF для регистрации и авторизации
                )


                .authorizeHttpRequests(authorizeRequests -> authorizeRequests
                        .requestMatchers("/", "/auth/**", "/auth/csrf", "/product").permitAll()
                        .requestMatchers("/user/me").hasAnyAuthority("ROLE_ADMIN", "ROLE_USER")
                        .anyRequest().hasAnyAuthority("ROLE_ADMIN", "ROLE_USER")
                )
                .logout(LogoutConfigurer::permitAll);
         http.cors(cors -> cors.configurationSource(request -> new CorsConfiguration().applyPermitDefaultValues()));

        http.addFilterBefore(new CsrfLoggingFilter(), UsernamePasswordAuthenticationFilter.class);
        http.addFilterBefore(jwtAuthenticationFilter, UsernamePasswordAuthenticationFilter.class);
        return http.build();
    }

    @Bean
    public AuthenticationManager authenticationManager(AuthenticationConfiguration authenticationConfiguration) throws Exception {
        return authenticationConfiguration.getAuthenticationManager();
    }

    @Bean
    PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder(8);
    }

}
