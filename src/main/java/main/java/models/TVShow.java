package main.java.models;

import java.time.LocalDate;
import java.util.List;
import java.util.Set;
import javax.persistence.Entity;

@Entity
public class TVShow extends Content{
	
	private LocalDate nextAirDate;

	public TVShow() {
	}
	
	public TVShow(LocalDate nextAirDate, int content_id, String title, Set<Genre> genres, LocalDate date, double userRating, List<UserReview> userReview, double criticRating, List<CriticReview> criticReview, String description, Celebrity director, String trailerPath, String poster_path, Celebrity writer, String runtime, String studio) {
		super(content_id, title, genres, date, userRating, userReview, criticRating, criticReview, description, director, trailerPath, poster_path, writer, runtime, studio);
		this.nextAirDate = nextAirDate;
	}
	
}
