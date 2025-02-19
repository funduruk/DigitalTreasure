package com.DigitalShop.DigitalTreasure.Controllers;

import com.DigitalShop.DigitalTreasure.DTO.PurchaseDto;
import com.DigitalShop.DigitalTreasure.DTO.UserProfileDto;
import com.DigitalShop.DigitalTreasure.Entities.UserEntity;
import com.DigitalShop.DigitalTreasure.Repositories.UserRepository;
import com.DigitalShop.DigitalTreasure.Services.AuthService;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Collections;
import java.util.List;

@RestController
@RequestMapping("/user")
@RequiredArgsConstructor
public class UserController {
    private final AuthService authService;

    @Transactional
    @PreAuthorize("hasAnyRole('ROLE_USER', 'ROLE_ADMIN')")
    @GetMapping("/me")
    public UserProfileDto getUserProfile(Authentication userDetails) {
        if (userDetails == null) {
            throw new RuntimeException("Authentication not found");
        }

        UserEntity user = (UserEntity) authService.loadUserByUsername(userDetails.getName());
        user.getPurchases().size();

        List<PurchaseDto> purchaseDtos = user.getPurchases().stream()
                .map(p -> new PurchaseDto(p.getName(), p.getPrice()))
                .toList();
        return  new UserProfileDto(user.getUsername(), user.getEmail(), user.getBalance(), purchaseDtos);
    }
}
