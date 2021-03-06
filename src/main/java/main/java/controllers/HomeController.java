package main.java.controllers;

import java.awt.image.BufferedImage;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.time.DayOfWeek;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import javax.imageio.ImageIO;
import main.java.managers.BackdropManager;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;
import properties.PropertiesManager;
import main.java.models.Movie;
import main.java.managers.ContentManager;
import main.java.managers.UserManager;
import main.java.models.Content;
import main.java.models.TVShow;
import main.java.models.User;
import main.java.services.BlacklistService;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.RequestParam;

@CrossOrigin("http://localhost:3000")
@RestController
public class HomeController {

	@Autowired
	private UserManager userManager;
	@Autowired
	private ContentManager contentManager;
	@Autowired
	private BackdropManager backdropManager;
	private final PropertiesManager propertiesManager;

	public HomeController() {
		this.propertiesManager = PropertiesManager.getManager();
	}

	@GetMapping("/api/topboxoffice")
	public List<Movie> displayBoxOffice() {
		BlacklistService blacklistService = BlacklistService.getService();
		String email = SecurityContextHolder.getContext().getAuthentication().getName();

		List<Movie> boxOfficeList
				= contentManager.findTop10ByCurrentlyInTheatersTrueOrderByBoxOfficeDesc();
		if (email.equals("anonymousUser")) {
			return boxOfficeList;
		} else {
			User currentUser = userManager.findByEmail(email);
			boxOfficeList = blacklistService.filterMovie(boxOfficeList, currentUser);

		}
		return boxOfficeList;
	}

	@GetMapping("/api/moviesopeningthisweek")
	public List<Movie> displayMoviesOpeningThisWeek() {
		BlacklistService blacklistService = BlacklistService.getService();
		String email = SecurityContextHolder.getContext().getAuthentication().getName();

		DayOfWeek currentDayOfWeek = LocalDate.now().getDayOfWeek();
		int currentDayOfWeekValue = currentDayOfWeek.getValue();
		if (currentDayOfWeek == DayOfWeek.SUNDAY) {
			currentDayOfWeekValue = propertiesManager.getProperty("firstDayOfWeekIndex");
		}
		/* The +1 is to account for zero-indexing of days */
		int daysToSubtract = currentDayOfWeekValue + 1;
		int daysToAdd = propertiesManager.getProperty("numDaysInWeek") - currentDayOfWeekValue;
		LocalDate lastDayOfLastWeek = LocalDate.now().minusDays(daysToSubtract);
		LocalDate firstDayOfNextWeek = LocalDate.now().plusDays(daysToAdd);
		List<Movie> moviesForThisWeek
				= contentManager.findTop10ByDateAfterAndDateBefore(lastDayOfLastWeek, firstDayOfNextWeek);
		if (email.equals("anonymousUser")) {
			return moviesForThisWeek;
		} else {
			User currentUser = userManager.findByEmail(email);
			moviesForThisWeek = blacklistService.filterMovie(moviesForThisWeek, currentUser);
		}
		return moviesForThisWeek;
	}

	@GetMapping("/api/comingsoontotheaters")
	public List<Movie> displayComingSoonToTheaters() {
		BlacklistService blacklistService = BlacklistService.getService();
		String email = SecurityContextHolder.getContext().getAuthentication().getName();

		LocalDate endDate
				= LocalDate.now().plusWeeks(propertiesManager.getProperty("numWeeksForComingSoon"));
		List<Movie> moviesComingSoon
				= contentManager.findTop10ByDateAfterAndDateBefore(LocalDate.now(), endDate);
		if (email.equals("anonymousUser")) {
			return moviesComingSoon;
		} else {
			User currentUser = userManager.findByEmail(email);
			moviesComingSoon = blacklistService.filterMovie(moviesComingSoon, currentUser);
		}
		return moviesComingSoon;
	}

	@GetMapping("/api/newtvtonight")
	public List<TVShow> getNewTVTonight() {
		BlacklistService blacklistService = BlacklistService.getService();
		String email = SecurityContextHolder.getContext().getAuthentication().getName();

		List<TVShow> shows = contentManager.findTop10ByNextAirDate(LocalDate.now());
		if (email.equals("anonymousUser")) {
			return shows;
		} else {
			User currentUser = userManager.findByEmail(email);
			shows = blacklistService.filterTVShow(shows, currentUser);
		}
		return shows;
	}

	@GetMapping(value = "/api/getBackdrop", produces = "image/jpg")
	public byte[] getBackdrop(@RequestParam(value = "id") int id) {
		Content c;
		String backdropPath = "";
		try {
			c = contentManager.findById(id).get();
			backdropPath = backdropManager.findByContent(c).getImageLocation();
		} catch (Exception e) {
			return null;
		}

		String backdropLocation = System.getProperty("user.dir") + "/images/backdrops" + backdropPath;
		try {
			BufferedImage backdrop = ImageIO.read(new File(backdropLocation));
			ByteArrayOutputStream output = new ByteArrayOutputStream();
			ImageIO.write(backdrop, "jpg", output);
			byte[] imageAsBytes = output.toByteArray();
			return imageAsBytes;
		} catch (IOException | IllegalArgumentException e) {
			return null;
		}
	}

	@GetMapping("/api/featuredmovie")
	public Content getFeaturedMovie() {
		BlacklistService blacklistService = BlacklistService.getService();
		String email = SecurityContextHolder.getContext().getAuthentication().getName();

		try {
			List<Integer> ids = new ArrayList<>();
			ids.add(299536);
			List<Content> featured = (List) contentManager.findAllById(ids);
			Random random = new Random();
			if (email.equals("anonymousUser")) {
				return featured.get(random.nextInt(featured.size()));
			} else {
				User currentUser = userManager.findByEmail(email);
				featured = blacklistService.filter(featured, currentUser);
			}

			return featured.get(random.nextInt(featured.size()));
		} catch (Exception e) {
			return null;
		}
	}

}
