package com.malle.Controller;

import com.malle.Entity.CustomUserDetails;
import com.malle.Entity.Review;
import com.malle.Entity.User;
import com.malle.Service.ReviewService;
import com.malle.Service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;


@Controller
@RequestMapping
public class ReviewController {

    @Autowired
    private ReviewService reviewService;

    @Autowired
    private UserService userService;

    @RequestMapping(value = "/item/addreview", method = RequestMethod.POST)
    public String addReview(@RequestParam("rating") String rating, @RequestParam("comment") String comment, @RequestParam("id") String id, @AuthenticationPrincipal CustomUserDetails user) {
        if(user != null){
            User reviewer = userService.FindByEmail(user.getEmail()).get();
            Review review = new Review(reviewer.getId(), Integer.parseInt(id), Integer.parseInt(rating), comment);
            reviewService.Save(review);
            return "redirect:/item/view?id="+id;
        }
        else{
            return "redirect:/login";
        }

    }



}
