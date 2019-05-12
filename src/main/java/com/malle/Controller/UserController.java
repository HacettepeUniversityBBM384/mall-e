package com.malle.Controller;

import com.malle.Entity.*;
import com.malle.Service.CategoryService;
import com.malle.Service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.logout.SecurityContextLogoutHandler;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.ArrayList;

import static com.malle.Controller.ItemController.cart;

@Controller
@RequestMapping
public class UserController {

    @Autowired
    private UserService userService;

    @Autowired
    private CategoryService categoryService;

    @RequestMapping(value = "/", method = RequestMethod.GET)
    public String HomePage(@AuthenticationPrincipal CustomUserDetails user,Model model) {
        if (user != null) {
            model.addAttribute("user", userService.FindByEmail(user.getEmail()).get());
        }
        model.addAttribute("cartitemlist", cart);
        model.addAttribute("categories", categoryService.getAllCategories());
        return "home";
    }

    @RequestMapping(value = "/login", method = RequestMethod.GET)
    public String LoginPage(Model model) {
        model.addAttribute("user", new Customer());
        model.addAttribute("cartitemlist", cart);
        model.addAttribute("categories", categoryService.getAllCategories());
        return "login";
    }

    @RequestMapping(value = "/register", method = RequestMethod.POST)
    public String SignUp(@ModelAttribute("user") Customer user, Model model) {
        if(userService.FindByEmail(user.getEmail()).isPresent())
            model.addAttribute("status", 0);
        else {
            user.setRole("CUSTOMER");
            userService.Save(user);
            model.addAttribute("user", new Customer());
            model.addAttribute("status", 1);
        }
        model.addAttribute("cartitemlist", cart);
        model.addAttribute("categories", categoryService.getAllCategories());
        return "login";
    }

    @RequestMapping(value = "/view", method = RequestMethod.GET)
    public String ViewUserPage(@RequestParam String id, @AuthenticationPrincipal CustomUserDetails user, Model model){
        model.addAttribute("status", model.asMap().get("status"));
        model.addAttribute("user", userService.FindById(Integer.parseInt(id)).get());
        model.addAttribute("authuser", userService.FindByEmail(user.getEmail()).get());
        model.addAttribute("cartitemlist", cart);
        model.addAttribute("categories", categoryService.getAllCategories());
        return "profile";
    }

    @RequestMapping(value = "/update", method = RequestMethod.GET)
    public String UpdateUserPage(@RequestParam String id, @AuthenticationPrincipal CustomUserDetails user, Model model) {
        model.addAttribute("user", userService.FindByEmail(user.getEmail()).get());
        model.addAttribute("updateduser", userService.FindById(Integer.parseInt(id)).get());
        model.addAttribute("cartitemlist", cart);
        model.addAttribute("categories", categoryService.getAllCategories());
        return "profileUpdate";
    }

    @RequestMapping(value = "/delete", method = RequestMethod.GET)
    public String DeleteUserPage(RedirectAttributes redir, @RequestParam String id, @AuthenticationPrincipal CustomUserDetails authuser, HttpServletRequest request, HttpServletResponse response) {
        User user = userService.FindById(Integer.parseInt(id)).get();
        User userAuth = userService.FindByEmail(authuser.getEmail()).get();
        if(user.getId()==userAuth.getId()) {
            Authentication auth = SecurityContextHolder.getContext().getAuthentication();
            if (auth != null) {
                new SecurityContextLogoutHandler().logout(request, response, auth);
            }
            userService.DeleteById(userAuth.getId());
            cart=new ArrayList<>();
            return "redirect:/";
        }
        else {
            userService.DeleteById(user.getId());
            if(user.getRole().equals("ADMIN")) {redir.addFlashAttribute("deleted", "admin"); return "redirect:/admins";}
            else if(user.getRole().equals("SELLER")) {redir.addFlashAttribute("deleted", "seller"); return "redirect:/sellers";}
            else {redir.addFlashAttribute("deleted", "customer"); return "redirect:/customers";}
        }
    }

    @RequestMapping(value="/logout", method = RequestMethod.POST)
    public String Logout (HttpServletRequest request, HttpServletResponse response, Model model) {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        if (auth != null){
            new SecurityContextLogoutHandler().logout(request, response, auth);
        }
        cart=new ArrayList<>();
        return "redirect:/";
    }

    @RequestMapping(value = "/admins", method = RequestMethod.GET)
    public String AdminsPage(@AuthenticationPrincipal CustomUserDetails user, Model model){
        model.addAttribute("deleted", model.asMap().get("deleted"));
        model.addAttribute("status", model.asMap().get("status"));
        model.addAttribute("user", userService.FindByEmail(user.getEmail()).get());
        model.addAttribute("data", "admins");
        ArrayList<Admin> adminlist = new ArrayList<Admin>();
        for (User i: userService.getAllUsers()){
            if(i.getRole().equals("ADMIN")) adminlist.add((Admin)i);
        }
        model.addAttribute("adminlist",adminlist);
        model.addAttribute("categories", categoryService.getAllCategories());
        return "datatable";
    }

    @RequestMapping(value = "/customers", method = RequestMethod.GET)
    public String CustomersPage(@AuthenticationPrincipal CustomUserDetails user, Model model) {
        model.addAttribute("deleted", model.asMap().get("deleted"));
        model.addAttribute("status", model.asMap().get("status"));
        model.addAttribute("user", userService.FindByEmail(user.getEmail()).get());
        model.addAttribute("data", "customers");
        ArrayList<Customer> customerlist = new ArrayList<Customer>();
        for (User i: userService.getAllUsers()){
            if(i.getRole().equals("CUSTOMER")) customerlist.add((Customer)i);
        }
        model.addAttribute("customerlist",customerlist);
        model.addAttribute("categories", categoryService.getAllCategories());
        return "datatable";
    }

    @RequestMapping(value = "/sellers", method = RequestMethod.GET)
    public String SellersPage(@AuthenticationPrincipal CustomUserDetails user, Model model){
        model.addAttribute("deleted", model.asMap().get("deleted"));
        model.addAttribute("user", userService.FindByEmail(user.getEmail()).get());
        model.addAttribute("data", "sellers");
        ArrayList<Seller> sellerlist = new ArrayList<Seller>();
        for (User i: userService.getAllUsers()){
            if(i.getRole().equals("SELLER")) sellerlist.add((Seller)i);
        }
        model.addAttribute("sellerlist",sellerlist);
        model.addAttribute("categories", categoryService.getAllCategories());
        return "datatable";
    }

    @RequestMapping(value = "/addseller", method = RequestMethod.GET)
    public String AddSellerPage(@AuthenticationPrincipal CustomUserDetails user, Model model) {
        model.addAttribute("user", userService.FindByEmail(user.getEmail()).get());
        model.addAttribute("newuser", new Seller());
        model.addAttribute("data","seller");
        model.addAttribute("categories", categoryService.getAllCategories());
        return "add";
    }

    @RequestMapping(value = "/addseller", method = RequestMethod.POST)
    public String AddSeller(RedirectAttributes redir, @ModelAttribute("newuser") Seller newuser) {
        if(userService.FindByEmail(newuser.getEmail()).isPresent())
            redir.addFlashAttribute("status", 0);
        else {
            newuser.setRole("SELLER");
            userService.Save(newuser);
            redir.addFlashAttribute("status", "seller");
        }
        return "redirect:/sellers";
    }

    @RequestMapping(value = "/addadmin", method = RequestMethod.GET)
    public String AddAdminPage(@AuthenticationPrincipal CustomUserDetails user, Model model) {
        model.addAttribute("user", userService.FindByEmail(user.getEmail()).get());
        model.addAttribute("newuser", new Admin());
        model.addAttribute("data", "admin");
        model.addAttribute("categories", categoryService.getAllCategories());
        return "add";
    }

    @RequestMapping(value = "/addadmin", method = RequestMethod.POST)
    public String AddAdmin(RedirectAttributes redir, @ModelAttribute("newuser") Admin newuser) {
        if(userService.FindByEmail(newuser.getEmail()).isPresent())
            redir.addFlashAttribute("status", 0);
        else {
            newuser.setRole("ADMIN");
            userService.Save(newuser);
            redir.addFlashAttribute("status", "admin");
        }
        return "redirect:/admins";
    }

    @RequestMapping(value = "/updatecustomer", method = RequestMethod.POST)
    public String UpdateCustomer(RedirectAttributes redir, @RequestParam String id, @ModelAttribute("updateduser") Customer updateduser) {
        Customer user = (Customer) userService.FindById(Integer.parseInt(id)).get();
        if (!user.getEmail().equals(updateduser.getEmail()) && userService.FindByEmail(updateduser.getEmail()).isPresent())
            redir.addFlashAttribute("status", 0);
        else {
            updateduser.setPassword(user.getPassword());
            updateduser.setId(user.getId());
            updateduser.setRole(user.getRole());
            updateduser.setCartid(user.getCartid());
            userService.Save(updateduser);
            redir.addFlashAttribute("status", "update");
        }
        return "redirect:/view?id="+id;
    }

    @RequestMapping(value = "/updateseller", method = RequestMethod.POST)
    public String UpdateSeller(RedirectAttributes redir, @RequestParam String id, @ModelAttribute("updateduser") Seller updateduser) {
        Seller user = (Seller) userService.FindById(Integer.parseInt(id)).get();
        if(!user.getEmail().equals(updateduser.getEmail()) && userService.FindByEmail(updateduser.getEmail()).isPresent())
            redir.addFlashAttribute("status", 0);
        else {
            updateduser.setPassword(user.getPassword());
            updateduser.setId(user.getId());
            updateduser.setRole(user.getRole());
            updateduser.setSale(user.getSale());
            updateduser.setRating(user.getRating());
            updateduser.setShopname(user.getShopname());
            userService.Save(updateduser);
            redir.addFlashAttribute("status", "update");
        }
        return "redirect:/view?id="+id;
    }

    @RequestMapping(value = "/updateadmin", method = RequestMethod.POST)
    public String UpdateAdmin(RedirectAttributes redir, @RequestParam String id, @ModelAttribute("updateduser") Admin updateduser) {
        Admin user = (Admin)userService.FindById(Integer.parseInt(id)).get();
        if(!user.getEmail().equals(updateduser.getEmail()) && userService.FindByEmail(updateduser.getEmail()).isPresent())
            redir.addFlashAttribute("status", 0);
        else {
            updateduser.setId(user.getId());
            updateduser.setPassword(user.getPassword());
            updateduser.setRole(user.getRole());
            userService.Save(updateduser);
            redir.addFlashAttribute("status", "update");
        }
        return "redirect:/view?id="+id;
    }

    @RequestMapping(value = "/changepassword", method = RequestMethod.GET)
    public String PasswordPage(@RequestParam String id, @AuthenticationPrincipal CustomUserDetails user, Model model) {
        model.addAttribute("user", userService.FindByEmail(user.getEmail()).get());
        model.addAttribute("updateduser", userService.FindById(Integer.parseInt(id)).get());
        model.addAttribute("cartitemlist", cart);
        model.addAttribute("categories", categoryService.getAllCategories());
        return "profilePsw";
    }

    @RequestMapping(value = "/changepassword", method = RequestMethod.POST)
    public String ProfileChangePassword(RedirectAttributes redir, @RequestParam String id, @ModelAttribute("updateduser") User updateduser) {
        User user = userService.FindById(Integer.parseInt(id)).get();
        user.setPassword(updateduser.getPassword());
        userService.Save(user);
        redir.addFlashAttribute("status", "password");
        return "redirect:/view?id="+id;
    }
}
