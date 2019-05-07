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
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

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

    @RequestMapping(value = "/item/list", method = RequestMethod.GET)
    public String ItemsPage(@AuthenticationPrincipal CustomUserDetails user, Model model) {
        User userauth = userService.FindByEmail(user.getEmail()).get();
        model.addAttribute("user", userauth);
        model.addAttribute("data", "items");
        if(userauth.getRole().equals("ADMIN"))
            model.addAttribute("itemlist", itemService.getAllItems());
        else {
            ArrayList<Item> itemlist = new ArrayList<>();
            for (Item i : getAllItems()) {
                if (i.getShopname().equals(((Seller)userauth).getShopname())) itemlist.add(i);
            }
            model.addAttribute("itemlist", itemlist);
        }
        return "datatable";
    }

    @RequestMapping(value = "/item/view", method = RequestMethod.GET)
    public String ViewItemPage(@RequestParam String id, Model model, @AuthenticationPrincipal CustomUserDetails user) {
        model.addAttribute("authuser", userService.FindByEmail(user.getEmail()).get());
        model.addAttribute("user", userService.FindByEmail(user.getEmail()).get());
        model.addAttribute("item", itemService.FindById(Integer.parseInt(id)).get());
        return "profile";
    }

    @RequestMapping(value = "/item/add", method = RequestMethod.GET)
    public String AddItemPage(@AuthenticationPrincipal CustomUserDetails user, Model model) {
        model.addAttribute("user", userService.FindByEmail(user.getEmail()).get());
        model.addAttribute("newitem", new Item());
        model.addAttribute("data", "item");
        return "add";
    }

    @RequestMapping(value = "/item/add", method = RequestMethod.POST)
    public String AddItem(RedirectAttributes redir, @ModelAttribute("newitem") Item newitem, @AuthenticationPrincipal CustomUserDetails user) {
        newitem.setShopname(((Seller)userService.FindByEmail(user.getEmail()).get()).getShopname());
        itemService.Save(newitem);
        redir.addFlashAttribute("status", "item");
        return "redirect:/item/list";
    }

    @RequestMapping(value = "/item/delete", method = RequestMethod.GET)
    public String DeleteItem(RedirectAttributes redir, @RequestParam String id) {
        itemService.DeleteById(Integer.parseInt(id));
        redir.addFlashAttribute("deleted", "item");
        return "redirect:/item/list";
    }

    @RequestMapping(value = "/item/update", method = RequestMethod.GET)
    public String UpdateItemPage(@RequestParam String id, Model model, @AuthenticationPrincipal CustomUserDetails user) {
        model.addAttribute("user", userService.FindByEmail(user.getEmail()).get());
        model.addAttribute("updateditem", itemService.FindById(Integer.parseInt(id)).get());
        return "profileUpdate";
    }

    @RequestMapping(value = "/item/update", method = RequestMethod.POST)
    public String UpdateItem(RedirectAttributes redir, @RequestParam String id, @AuthenticationPrincipal CustomUserDetails user, @ModelAttribute("updateditem") Item updateditem) {
        Item item = itemService.FindById(Integer.parseInt(id)).get();
        updateditem.setShopname(item.getShopname());
        updateditem.setId(item.getId());
        updateditem.setOrdercount(item.getOrdercount());
        updateditem.setRating(item.getRating());
        itemService.Save(updateditem);
        redir.addFlashAttribute("status", "item");
        return "redirect:/view?id="+id;
    }
}