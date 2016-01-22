package com.herokuapp.ereminder.services;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.MailSender;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.stereotype.Service;

import com.herokuapp.ereminder.dao.ApplicationDAO;
import com.herokuapp.ereminder.database.Event;
import com.herokuapp.ereminder.database.Reminder;

@Service("applicationService")
public class ApplicationServiceImpl implements ApplicationService {

	@Autowired
	private ApplicationDAO applicationDAO;
	
	@Autowired
	private MailSender mailSender;
	
	@SuppressWarnings("unchecked")
	@Override
	public List<Event> getEvents() {
		return getApplicationDAO().findAll(Event.class);
	}

	@Override
	public void updateEvents(String updateString) {
		String[] events = updateString.substring(0, updateString.length()-1).split(":");
		for(String event : events) {
			handleEvent(event);
		}
	}

	private void handleEvent(String event) {
		String[] reminders = event.split(";");
		Integer eventId = Integer.valueOf(reminders[0]);
		String query = "delete from reminder where event_id = " + eventId;
		getApplicationDAO().runQuery(query);
		
		int i = 0;
		for (String reminder : reminders) {
			if ("true".equals(reminder)) {
				addReminder(i, eventId);
			}
			i++;
		}
	}

	private void addReminder(int i, Integer eventId) {
		Integer daysBefore = 0;
		switch (i) {
		case 1:
			daysBefore = 0;
			break;
		case 2:
			daysBefore = 1;
			break;
		case 3:
			daysBefore = 3;
			break;
		case 4:
			daysBefore = 7;
			break;
		case 5:
			daysBefore = 14;
			break;
		case 6:
			daysBefore = 30;
			break;
		default:
			break;
		}
		Reminder reminder = new Reminder();
		reminder.setDaysBefore(daysBefore);
		Event event = (Event) getApplicationDAO().findById(Event.class, eventId);
		reminder.setEvent(event);
		reminder.setReminderDate(calculateDateBefore(event.getEventDate(), daysBefore));
		getApplicationDAO().add(reminder);
	}

	@Override
	public void addEvents(String updateString) {
		String[] events = updateString.substring(0, updateString.length()-1).split(";");
		Event event = new Event();
		event.setName(events[7]);
		event.setEventDate(getDate("1950-" + events[6]));
		Event added = getApplicationDAO().add(event);
		int i = 1;
		for (String reminder : events) {
			if ("true".equals(reminder)) {
				addReminder(i, added.getId());
			}
			i++;
		}
	}

	private Date getDate(String date) {
		SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd");
		try {
			return simpleDateFormat.parse(date);
		} catch (ParseException e) {
			e.printStackTrace();
		}
		return null;
	}
	
	private Date calculateDateBefore(Date date, int daysBefore) {
		Calendar cal = Calendar.getInstance();
		cal.setTime(date);
		cal.add(Calendar.DAY_OF_YEAR,-daysBefore);
		Date before= cal.getTime();
		return before;
	}
	
	@Override
	public void checkReminders() {
		List<Reminder> reminders = getApplicationDAO().findAll(Reminder.class);
		List<Reminder> remindersToSend = new ArrayList<Reminder>();
		Date today = new Date();
		for (Reminder reminder : reminders) {
			if (equalsDay(today, reminder.getReminderDate())) {
				remindersToSend.add(reminder);
			}
		}
		if (remindersToSend.size() > 0) {
			sendReminders(remindersToSend);
		}
	}

	public boolean equalsDay(Date date1, Date date2) {
		Calendar calendar1 = Calendar.getInstance();
		calendar1.setTime(date1);
		Calendar calendar2 = Calendar.getInstance();
		calendar2.setTime(date2);
		if (calendar1.get(Calendar.DAY_OF_MONTH) != calendar2.get(Calendar.DAY_OF_MONTH)) {
			return false;
		}
		if (calendar1.get(Calendar.MONTH) != calendar2.get(Calendar.MONTH)) {
			return false;
		}
		return true;
	}
	
	private void sendReminders(List<Reminder> remindersToSend) {
		System.out.println("Start sending reminders");
		SimpleMailMessage message = new SimpleMailMessage();
		message.setFrom("ereminder@gmail.com");
		message.setTo("maskrz@gmail.com");
		message.setSubject("Nadchodzace wydarzenia");
		StringBuilder messageBuilder = new StringBuilder();
		messageBuilder.append("Przypomnienie! Pamietaj o nastepujacych wydarzeniach: ");
		messageBuilder.append(System.getProperty("line.separator"));
		for(Reminder reminder : remindersToSend) {
			messageBuilder.append(prepareReminder(reminder));
		}
		message.setText(messageBuilder.toString());
		getMailSender().send(message);		
	}

	private String prepareReminder(Reminder reminder) {
		StringBuilder sb = new StringBuilder();
		sb.append(reminder.getEvent().getName()).append(" odbeda sie w dniu ");
		sb.append(getStringFromatOfDate(reminder.getEvent().getEventDate())).append(", czyli za ");
		sb.append(reminder.getDaysBefore()).append(" dni.");
		sb.append(System.getProperty("line.separator"));
		return sb.toString();
	}

	private String getStringFromatOfDate(Date date) {
		SimpleDateFormat simpleDateFormat = new SimpleDateFormat("dd-MM");
		return simpleDateFormat.format(date);
	}

	public ApplicationDAO getApplicationDAO() {
		return applicationDAO;
	}

	public void setApplicationDAO(ApplicationDAO applicationDAO) {
		this.applicationDAO = applicationDAO;
	}

	public MailSender getMailSender() {
		return mailSender;
	}

	public void setMailSender(MailSender mailSender) {
		this.mailSender = mailSender;
	}

}
