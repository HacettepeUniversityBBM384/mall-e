package com.malle.Controller;

import com.malle.Dao.UserDao;
import com.malle.Entity.*;
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
    public String HomePage(@AuthenticationPrincipal CustomUserDetails user,Model model) {
        if (user != null) {
            model.addAttribute("user", userService.FindByEmail(user.getEmail()).get());
        }
        return "home";
    }

    @RequestMapping(value = "/login", method = RequestMethod.GET)
    public String LoginPage(@AuthenticationPrincipal CustomUserDetails user, Model model) {
        model.addAttribute("user", new Customer());
        return "login";
    }

    @RequestMapping(value = "/login", method = RequestMethod.POST)
    public String Login(@ModelAttribute("user") User user, Model model) {
        if (user != null) {
            model.addAttribute("user", userService.FindByEmail(user.getEmail()).get());
        }
        return "home";
    }

    @RequestMapping(value = "/register", method = RequestMethod.POST)
    public String AddUser(@ModelAttribute("user") Customer user, Model model) {
        user.setRole("CUSTOMER");
        userService.Save(user);
        model.addAttribute("user",null);
        model.addAttribute("registered", 1);
        return "login";
    }

    @RequestMapping(value = "/profile", method = RequestMethod.GET)
    public String ProfilePage(@AuthenticationPrincipal CustomUserDetails user, Model model) {
        if (user != null) {
            model.addAttribute("user", userService.FindByEmail(user.getEmail()).get());
        }
        return "profile";
    }

    @RequestMapping(value = "/items", method = RequestMethod.GET)
    public String ItemsPage(@AuthenticationPrincipal CustomUserDetails user, Model model) {
        if (user != null) {
            model.addAttribute("user", userService.FindByEmail(user.getEmail()).get());
            model.addAttribute("data", "items");
        }
        else model.addAttribute("data","nop");
        return "datatable";
    }

    @RequestMapping(value = "/admins", method = RequestMethod.GET)
    public String AdminsPage(@AuthenticationPrincipal CustomUserDetails user, Model model) {
        if (user != null) {
            model.addAttribute("user", userService.FindByEmail(user.getEmail()).get());
            model.addAttribute("data", "admins");
        }
        else model.addAttribute("data","nop");
        return "datatable";
    }

    @RequestMapping(value = "/orders", method = RequestMethod.GET)
    public String OrdersPage(@AuthenticationPrincipal CustomUserDetails user, Model model) {
        if (user != null) {
            model.addAttribute("user", userService.FindByEmail(user.getEmail()).get());
            model.addAttribute("data", "orders");
        }
        else model.addAttribute("data","nop");
        return "datatable";
    }

    @RequestMapping(value = "/customers", method = RequestMethod.GET)
    public String CustomersPage(@AuthenticationPrincipal CustomUserDetails user, Model model) {
        if (user != null) {
            model.addAttribute("user", userService.FindByEmail(user.getEmail()).get());
            model.addAttribute("data", "customers");
        }
        else model.addAttribute("data","nop");
        return "datatable";
    }

    @RequestMapping(value = "/sellers", method = RequestMethod.GET)
    public String SellersPage(@AuthenticationPrincipal CustomUserDetails user, Model model) {
        if (user != null) {
            model.addAttribute("user", userService.FindByEmail(user.getEmail()).get());
            model.addAttribute("data", "sellers");
        }
        else model.addAttribute("data","nop");
        return "datatable";
    }

    @RequestMapping(value = "/updateadmin", method = RequestMethod.GET)
    public String UpdateAdminPage(@AuthenticationPrincipal CustomUserDetails authuser, Model model, @ModelAttribute("user") Admin user) {
        if (user != null) {
            model.addAttribute("user", userService.FindByEmail(authuser.getEmail()).get());
            model.addAttribute("updateduser", user);
        }
        return "profileUpdate";
    }

    @RequestMapping(value = "/updateseller", method = RequestMethod.GET)
    public String UpdateSellerPage(@AuthenticationPrincipal CustomUserDetails authuser, Model model, @ModelAttribute("user") Seller user) {
        if (user != null) {
            model.addAttribute("user", userService.FindByEmail(authuser.getEmail()).get());
            model.addAttribute("updateduser", user);
        }
        return "profileUpdate";
    }

    @RequestMapping(value = "/updatecustomer", method = RequestMethod.GET)
    public String UpdateCustomerPage(@AuthenticationPrincipal CustomUserDetails authuser, Model model, @ModelAttribute("user") Customer user) {
        if (user != null) {
            model.addAttribute("user", userService.FindByEmail(authuser.getEmail()).get());
            model.addAttribute("updateduser", user);
        }
        return "profileUpdate";
    }

    @RequestMapping(value = "/profile/changepassword", method = RequestMethod.GET)
    public String PasswordPage(@AuthenticationPrincipal CustomUserDetails user, Model model) {
        if (user != null) {
            model.addAttribute("user", userService.FindByEmail(user.getEmail()).get());
        }
        return "profilePsw";
    }

    @RequestMapping(value = "/additem", method = RequestMethod.GET)
    public String AddItemPage(@AuthenticationPrincipal CustomUserDetails user, Model model) {
        if (user != null) {
            model.addAttribute("user", userService.FindByEmail(user.getEmail()).get());
            model.addAttribute("data","item");
        }
        return "add";
    }

    @RequestMapping(value = "/addseller", method = RequestMethod.GET)
    public String AddSellerPage(@AuthenticationPrincipal CustomUserDetails user, Model model) {
        if (user != null) {
            model.addAttribute("user", userService.FindByEmail(user.getEmail()).get());
            model.addAttribute("newuser", new Seller());
            model.addAttribute("data","seller");
        }
        return "add";
    }

    @RequestMapping(value = "/addseller", method = RequestMethod.POST)
    public String AddSeller(@ModelAttribute("newuser") Seller newuser, @ModelAttribute("user") Admin user, Model model) {
        newuser.setRole("SELLER");
        userService.Save(newuser);
        model.addAttribute("data", "sellers");
        model.addAttribute("registered", "seller");
        model.addAttribute("user", userService.FindByEmail(user.getEmail()).get());
        return "datatable";
    }

    @RequestMapping(value = "/addadmin", method = RequestMethod.GET)
    public String AddAdminPage(@AuthenticationPrincipal CustomUserDetails user, Model model) {
        if (user != null) {
            model.addAttribute("user", userService.FindByEmail(user.getEmail()).get());
            model.addAttribute("newuser", new Admin());
            model.addAttribute("data", "admin");
        }
        return "add";
    }

    @RequestMapping(value = "/addadmin", method = RequestMethod.POST)
    public String AddAdmin(@ModelAttribute("newuser") Admin newuser, @ModelAttribute("user") Admin user, Model model) {
        newuser.setRole("ADMIN");
        userService.Save(newuser);
        model.addAttribute("data", "admins");
        model.addAttribute("registered", "admin");
        model.addAttribute("user", userService.FindByEmail(user.getEmail()).get());
        return "datatable";
    }

    @RequestMapping(value = "/updatecustomer", method = RequestMethod.POST)
    public String UpdateCustomer(@AuthenticationPrincipal CustomUserDetails authuser, Model model, @ModelAttribute("updateduser") Customer userUpdate,  @ModelAttribute("user") Customer user) {
        User userAuth = userService.FindByEmail(authuser.getEmail()).get();
        userUpdate.setPassword(userAuth.getPassword());
        userUpdate.setId(userAuth.getId());
        userUpdate.setRole(userAuth.getRole());
        userUpdate.setCartid(user.getCartid());
        userService.Save(userUpdate);
        model.addAttribute("user", userUpdate);
        model.addAttribute("registered", "update");
        return "profile";
    }

    @RequestMapping(value = "/updateseller", method = RequestMethod.POST)
    public String UpdateSeller(@AuthenticationPrincipal CustomUserDetails authuser, Model model, @ModelAttribute("updateduser") Seller userUpdate,  @ModelAttribute("user") Seller user) {
        User userAuth = userService.FindByEmail(authuser.getEmail()).get();
        userUpdate.setPassword(userAuth.getPassword());
        userUpdate.setId(userAuth.getId());
        userUpdate.setRole(userAuth.getRole());
        userUpdate.setSale(user.getSale());
        userUpdate.setRating(user.getRating());
        userUpdate.setShopname(user.getShopname());
        userService.Save(userUpdate);
        model.addAttribute("user", userUpdate);
        model.addAttribute("registered", "update");
        return "profile";
    }

    @RequestMapping(value = "/updateadmin", method = RequestMethod.POST)
    public String UpdateAdmin(@AuthenticationPrincipal CustomUserDetails authuser, Model model, @ModelAttribute("updateduser") Admin userUpdate,  @ModelAttribute("user") Admin user) {
        User userAuth = userService.FindByEmail(authuser.getEmail()).get();
        userUpdate.setId(userAuth.getId());
        userUpdate.setPassword(userAuth.getPassword());
        userUpdate.setRole(userAuth.getRole());
        userService.Save(userUpdate);
        model.addAttribute("user", userUpdate);
        model.addAttribute("registered", "update");
        return "profile";
    }

    @RequestMapping(value = "/changepassword", method = RequestMethod.POST)
    public String ProfileChangePassword(@AuthenticationPrincipal CustomUserDetails user, Model model, @ModelAttribute("user") User userUpdate) {
        User userAuth = userService.FindByEmail(user.getEmail()).get();
        userAuth.setPassword(userUpdate.getPassword());
        userService.Save(userAuth);
        model.addAttribute("user",userAuth);
        model.addAttribute("registered", "password");
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
