package com.malle.Service;

import com.malle.Dao.UserDao;
import com.malle.Entity.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Collection;
import java.util.Optional;

@Service
public class UserService {

    @Autowired
    public UserDao userDao;

    public Collection<User> getAllUsers(){
        return userDao.findAll();
    }

    public User Save(User user) {return userDao.save(user);}

    public Optional<User> FindByEmail(String email){return userDao.findByEmail(email);}

    public Optional<User> FindById(int id){return userDao.findById(id);}
}
