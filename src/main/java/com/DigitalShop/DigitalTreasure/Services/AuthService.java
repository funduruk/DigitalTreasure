package com.DigitalShop.DigitalTreasure.Services;

import com.DigitalShop.DigitalTreasure.Entities.Enums.RoleUser;
import com.DigitalShop.DigitalTreasure.Entities.UserEntity;
import com.DigitalShop.DigitalTreasure.Repositories.UserRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.security.Principal;
import java.util.Collections;

@Service
@Slf4j
@RequiredArgsConstructor
public class AuthService implements UserDetailsService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    @Override
    public UserDetails loadUserByUsername(String login) throws UsernameNotFoundException {
        log.info("Entering loadUserByUsername with login: {}" , login);
        UserEntity userEntity = userRepository.findByEmail(login);
        if(userEntity == null) {
            userEntity = userRepository.findByUsername(login);
            if(userEntity == null) {
                throw new UsernameNotFoundException(login);
            }
        }
        log.info("Exit loadUserByUsername");
        return userEntity;
    }

    public UserEntity saveUser(UserEntity userEntity) {
        log.info("Saving user: " + userEntity);
        String email = userEntity.getEmail();
        log.info("User email: {}", email);
        if(userRepository.findByEmail(email) != null) {
            throw new UsernameNotFoundException("user not found, check email: " + email);
        }
        userEntity.setActive(true);
        userEntity.setPassword(passwordEncoder.encode(userEntity.getPassword()));
        userEntity.setBalance(0.0);
        log.info("user password: {}", userEntity.getPassword());
        userEntity.setRole(Collections.singleton(RoleUser.ROLE_USER));
        log.info("User saved: {}", userEntity);
        return userRepository.save(userEntity);
    }

    private UserEntity getUserByPrincipal(Principal principal) {
        if(principal == null) return new UserEntity();
        return userRepository.findByEmail(principal.getName());
    }
}
