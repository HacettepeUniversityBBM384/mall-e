package com.malle.Dao;

import com.malle.Entity.Item;
import com.malle.Entity.Order;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;


@Repository
public interface OrderDao extends JpaRepository<Order, Integer>{
    @Override
     void deleteById(Integer ınteger);

    @Override
    Optional<Order> findById(Integer integer);
}
