package com.malle.Service;

import com.malle.Dao.UserDao;
import com.malle.Entity.CustomUserDetails;
import com.malle.Entity.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class CustomUserDetailsService implements UserDetailsService {

    @Autowired
    private UserDao userDao;


    @Override
    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
        Optional<User> optionalUser;
        optionalUser = userDao.findByEmail(email);

        optionalUser
                .orElseThrow(() -> new UsernameNotFoundException("Email not found!"));

        return optionalUser
                .map(CustomUserDetails::new).get();
    }
}
