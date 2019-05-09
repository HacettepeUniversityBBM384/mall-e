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
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.net.MalformedURLException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.util.ArrayList;

@Controller
@RequestMapping
public class ItemController {

    @Autowired
    private ItemService itemService;
    @Autowired
    private UserService userService;

    public static ArrayList<Item> cart = new ArrayList<>();

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
        Item item = itemService.FindById(Integer.parseInt(id)).get();
        model.addAttribute("status", model.asMap().get("status"));
        model.addAttribute("authuser", userService.FindByEmail(user.getEmail()).get());
        model.addAttribute("user", userService.FindByEmail(user.getEmail()).get());
        model.addAttribute("item", item);
        model.addAttribute("cartitemlist", cart);
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
        File file = new File("");
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

    @RequestMapping(value = "/item/image", method = RequestMethod.GET)
    public String UpdateImagePage(@RequestParam String id, Model model, @AuthenticationPrincipal CustomUserDetails user) {
        model.addAttribute("user", userService.FindByEmail(user.getEmail()).get());
        model.addAttribute("item", itemService.FindById(Integer.parseInt(id)).get());
        return "profileImage";
    }

    @RequestMapping(value = "/item/image", method = RequestMethod.POST)
    public String UpdateImage(RedirectAttributes redir, @RequestParam MultipartFile file, @RequestParam String id) {
        Item item = itemService.FindById(Integer.parseInt(id)).get();
        Seller seller = userService.FindByShopname(item.getShopname()).get();
        String filename = StringUtils.cleanPath(file.getOriginalFilename());
        Path uploadLocation = Paths.get("src/main/resources/static/images/malle").resolve(seller.getShopname());
        try {
            if (file.isEmpty()) { throw new RuntimeException("Failed to store empty file " + filename); }
            if (filename.contains("..")) { throw new RuntimeException("Cannot store file with relative path outside current directory " + filename); }

            try (InputStream inputStream = file.getInputStream()) {
                try { Files.createDirectories(uploadLocation);
                } catch (IOException e) {
                    throw new RuntimeException("Could not initialize storage", e);
                }
                filename = id+'.'+file.getContentType().substring(6);
                Files.copy(inputStream, uploadLocation.resolve(filename), StandardCopyOption.REPLACE_EXISTING);
            }
        } catch (IOException e) {
            throw new RuntimeException("Failed to store file " + filename, e);
        }
        itemService.Save(item);
        redir.addFlashAttribute("status", "item");
        return "redirect:/item/view?id="+id;
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
        return "redirect:/item/view?id="+id;
    }

    @RequestMapping(value = "/item/addtocart", method = RequestMethod.POST)
    public String AddToCart(RedirectAttributes redir, @RequestParam String id, @RequestParam String quantity) {
        Item item = itemService.FindById(Integer.parseInt(id)).get();
        for(int i=0;i<Integer.parseInt(quantity);i++) cart.add(item);
        redir.addFlashAttribute("status", "cart");
        return "redirect:/item/view?id="+id;
    }
}