package com.order.product.repository;

import com.order.product.model.entity.Brand;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface IBrandRepository extends JpaRepository<Brand, Integer> {

    Optional<Brand> findByBrandName(String brandName);

}
