package main.java.models;

import com.fasterxml.jackson.annotation.JsonIgnore;
import java.time.LocalDateTime;
import java.util.List;
import java.util.ArrayList;
import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Inheritance;
import javax.persistence.InheritanceType;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.Transient;

@Entity
@Inheritance(strategy = InheritanceType.TABLE_PER_CLASS)
public abstract class Review {

	@Id @GeneratedValue(strategy=GenerationType.AUTO)
	private int review_id;
	@ManyToOne(targetEntity=User.class)
	@JoinColumn(name = "author_id")
	private User author;
	private int rating;
	private LocalDateTime date;
	private String body;	
	@JsonIgnore
	@OneToMany(targetEntity=ReviewReport.class, mappedBy="review",  cascade = CascadeType.ALL)
	private List<ReviewReport> reports;

	public Review() {
		reports = new ArrayList<>();
	}
	
	public Review(User author, int rating, LocalDateTime date, String body) {
		reports = new ArrayList<>();
		this.author = author;
		this.rating = rating;
		this.date = date;
		this.body = body;
	}

	public int getReview_id() {
		return review_id;
	}

	public void setReview_id(int review_id) {
		this.review_id = review_id;
	}

	public User getAuthor() {
		return author;
	}

	public void setAuthor(User author) {
		this.author = author;
	}

	public int getRating() {
		return rating;
	}

	public void setRating(int rating) {
		this.rating = rating;
	}

	public LocalDateTime getDate() {
		return date;
	}

	public void setDate(LocalDateTime date) {
		this.date = date;
	}

	public String getBody() {
		return body;
	}

	public void setBody(String body) {
		this.body = body;
	}

	public List<ReviewReport> getReports() {
		return reports;
	}

	public void setReports(List<ReviewReport> reports) {
		this.reports = reports;
	}
	
	public void addReport(ReviewReport r) {
		reports.add(r);
	}
	
	
	
}