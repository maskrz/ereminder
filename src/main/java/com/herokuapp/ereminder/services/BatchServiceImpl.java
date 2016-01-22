package com.herokuapp.ereminder.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

@Service("batchService")
public class BatchServiceImpl implements BatchService {

	@Autowired
	private ApplicationService applicationService;
	
	@Scheduled(cron = "0 0 7 * * *")
	@Override
	public void sendReminders() {
		getApplicationService().checkReminders();
		
	}

	public ApplicationService getApplicationService() {
		return applicationService;
	}

	public void setApplicationService(ApplicationService applicationService) {
		this.applicationService = applicationService;
	}

}
