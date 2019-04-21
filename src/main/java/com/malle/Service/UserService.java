package com.malle.Service;

import com.malle.Dao.UserDao;
import com.malle.Entity.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Collection;

@Service
public class UserService {

    @Autowired
    public UserDao userDao;

    public Collection<User> getAllUsers(){
        return userDao.findAll();
    }

}
