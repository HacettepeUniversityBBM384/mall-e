package com.malle.Controller;

import com.malle.Entity.CustomUserDetails;
import com.malle.Entity.Item;
import com.malle.Entity.Order;
import com.malle.Service.ItemService;
import com.malle.Service.OrderService;
import com.malle.Service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

@Controller
@RequestMapping
public class OrderController {

    @Autowired
    private OrderService orderService;
    @Autowired
    private UserService userService;

    public Iterable<Order> getAllOrders(){
        return orderService.getAllOrders();
    }

    @RequestMapping(value = "/orders", method = RequestMethod.GET)
    public String OrdersPage(@AuthenticationPrincipal CustomUserDetails user, Model model) {
        if (user != null) {
            model.addAttribute("user", userService.FindByEmail(user.getEmail()).get());
            model.addAttribute("data", "orders");
            model.addAttribute("orderlist",getAllOrders());
        }
        else model.addAttribute("data","nop");
        return "datatable";
    }

}