package com.malle.Dao;

import com.malle.Entity.Category;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface CategoryDao extends JpaRepository<Category, Integer> {
    @Override
    void deleteById(Integer ınteger);

    @Override
    Optional<Category> findById(Integer integer);

    Optional<Category> findByName(String name);
}
