package com.cliente.hexagonal.adapters.out.repository;

import com.cliente.hexagonal.adapters.out.repository.entity.OrderEntity;
import org.springframework.data.mongodb.repository.MongoRepository;

import java.util.List;

public interface OrderRepository extends MongoRepository<OrderEntity, String> {
    List<OrderEntity> findByCustomerId(String customerId);
}