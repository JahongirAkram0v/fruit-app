package com.example.fruit.repo;

import com.example.fruit.model.Order;
import jakarta.transaction.Transactional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;

public interface OrderRepo extends JpaRepository<Order, String> {

    @Transactional
    @Modifying
    void deleteById(String id);
}
