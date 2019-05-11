package com.malle.Controller;

import com.malle.Entity.Subcategory;
import com.malle.Service.SubcategoryService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping
public class SubcategoryController {
    @Autowired
    private SubcategoryService subcategoryService;

    public Iterable<Subcategory> getAllSubcategories() { return subcategoryService.getAllSubcategories(); }

}
