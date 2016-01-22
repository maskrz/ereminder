package com.herokuapp.ereminder;

import java.text.DateFormat;
import java.util.Date;
import java.util.List;
import java.util.Locale;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.herokuapp.ereminder.database.Event;
import com.herokuapp.ereminder.services.ApplicationService;

/**
 * Handles requests for the application home page.
 */
@Controller
public class HomeController {
	
	@Autowired
	private ApplicationService applicationService;
	
	private static final Logger logger = LoggerFactory.getLogger(HomeController.class);
	
	/**
	 * Simply selects the home view to render by returning its name.
	 */
	@RequestMapping(value = "/", method = RequestMethod.GET)
	public String home(Locale locale, Model model) {
		logger.info("Welcome home! The client locale is {}.", locale);
		
		Date date = new Date();
		DateFormat dateFormat = DateFormat.getDateTimeInstance(DateFormat.LONG, DateFormat.LONG, locale);
		
		String formattedDate = dateFormat.format(date);
		
		model.addAttribute("serverTime", formattedDate );
		
		return "home";
	}
	
	@RequestMapping("/hello")
	public @ResponseBody List<Event> helloWorld() { 
		List<Event> events = getApplicationService().getEvents();
		return events;
	}
	
	@RequestMapping(value="/save", method = RequestMethod.PUT)
	public @ResponseBody String save(@RequestBody String updateString) { 
		System.out.println(updateString);
		getApplicationService().updateEvents(updateString);
		JsonObject o = new JsonParser().parse("{\"result\": \"done\"}").getAsJsonObject();
		return o.toString();
	}
	
	@RequestMapping(value="/add", method = RequestMethod.PUT)
	public @ResponseBody String add(@RequestBody String updateString) { 
		System.out.println(updateString);
		getApplicationService().addEvents(updateString);
		JsonObject o = new JsonParser().parse("{\"result\": \"done\"}").getAsJsonObject();
		return o.toString();
	}
	
	@RequestMapping(value="/send", method = RequestMethod.PUT)
	public @ResponseBody String sendReminders() { 
		getApplicationService().checkReminders();
		JsonObject o = new JsonParser().parse("{\"result\": \"done\"}").getAsJsonObject();
		return o.toString();
	}

	public ApplicationService getApplicationService() {
		return applicationService;
	}

	public void setApplicationService(ApplicationService applicationService) {
		this.applicationService = applicationService;
	}
	
}
