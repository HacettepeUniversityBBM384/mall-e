package com.malle.Dao;

import com.malle.Entity.Review;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface ReviewDao extends JpaRepository<Review, Integer>{
    @Override
    void deleteById(Integer ınteger);

    @Override
    Optional<Review> findById(Integer integer);
}

