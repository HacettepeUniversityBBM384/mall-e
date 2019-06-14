package com.malle.Service;

import com.malle.Dao.ItemDao;
import com.malle.Dao.OrderDao;
import com.malle.Entity.Order;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import static java.util.stream.Collectors.*;
import static java.util.Map.Entry.*;
import java.util.*;
import java.util.stream.Collectors;

@Service
public class OrderService {

    @Autowired
    public OrderDao orderDao;

    @Autowired
    public ItemDao itemDao;

    public Collection<Order> getAllOrders(){
        return orderDao.findAll();
    }

    public Order Save(Order order) {return orderDao.save(order);}

    public Optional<Order> FindById(int id){return orderDao.findById(id);}

    public void DeleteById(int id){orderDao.deleteById(id);}

    public TreeMap<Date, List<Order>> FindByCustomerid(int customerid){

        List<Order> orderList = orderDao.findAllByCustomerid(customerid);
        Map<Date, List<Order>> groupByDateMap =
                orderList.stream().collect(Collectors.groupingBy(Order::getDate)
                );

        groupByDateMap.forEach((k, v) -> v.forEach( o -> o.setItem(itemDao.findById(o.getId()).get())));

        TreeMap<Date, List<Order> > finalMap = new TreeMap<Date, List<Order>>(new Comparator<Date>() {
            @Override
            public int compare(Date o1, Date o2) {
                return o2.compareTo(o1);
            }

        });

        //Sort a map and add to finalMap
        finalMap.putAll(groupByDateMap);

        return finalMap;
    }
}
