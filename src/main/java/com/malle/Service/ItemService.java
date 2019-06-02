package com.malle.Service;

import com.malle.Dao.ItemDao;
import com.malle.Entity.Item;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Collection;
import java.util.Optional;

@Service
public class ItemService {

    @Autowired
    public ItemDao itemDao;

    public Collection<Item> getAllItems(){
        return itemDao.findAll();
    }

    public Item Save(Item item) {return itemDao.saveAndFlush(item);}

    public Optional<Item> FindById(int id){return itemDao.findById(id);}

    public void DeleteById(int id){itemDao.deleteById(id);}

}
