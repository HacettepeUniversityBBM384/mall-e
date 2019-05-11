package com.malle.Controller;

import com.malle.Entity.*;
import com.malle.Service.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;


import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.Set;

@Controller
@RequestMapping
public class ItemController {

    @Autowired
    private ItemService itemService;
    @Autowired
    private UserService userService;
    @Autowired
    private CategoryService categoryService;
    @Autowired
    private SubcategoryService subcategoryService;

    public static ArrayList<Item> cart = new ArrayList<>();

    public int getTotal(){ int sum=0;for(Item i:cart)sum+=i.getPrice(); return sum;}

    @RequestMapping(value = "/category", method = RequestMethod.GET)
    public String CategoryPage(@RequestParam String name, @AuthenticationPrincipal CustomUserDetails user,Model model) {
        if (user != null) {
            model.addAttribute("user", userService.FindByEmail(user.getEmail()).get());
            model.addAttribute("authuser", userService.FindByEmail(user.getEmail()).get());
        }
        ArrayList<Item> itemlist = new ArrayList<>();
        HashSet<Subcategory> subcategoryset = new HashSet<>();
        for (Item i : itemService.getAllItems()) {
            if (subcategoryService.FindById(i.getSubcategoryid()).get().getCategoryname().equals(name)) {
                itemlist.add(i);
                subcategoryset.add(subcategoryService.FindById(i.getSubcategoryid()).get());
            }
        }
        model.addAttribute("itemlist", itemlist);
        model.addAttribute("cartitemlist", cart);
        model.addAttribute("categoryname",name);
        model.addAttribute("categories", categoryService.getAllCategories());
        model.addAttribute("subcategories", subcategoryService.getAllSubcategories());
        model.addAttribute("subcategoryset", subcategoryset);

        return "shop";
    }

    @RequestMapping(value = "/shop", method = RequestMethod.GET)
    public String ShopPage(@RequestParam String name, @AuthenticationPrincipal CustomUserDetails user,Model model) {
        if (user != null) {
            model.addAttribute("user", userService.FindByEmail(user.getEmail()).get());
            model.addAttribute("authuser", userService.FindByEmail(user.getEmail()).get());
        }
        ArrayList<Item> itemlist = new ArrayList<>();
        HashSet<Subcategory> subcategoryset = new HashSet<>();
        for (Item i : itemService.getAllItems()) {
            if (i.getShopname().equals(name)) {
                itemlist.add(i);
                subcategoryset.add(subcategoryService.FindById(i.getSubcategoryid()).get());
            }
        }
        model.addAttribute("itemlist", itemlist);
        model.addAttribute("cartitemlist", cart);
        model.addAttribute("categoryname",name);
        model.addAttribute("categories", categoryService.getAllCategories());
        model.addAttribute("subcategories", subcategoryService.getAllSubcategories());
        model.addAttribute("subcategoryset", subcategoryset);
        return "shop";
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
            for (Item i : itemService.getAllItems()) {
                if (i.getShopname().equals(((Seller)userauth).getShopname())) itemlist.add(i);
            }
            model.addAttribute("itemlist", itemlist);
        }
        model.addAttribute("categories", categoryService.getAllCategories());
        return "datatable";
    }

    @RequestMapping(value = "/item/view", method = RequestMethod.GET)
    public String ViewItemPage(@RequestParam String id, Model model, @AuthenticationPrincipal CustomUserDetails user) {
        Item item = itemService.FindById(Integer.parseInt(id)).get();
        model.addAttribute("item", item);
        if(user!=null) {
            model.addAttribute("status", model.asMap().get("status"));
            model.addAttribute("authuser", userService.FindByEmail(user.getEmail()).get());
            model.addAttribute("user", userService.FindByEmail(user.getEmail()).get());
        }
        model.addAttribute("cartitemlist", cart);
        model.addAttribute("subcategory", subcategoryService.FindById(item.getSubcategoryid()).get());
        model.addAttribute("categories", categoryService.getAllCategories());
        model.addAttribute("total", getTotal());
        return "profile";
    }

    @RequestMapping(value = "/item/add", method = RequestMethod.GET)
    public String AddItemPage(@AuthenticationPrincipal CustomUserDetails user, Model model) {
        model.addAttribute("user", userService.FindByEmail(user.getEmail()).get());
        model.addAttribute("newitem", new Item());
        model.addAttribute("data", "item");
        model.addAttribute("categories", categoryService.getAllCategories());
        model.addAttribute("subcategorylist", subcategoryService.getAllSubcategories());
        return "add";
    }

    @RequestMapping(value = "/item/add", method = RequestMethod.POST)
    public String AddItem(RedirectAttributes redir, @ModelAttribute("newitem") Item newitem, @AuthenticationPrincipal CustomUserDetails user) {
        newitem.setShopname(((Seller)userService.FindByEmail(user.getEmail()).get()).getShopname());
        newitem.setRating(5);
        Item item = itemService.Save(newitem);
        redir.addFlashAttribute("status", "item");
        return "redirect:/item/image?id="+item.getId();
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
        model.addAttribute("categories", categoryService.getAllCategories());
        model.addAttribute("subcategorylist", subcategoryService.getAllSubcategories());
        return "profileUpdate";
    }

    @RequestMapping(value = "/item/image", method = RequestMethod.GET)
    public String UpdateImagePage(@RequestParam String id, Model model, @AuthenticationPrincipal CustomUserDetails user) {
        model.addAttribute("user", userService.FindByEmail(user.getEmail()).get());
        model.addAttribute("item", itemService.FindById(Integer.parseInt(id)).get());
        model.addAttribute("categories", categoryService.getAllCategories());
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
    public String UpdateItem(RedirectAttributes redir, @RequestParam String id, @ModelAttribute("updateditem") Item updateditem) {
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

    @RequestMapping(value = "/item/addtocart", method = RequestMethod.GET)
    public String AddToCartShop(RedirectAttributes redir, @RequestParam String id, @RequestParam String quantity) {
        Item item = itemService.FindById(Integer.parseInt(id)).get();
        for(int i=0;i<Integer.parseInt(quantity);i++) cart.add(item);
        redir.addFlashAttribute("status", "cart");
        return "redirect:/item/view?id="+id;
    }
}