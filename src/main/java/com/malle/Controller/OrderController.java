package com.malle.Controller;


import com.malle.Entity.CustomUserDetails;
import com.malle.Entity.Item;
import com.malle.Entity.User;
import com.malle.Service.CategoryService;
import com.malle.Service.OrderService;
import com.malle.Service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import java.util.ArrayList;

@Controller
@RequestMapping
public class OrderController {


    @Autowired
    private UserService userService;
    @Autowired
    private CategoryService categoryService;
    @Autowired
    private OrderService orderService;

    public static ArrayList<Item> cart = new ArrayList<>();

    @RequestMapping(value = "/orders", method = RequestMethod.GET)
    public String CartPage(@AuthenticationPrincipal CustomUserDetails user, Model model){
        User userLog = null;
        if(user!=null){
            userLog = userService.FindByEmail(user.getEmail()).get();
            model.addAttribute("user", userLog);
        }
        model.addAttribute("cartitemlist",cart);
        model.addAttribute("total",ItemController.getTotal());
        model.addAttribute("categories",categoryService.getAllCategories());
        model.addAttribute("orderlist", orderService.FindByCustomerid(userLog.getId()));
        return "order";
    }




}