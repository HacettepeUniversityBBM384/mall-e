package com.malle.Controller;

import com.malle.Entity.CustomUserDetails;
import com.malle.Entity.User;
import com.malle.Service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

@Controller
@RequestMapping()
public class UserController {

    @Autowired
    private UserService userService;


    @PreAuthorize("hasAnyRole('ADMIN')")
    @RequestMapping(value = "/secured/users", method = RequestMethod.GET)
    public Iterable<User> getAllUsers(){
        return userService.getAllUsers();
    }

    @RequestMapping(value = "/", method = RequestMethod.GET)
    public String homePage(@AuthenticationPrincipal CustomUserDetails user) {
        if (user != null)
            System.out.println(user.getName());
        return "home";
    }

    @RequestMapping(value = "/login", method = RequestMethod.GET)
    public String Login() {
        return "login";
    }

}
