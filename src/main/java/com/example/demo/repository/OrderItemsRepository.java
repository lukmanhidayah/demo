package com.example.demo.repository;

import com.example.demo.models.Orderitems;
import org.springframework.data.repository.CrudRepository;

public interface OrderItemsRepository extends CrudRepository<Orderitems, Long> {
}
