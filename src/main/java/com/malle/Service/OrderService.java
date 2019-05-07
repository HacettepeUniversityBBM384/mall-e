package com.malle.Service;

import com.malle.Dao.ItemDao;
import com.malle.Dao.OrderDao;
import com.malle.Entity.Item;
import com.malle.Entity.Order;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Collection;
import java.util.Optional;

@Service
public class OrderService {

    @Autowired
    public OrderDao orderDao;

    public Collection<Order> getAllOrders(){
        return orderDao.findAll();
    }

    public Order Save(Order order) {return orderDao.save(order);}

    public Optional<Order> FindById(int id){return orderDao.findById(id);}

    public void DeleteById(int id){orderDao.deleteById(id);}
}
