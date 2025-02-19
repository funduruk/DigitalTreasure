package com.DigitalShop.DigitalTreasure.Repositories;

import com.DigitalShop.DigitalTreasure.Entities.ProductEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ProductRepository extends JpaRepository<ProductEntity, Long> {

}
