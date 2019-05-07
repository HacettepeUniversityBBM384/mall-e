package com.malle.Controller;

import com.malle.Entity.*;
import com.malle.Service.ItemService;
import com.malle.Service.OrderService;
import com.malle.Service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.logout.SecurityContextLogoutHandler;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.ArrayList;

@Controller
@RequestMapping
public class ItemController {

    @Autowired
    private ItemService itemService;
    @Autowired
    private UserService userService;

    public Iterable<Item> getAllItems() {
        return itemService.getAllItems();
    }

    @RequestMapping(value = "/items", method = RequestMethod.GET)
    public String ItemsPage(@AuthenticationPrincipal CustomUserDetails user, Model model) {
        if (user != null) {
            model.addAttribute("user", userService.FindByEmail(user.getEmail()).get());
            model.addAttribute("data", "items");
            model.addAttribute("itemlist", getAllItems());
            model.addAttribute("itemid");
        } else model.addAttribute("data", "nop");
        return "datatable";
    }

    @RequestMapping(value = "/additem", method = RequestMethod.GET)
    public String AddItemPage(@AuthenticationPrincipal CustomUserDetails user, Model model) {
        if (user != null) {
            model.addAttribute("user", userService.FindByEmail(user.getEmail()).get());
            model.addAttribute("newitem", new Item());
            model.addAttribute("data", "item");
        }
        return "add";
    }

    @RequestMapping(value = "/additem", method = RequestMethod.POST)
    public String AddItem(@ModelAttribute("newitem") Item newitem, @AuthenticationPrincipal CustomUserDetails user, Model model) {
        newitem.setSellerid(userService.FindByEmail(user.getEmail()).get().getId());
        itemService.Save(newitem);
        model.addAttribute("data", "items");
        model.addAttribute("registered", "item");
        model.addAttribute("user", userService.FindByEmail(user.getEmail()).get());
        return "datatable";
    }

    @RequestMapping(value = "/deleteitem/{id}", method = RequestMethod.GET)
    public String DeleteItem(@PathVariable String id, @AuthenticationPrincipal CustomUserDetails user, Model model, @ModelAttribute("item") Item item) {
        System.out.println(item.getName());
        itemService.DeleteById(Integer.parseInt(id));
        model.addAttribute("data", "items");
        model.addAttribute("deleted", "item");
        model.addAttribute("user", userService.FindByEmail(user.getEmail()).get());
        return "datatable";
    }
}