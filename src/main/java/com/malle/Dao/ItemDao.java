package com.malle.Dao;

import com.malle.Entity.Item;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface ItemDao extends JpaRepository<Item, Integer>{
    @Override
     void deleteById(Integer integer);

    @Override
    Optional<Item> findById(Integer integer);
}
