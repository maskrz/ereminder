package com.herokuapp.ereminder.services;

import java.util.List;

import org.springframework.transaction.annotation.Transactional;

import com.herokuapp.ereminder.database.Event;

public interface ApplicationService {

	@Transactional
	public List<Event> getEvents();

	@Transactional
	public void updateEvents(String updateString);

	@Transactional
	public void addEvents(String updateString);
	
	@Transactional
	public void checkReminders();

}
