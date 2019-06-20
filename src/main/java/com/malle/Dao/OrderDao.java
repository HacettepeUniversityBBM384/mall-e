package com.malle.Dao;


import com.malle.Entity.Item;
import com.malle.Entity.Order;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import javax.persistence.Table;
import java.util.List;
import java.util.Optional;

@Repository

public interface OrderDao extends JpaRepository<Order, Integer> {

    @Override
    void deleteById(Integer Id);

    @Override
    Optional<Order> findById(Integer Id);

    List<Order> findAllByCustomerid(Integer Id);

}
