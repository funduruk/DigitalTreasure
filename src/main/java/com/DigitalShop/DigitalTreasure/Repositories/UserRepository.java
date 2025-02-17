package com.DigitalShop.DigitalTreasure.Repositories;

import com.DigitalShop.DigitalTreasure.Entities.UserEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface UserRepository extends JpaRepository<UserEntity, Long> {
    UserEntity findByEmail(String email);

    UserEntity findByUsername(String username);
}
