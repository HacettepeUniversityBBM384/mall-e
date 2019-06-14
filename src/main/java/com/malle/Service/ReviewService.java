package com.malle.Service;

import com.malle.Dao.ReviewDao;
import com.malle.Entity.Review;
import com.malle.Entity.Seller;
import com.malle.Entity.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Collection;
import java.util.Optional;

@Service
public class ReviewService {

    @Autowired
    public ReviewDao reviewDao;

    public Collection<Review> getAllItems(){
        return reviewDao.findAll();
    }


    public Review Save(Review review) {return reviewDao.save(review);}

    public Optional<Review> FindByItemid(int itemid){return reviewDao.findByItemid(itemid);}

    public Optional<Review> findById(int id){return reviewDao.findById(id);}

    public void DeleteById(int id){reviewDao.deleteById(id);}
}

