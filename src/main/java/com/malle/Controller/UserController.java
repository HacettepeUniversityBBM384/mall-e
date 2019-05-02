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
import org.springframework.security.web.authentication.logout.SecurityContextLogoutHandler;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.context.request.WebRequest;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
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
            model.addAttribute("user", userService.FindByEmail(user.getEmail()).get());
        }
        return "home";
    }

    @RequestMapping(value = "/login", method = RequestMethod.GET)
    public String Login(@ModelAttribute("user") User user) {
        return "login";
    }

    @RequestMapping(value = "/login", method = RequestMethod.POST)
    public String LoginPost(@ModelAttribute("user") User user, Model model) {
        if (user != null) {
            model.addAttribute("user", userService.FindByEmail(user.getEmail()).get());
        }
        return "home";
    }

    @RequestMapping(value = "/register", method = RequestMethod.POST)
    public String AddUser(@ModelAttribute("user") User user, Model model) {
        user.setRole("CUSTOMER");
        userService.Save(user);
        model.addAttribute("registered", 1);
        return "login";
    }

    @RequestMapping(value = "/profile", method = RequestMethod.GET)
    public String Profile(@AuthenticationPrincipal CustomUserDetails user, Model model) {
        if (user != null) {
            model.addAttribute("user", userService.FindByEmail(user.getEmail()).get());
        }
        return "profile";
    }

    @RequestMapping(value = "/items", method = RequestMethod.GET)
    public String Items(@AuthenticationPrincipal CustomUserDetails user, Model model) {
        if (user != null) {
            model.addAttribute("user", userService.FindByEmail(user.getEmail()).get());
            model.addAttribute("data", "items");
        }
        return "datatable";
    }

    @RequestMapping(value = "/orders", method = RequestMethod.GET)
    public String Orders(@AuthenticationPrincipal CustomUserDetails user, Model model) {
        if (user != null) {
            model.addAttribute("user", userService.FindByEmail(user.getEmail()).get());
            model.addAttribute("data", "orders");
        }
        return "datatable";
    }

    @RequestMapping(value = "/customers", method = RequestMethod.GET)
    public String Customers(@AuthenticationPrincipal CustomUserDetails user, Model model) {
        if (user != null) {
            model.addAttribute("user", userService.FindByEmail(user.getEmail()).get());
            model.addAttribute("data", "customers");
        }
        return "datatable";
    }

    @RequestMapping(value = "/sellers", method = RequestMethod.GET)
    public String Sellers(@AuthenticationPrincipal CustomUserDetails user, Model model) {
        if (user != null) {
            model.addAttribute("user", userService.FindByEmail(user.getEmail()).get());
            model.addAttribute("data", "sellers");
        }
        return "datatable";
    }

    @RequestMapping(value = "/profile/update", method = RequestMethod.GET)
    public String ProfileUpdate(@AuthenticationPrincipal CustomUserDetails user, Model model) {
        if (user != null) {
            model.addAttribute("user", userService.FindByEmail(user.getEmail()).get());
        }
        return "profileUpdate";
    }

    @RequestMapping(value = "/profile/changepassword", method = RequestMethod.GET)
    public String ProfilePsw(@AuthenticationPrincipal CustomUserDetails user, Model model) {
        if (user != null) {
            model.addAttribute("user", userService.FindByEmail(user.getEmail()).get());
        }
        return "profilePsw";
    }

    @RequestMapping(value = "/profile", method = RequestMethod.POST)
    public String ProfileUpdateData(@AuthenticationPrincipal CustomUserDetails user, Model model, @ModelAttribute("user") User userUpdate) {
        User userAuth = userService.FindByEmail(user.getEmail()).get();
        userUpdate.setId(userAuth.getId());
        userUpdate.setRole(userAuth.getRole());
        userUpdate.setPassword(userAuth.getPassword());
        userService.Save(userUpdate);
        model.addAttribute("user", userUpdate);
        return "profile";
    }

    @RequestMapping(value = "/changepassword", method = RequestMethod.POST)
    public String ProfileChangePassword(@AuthenticationPrincipal CustomUserDetails user, Model model, @ModelAttribute("user") User userUpdate) {
        User userAuth = userService.FindByEmail(user.getEmail()).get();
        userUpdate.setId(userAuth.getId());
        userUpdate.setRole(userAuth.getRole());
        userUpdate.setAddress(userAuth.getAddress());
        userUpdate.setEmail(userAuth.getEmail());
        userUpdate.setName(userAuth.getName());
        userUpdate.setPhone(userAuth.getPhone());
        userUpdate.setSurname(userAuth.getSurname());
        userService.Save(userUpdate);
        model.addAttribute("user",userUpdate);
        return "profile";
    }

    @RequestMapping(value = "/profile/delete", method = RequestMethod.GET)
    public String ProfileDelete(@AuthenticationPrincipal CustomUserDetails user, HttpServletRequest request, HttpServletResponse response) {
        User userAuth = userService.FindByEmail(user.getEmail()).get();
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        if (auth != null){
            new SecurityContextLogoutHandler().logout(request, response, auth);
        }
        userService.DeleteById(userAuth.getId());
        return "home";
    }

    @RequestMapping(value="/logout", method = RequestMethod.POST)
    public String logoutPage (HttpServletRequest request, HttpServletResponse response) {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        if (auth != null){
            new SecurityContextLogoutHandler().logout(request, response, auth);
        }
        return "home";
    }
}
