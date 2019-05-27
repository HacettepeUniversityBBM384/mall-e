package com.malle.Service;

import com.malle.Dao.ReviewDao;
import com.malle.Entity.Review;
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

    public Optional<Review> FindById(int id){return reviewDao.findById(id);}

    public void DeleteById(int id){reviewDao.deleteById(id);}
}

