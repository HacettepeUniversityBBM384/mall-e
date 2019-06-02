package com.malle.Dao;

import com.malle.Entity.Subcategory;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface SubcategoryDao extends JpaRepository<Subcategory, Integer> {
    @Override
    void deleteById(Integer Id);

    @Override
    Optional<Subcategory> findById(Integer Id);
}
