package com.malle.Controller;

import com.malle.Entity.Category;
import com.malle.Service.CategoryService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping
public class CategoryController {
    @Autowired
    private CategoryService categoryService;

    public Iterable<Category> getAllCategories() { return categoryService.getAllCategories(); }

}
