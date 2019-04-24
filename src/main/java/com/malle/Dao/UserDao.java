package com.malle.Dao;

import com.malle.Entity.User;
import org.springframework.data.domain.Example;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Repository;

import java.util.Optional;


@Repository
public interface UserDao extends JpaRepository<User, Integer>{

    Optional<User> findByEmail(String email);

    @Override
    Optional<User> findById(Integer integer);
}
