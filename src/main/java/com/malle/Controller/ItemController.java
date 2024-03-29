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


import javax.servlet.http.HttpServletRequest;
import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.text.DecimalFormat;
import java.util.*;

import static com.malle.Service.ReviewService.*;

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
    @Autowired
    private ReviewService reviewService;
    @Autowired
    private OrderService orderService;

    public static ArrayList<Item> cart = new ArrayList<>();
    public static ArrayList<Item> compareProducts = new ArrayList<>();

    public static int getTotal(){ int sum=0;for(Item i:cart)sum+=i.getPrice(); return sum;}

    @RequestMapping(value = "/category", method = RequestMethod.GET)
    public String CategoryPage(@RequestParam String name, @AuthenticationPrincipal CustomUserDetails user,Model model) {
        if (user != null) {
            model.addAttribute("user", userService.FindByEmail(user.getEmail()).get());
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
        model.addAttribute("compareitemlist", compareProducts);
        model.addAttribute("categoryname",name);
        model.addAttribute("total", ItemController.getTotal());
        model.addAttribute("categories", categoryService.getAllCategories());
        model.addAttribute("subcategories", subcategoryService.getAllSubcategories());
        model.addAttribute("subcategoryset", subcategoryset);

        return "shop";
    }

    @RequestMapping(value = "/shop", method = RequestMethod.GET)
    public String ShopPage(@RequestParam String name, @AuthenticationPrincipal CustomUserDetails user,Model model) {
        if (user != null) {
            model.addAttribute("user", userService.FindByEmail(user.getEmail()).get());
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
        model.addAttribute("compareitemlist", compareProducts);
        model.addAttribute("total", ItemController.getTotal());
        model.addAttribute("categoryname",name);
        model.addAttribute("categories", categoryService.getAllCategories());
        model.addAttribute("subcategories", subcategoryService.getAllSubcategories());
        model.addAttribute("subcategoryset", subcategoryset);
        return "shop";
    }

    @RequestMapping(value = "/cart", method = RequestMethod.GET)
    public String CartPage(@AuthenticationPrincipal CustomUserDetails user, Model model){
        if(user!=null){
            model.addAttribute("user", userService.FindByEmail(user.getEmail()).get());
        }
        model.addAttribute("cartitemlist",cart);
        model.addAttribute("compareitemlist", compareProducts);
        model.addAttribute("total",ItemController.getTotal());
        model.addAttribute("categories",categoryService.getAllCategories());
        return "cart";
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
        model.addAttribute("subcategories", subcategoryService.getAllSubcategories());
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

        ArrayList<Review> commentlist = new ArrayList<Review>();
        double totalrating = 0;
        for (Review i: reviewService.getAllItems())
            if (i.getItemid() == item.getId()){
                commentlist.add((Review)i);
                totalrating = totalrating + i.getRating();
            }
        String avrat = new DecimalFormat("#.##").format(totalrating/commentlist.size());
        model.addAttribute("averagerating",avrat);
        model.addAttribute("commentlist",commentlist);
        model.addAttribute("cartitemlist", cart);
        model.addAttribute("compareitemlist", compareProducts);
        model.addAttribute("total", ItemController.getTotal());
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
        model.addAttribute("subcategories", subcategoryService.getAllSubcategories());
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
        model.addAttribute("subcategories", subcategoryService.getAllSubcategories());
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
        Path uploadLocationtarget = Paths.get("target/classes/static/images/malle").resolve(seller.getShopname());
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
        try {
            try (InputStream inputStream = file.getInputStream()) {
                try { Files.createDirectories(uploadLocationtarget);
                } catch (IOException e) {
                    throw new RuntimeException("Could not initialize storage", e);
                }
                filename = id+'.'+file.getContentType().substring(6);
                Files.copy(inputStream, uploadLocationtarget.resolve(filename), StandardCopyOption.REPLACE_EXISTING);
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
    public String AddToCartProfile(RedirectAttributes redir, @RequestParam String id, @RequestParam String quantity) {
        Item item = itemService.FindById(Integer.parseInt(id)).get();
        for(int i=0;i<Integer.parseInt(quantity);i++) cart.add(item);
        redir.addFlashAttribute("status", "cart");
        return "redirect:/item/view?id="+id;
    }

    @RequestMapping(value = "/item/removefromcart", method = RequestMethod.GET)
    public String RemoveFromCart(RedirectAttributes redir, @RequestParam int id) {
        int index = 0;
        for(Item i: cart){
            if(i.getId()==id) break;
            index++;
        }
        cart.remove(index);
        redir.addFlashAttribute("status", "cart");
        return "redirect:/cart";
    }

    @RequestMapping(value = "/item/addtocart", method = RequestMethod.GET)
    public String AddToCartShop(RedirectAttributes redir, @RequestParam String id, HttpServletRequest request) {
        Item item = itemService.FindById(Integer.parseInt(id)).get();
        cart.add(item);
        redir.addFlashAttribute("status", "cart");
        return "redirect:"+request.getHeader("Referer");
    }

    @RequestMapping(value = "/item/addtocompare", method = RequestMethod.POST)
    public String AddToCompare(RedirectAttributes redir, @RequestParam String id, HttpServletRequest request) {
        if (compareProducts.size() < 2) {
            Item item = itemService.FindById(Integer.parseInt(id)).get();
            compareProducts.add(item);
        }else if (compareProducts.size() >= 2){
            redir.addFlashAttribute("compare", "error");
        }
        redir.addFlashAttribute("status", "compare");
        return "redirect:"+request.getHeader("Referer");
    }

    @RequestMapping(value = "/item/addtocompare", method = RequestMethod.GET)
    public String AddToCompareGet(RedirectAttributes redir, @RequestParam String id, HttpServletRequest request) {
        if (compareProducts.size() < 2) {
            Item item = itemService.FindById(Integer.parseInt(id)).get();
            compareProducts.add(item);
        }else if (compareProducts.size() >= 2){
            redir.addFlashAttribute("compare", "error");
        }
        redir.addFlashAttribute("status", "compare");
        return "redirect:"+request.getHeader("Referer");
    }

    @RequestMapping(value = "/compare", method = RequestMethod.GET)
    public String CompareItemPage(@RequestParam String id1, @RequestParam String id2,  Model model, @AuthenticationPrincipal CustomUserDetails user) {
        Item item = itemService.FindById(Integer.parseInt(id1)).get();
        model.addAttribute("item1", item);
        Item item2 = itemService.FindById(Integer.parseInt(id2)).get();
        model.addAttribute("item2", item2);
        if(user!=null) {
            model.addAttribute("status", model.asMap().get("status"));
            model.addAttribute("authuser", userService.FindByEmail(user.getEmail()).get());
            model.addAttribute("user", userService.FindByEmail(user.getEmail()).get());
        }

        ArrayList<Review> commentlistItem1 = new ArrayList<Review>();
        double totalratingItem1 = 0;
        for (Review i: reviewService.getAllItems())
            if (i.getItemid() == item.getId()){
                commentlistItem1.add((Review)i);
                totalratingItem1 = totalratingItem1 + i.getRating();
            }

        ArrayList<Review> commentlistItem2 = new ArrayList<Review>();
        double totalratingItem2 = 0;
        for (Review i: reviewService.getAllItems())
            if (i.getItemid() == item.getId()){
                commentlistItem2.add((Review)i);
                totalratingItem2 = totalratingItem2 + i.getRating();
            }
        String avrat = new DecimalFormat("#.##").format(totalratingItem1/commentlistItem1.size());
        String avrat2 = new DecimalFormat("#.##").format(totalratingItem2/commentlistItem2.size());
        model.addAttribute("averagerating",avrat);
        model.addAttribute("averagerating2",avrat2);
        model.addAttribute("cartitemlist", cart);
        model.addAttribute("compareitemlist", compareProducts);
        model.addAttribute("total", ItemController.getTotal());
        model.addAttribute("subcategory", subcategoryService.FindById(item.getSubcategoryid()).get());
        model.addAttribute("subcategory2", subcategoryService.FindById(item2.getSubcategoryid()).get());
        model.addAttribute("categories", categoryService.getAllCategories());
        model.addAttribute("total", getTotal());
        return "compare";
    }

    @RequestMapping(value = "/item/removecompare", method = RequestMethod.POST)
    public String RemoveComparePost(RedirectAttributes redir, @RequestParam String id, Model model, HttpServletRequest request) {
        redir.addFlashAttribute("status", "compare");
        model.addAttribute("compareitemlist", compareProducts);
        return "redirect:"+request.getHeader("Referer");
    }

    @RequestMapping(value = "/item/removecompare", method = RequestMethod.GET)
    public String RemoveCompareGet(RedirectAttributes redir, @RequestParam String id, Model model, HttpServletRequest request) {
        Item item = itemService.FindById(Integer.parseInt(id)).get();
        int itemIndex = compareProducts.indexOf(item);
        if (!compareProducts.isEmpty())
            compareProducts.remove(itemIndex);
        redir.addFlashAttribute("status", "compare");
        model.addAttribute("compareitemlist", compareProducts);
        return "redirect:"+request.getHeader("Referer");
    }

    @RequestMapping(value = "/payment", method = RequestMethod.GET)
    public String CheckoutPage(@AuthenticationPrincipal CustomUserDetails user, Model model){
        if(user!=null){
            model.addAttribute("user", userService.FindByEmail(user.getEmail()).get());
        }
        model.addAttribute("cartitemlist",cart);
        model.addAttribute("checkout","False");
        model.addAttribute("compareitemlist", compareProducts);
        model.addAttribute("total",ItemController.getTotal());
        model.addAttribute("categories",categoryService.getAllCategories());
        model.addAttribute("order", new Order());
        return "payment";
    }

    @RequestMapping(value = "/payment", method = RequestMethod.POST)
    public String CheckoutPostPage(@AuthenticationPrincipal CustomUserDetails user, Model model,@ModelAttribute(value="order") Order orderNew){
        if(user!=null){
            model.addAttribute("user", userService.FindByEmail(user.getEmail()).get());
        }

        Random rand = new Random();

        for (int i = 0; i<cart.size(); i++){
            Order order = new Order();
            order.setItem(cart.get(i));
            order.setCustomerid(user.getId());
            Date today = new Date();
            order.setDate(today);
            order.setItemid(cart.get(i).getId());
            order.setStatus("Waiting Payment");
            order.setPayoff(order.getItem().getPrice());
            order.setOrderNo(rand.nextInt(100000000));
            orderService.Save(order);
        }
        cart.clear();
        model.addAttribute("cartitemlist",cart);
        model.addAttribute("checkout","True");
        model.addAttribute("compareitemlist", compareProducts);
        model.addAttribute("total",ItemController.getTotal());
        model.addAttribute("categories",categoryService.getAllCategories());
        return "payment";
    }

}