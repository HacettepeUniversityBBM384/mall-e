package com.malle.Service;


import com.malle.Dao.SubcategoryDao;
import com.malle.Entity.Subcategory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Collection;
import java.util.Optional;

@Service
public class SubcategoryService {

    @Autowired
    public SubcategoryDao subcategoryDao;

    public Collection<Subcategory> getAllSubcategories(){
        return subcategoryDao.findAll();
    }

    public Subcategory Save(Subcategory subcategory) {return subcategoryDao.save(subcategory);}

    public Optional<Subcategory> FindById(int id){return subcategoryDao.findById(id);}

    public void DeleteById(int id){subcategoryDao.deleteById(id);}
}
