package main.java.controllers;

import java.time.LocalDate;
import java.util.List;
import java.util.NoSuchElementException;
import javax.servlet.http.HttpServletRequest;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import main.java.managers.MovieManager;
import main.java.models.CriticReview;
import main.java.models.Review;
import main.java.models.ReviewForm;
import main.java.models.User;
import main.java.models.UserReview;
import main.java.models.UserRole;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import main.java.managers.ReviewManager;
import main.java.managers.UserManager;
import main.java.models.ErrorCode;
import main.java.models.Movie;
import org.springframework.security.core.context.SecurityContextHolder;


@CrossOrigin("http://localhost:3000")
@RestController
public class ContentController {

	@Autowired
	MovieManager movieManager;
	@Autowired
	ReviewManager reviewManager;
	@Autowired
	UserManager userManager;

	
    @GetMapping("/movie")
    public Movie getMovieInfo(@RequestParam(value="id") int id, HttpServletRequest req) {
        try {
            Movie theMovie = movieManager.findById(id).get();
            return theMovie;
        }
    	catch (Exception e) {
            System.out.println("can't get movie");
    	}

       return null;
    }
	
	@PostMapping("/api/ratecontent")
	public Integer rateContent(@RequestBody ReviewForm form) {
		String email = SecurityContextHolder.getContext().getAuthentication().getName();
		if (email == null) {
			return -1;
		}
		User postingUser = userManager.findByEmail(email);
		Movie movieToRate = movieManager.findById(form.getContent_id()).get();
		Review reviewToPost;
		
		if (postingUser.getRole() == UserRole.ROLE_CRITIC) {
			reviewToPost = new CriticReview(null, movieToRate, postingUser,
				form.getRating(), LocalDate.now(), form.getBody());
		}
		else {
			reviewToPost = new UserReview(movieToRate, postingUser,
				form.getRating(), LocalDate.now(), form.getBody());
		}
		reviewManager.save(reviewToPost);
		movieToRate.addReview(reviewToPost);
		int status = movieToRate.calculateRatings();
		Movie editedMovie = movieManager.save(movieToRate);
		if (editedMovie == null) {
			return -1;
		}
		return status;
	}
	
	@GetMapping("/api/deletereview")
	public ErrorCode deleteReview(@RequestParam(value="id") int id) {
		String email = SecurityContextHolder.getContext().getAuthentication().getName();
		System.out.println(id);
		if (email.equals("anonymousUser")) {
			return ErrorCode.NOTLOGGEDIN;
		}
		User currentUser = userManager.findByEmail(email);
		Review reviewToDelete = null;
		try {
			reviewToDelete = reviewManager.findById(id).get();
		}
		catch (NoSuchElementException e) {
			System.out.println("failed");
			return ErrorCode.DOESNOTEXIST;
		}
	
		if (currentUser.getRole() != UserRole.ROLE_ADMIN && 
			!reviewToDelete.getAuthor().equals(currentUser)) {
			return ErrorCode.INVALIDPERMISSIONS;
		}
		reviewManager.delete(reviewToDelete);
		return ErrorCode.SUCCESS;
	}
	
	@GetMapping("/api/highestratedmovies")
	public List<Movie> displayHighestRatedMovies() {
		return movieManager.findTop10ByOrderByCriticRatingDesc();
	}
}