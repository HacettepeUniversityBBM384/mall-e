package com.malle.Controller;

import com.malle.Dao.UserDao;
import com.malle.Entity.CustomUserDetails;
import com.malle.Entity.User;
import com.malle.Service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.context.request.WebRequest;

import javax.validation.Valid;
import java.util.Optional;

@Controller
@RequestMapping
public class UserController {

    @Autowired
    private UserService userService;


    @PreAuthorize("hasAnyRole('ADMIN')")
    @RequestMapping(value = "/secured/users", method = RequestMethod.GET)
    public Iterable<User> getAllUsers(){
        return userService.getAllUsers();
    }

    @RequestMapping(value = "/", method = RequestMethod.GET)
    public String homePage(@AuthenticationPrincipal CustomUserDetails user,Model model) {
        if (user != null) {
            User userAuth = new User();
            userAuth = userService.FindByEmail(user.getEmail()).get();
            model.addAttribute("user", userAuth);
        }
        return "home";
    }

    @RequestMapping(value = "/login", method = RequestMethod.GET)
    public String Login(@ModelAttribute("user") User user, Model model) {
        model.addAttribute("user", user);
        return "login";
    }

    @RequestMapping(value = "/login", method = RequestMethod.POST)
    public String LoginPost(@ModelAttribute("user") User user) {
        System.out.println(user.getRole());
        return "home";
    }

    @RequestMapping(value = "/register", method = RequestMethod.POST)
    public String AddUser(@ModelAttribute("user") User user) {
        System.out.println(user.getName());
        user.setRole("CUSTOMER");
        userService.Save(user);
        return "home";
    }

    @RequestMapping(value = "/profile", method = RequestMethod.GET)
    public String Profile(@AuthenticationPrincipal CustomUserDetails user, Model model) {
        if (user != null) {
            User userAuth = new User();
            userAuth = userService.FindByEmail(user.getEmail()).get();
            model.addAttribute("user", userAuth);
        }else
            model.addAttribute("user", new User());
        return "profile";
    }

    @RequestMapping(value = "/profile/{id}", method = RequestMethod.GET)
    public String showProfile(@PathVariable("id") String id, Model model) {
        Optional<User> user;
        user = userService.FindById(Integer.parseInt(id));
        if (user != null)
            System.out.println(user.get().getName());
            model.addAttribute("user", user.get());
        return "profile";
    }
}
