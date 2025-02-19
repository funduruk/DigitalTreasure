package com.DigitalShop.DigitalTreasure.Entities.Enums;

import org.springframework.security.core.GrantedAuthority;

public enum RoleUser implements GrantedAuthority {
    ROLE_ADMIN, ROLE_USER;


    @Override
    public String getAuthority() {
        return name();
    }
}
