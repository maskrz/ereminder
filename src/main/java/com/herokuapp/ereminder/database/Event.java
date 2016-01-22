package com.herokuapp.ereminder.database;

import java.util.Date;
import java.util.Set;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import javax.persistence.Table;

import com.fasterxml.jackson.annotation.JsonManagedReference;

@Entity(name = "event")
@Table
public class Event {

	@Id
	@Column(name = "id", nullable = false)
	@GeneratedValue(strategy = GenerationType.IDENTITY)	
	private Integer id;
	
	@Column(name = "name", nullable = false)
	private String name;
	
	@Column(name = "event_date")
	private Date eventDate;
	
	@OneToMany(fetch = FetchType.EAGER, mappedBy="event")
	@JsonManagedReference
	private Set<Reminder> reminders;

	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public Date getEventDate() {
		return eventDate;
	}

	public void setEventDate(Date eventDate) {
		this.eventDate = eventDate;
	}

	public Set<Reminder> getReminders() {
		return reminders;
	}

	public void setReminders(Set<Reminder> reminders) {
		this.reminders = reminders;
	}
}
