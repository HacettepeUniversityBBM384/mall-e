package com.malle.Service;

import com.malle.Dao.CategoryDao;
import com.malle.Entity.Category;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Collection;
import java.util.Optional;

@Service
public class CategoryService {

    @Autowired
    public CategoryDao categoryDao;

    public Collection<Category> getAllCategories(){
        return categoryDao.findAll();
    }

    public Category Save(Category category) {return categoryDao.save(category);}

    public Optional<Category> FindById(int id){return categoryDao.findById(id);}

    public Optional<Category> FindByName(String name){return categoryDao.findByName(name);}

    public void DeleteById(int id){categoryDao.deleteById(id);}
}
