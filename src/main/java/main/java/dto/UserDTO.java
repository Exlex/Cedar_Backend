
package main.java.dto;

import java.util.List;
import main.java.models.Content;
import main.java.models.Review;
import main.java.models.TVShowSeason;
import main.java.models.User;

public class UserDTO {
	
	private int id;
    private String name;	
    private String email;
    private boolean verified;
    private boolean visible;
    private List<Content> movieWatchlist;
	private List<TVShowSeason> televisionWatchlist;
	private List<Content> blacklist;
    private List<String> roles;	
    private List<User> following;
	private List<Review> reviews;

	public UserDTO(User u) {
		this.id = u.getId();
		this.name = u.getName();
		this.email = u.getEmail();
		this.verified = u.isVerified();
		this.visible = u.isVisible();
		this.movieWatchlist = u.getMovieWatchlist();
		this.televisionWatchlist = u.getTelevisionWatchlist();
		this.blacklist = u.getBlacklist();
		this.roles = u.getRoles();
		this.following = u.getFollowing();
		this.reviews = u.getReviews();
	}

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public boolean isVerified() {
		return verified;
	}

	public void setVerified(boolean verified) {
		this.verified = verified;
	}

	public boolean isVisible() {
		return visible;
	}

	public void setVisible(boolean visible) {
		this.visible = visible;
	}

	public List<Content> getMovieWatchlist() {
		return movieWatchlist;
	}

	public void setMovieWatchlist(List<Content> movieWatchlist) {
		this.movieWatchlist = movieWatchlist;
	}

	public List<TVShowSeason> getTelevisionWatchlist() {
		return televisionWatchlist;
	}

	public void setTelevisionWatchlist(List<TVShowSeason> televisionWatchlist) {
		this.televisionWatchlist = televisionWatchlist;
	}

	public List<Content> getBlacklist() {
		return blacklist;
	}

	public void setBlacklist(List<Content> blacklist) {
		this.blacklist = blacklist;
	}

	public List<String> getRoles() {
		return roles;
	}

	public void setRoles(List<String> roles) {
		this.roles = roles;
	}

	public List<User> getFollowing() {
		return following;
	}

	public void setFollowing(List<User> following) {
		this.following = following;
	}

	public List<Review> getReviews() {
		return reviews;
	}

	public void setReviews(List<Review> reviews) {
		this.reviews = reviews;
	}
	
	
}
