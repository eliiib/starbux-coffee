package com.starbux.coffee.repository;

import com.starbux.coffee.domain.Topping;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface ToppingRepository extends JpaRepository<Topping, Long> {

    Optional<Topping> findByName(String name);
}
