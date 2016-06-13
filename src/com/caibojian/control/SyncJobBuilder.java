package com.caibojian.control;

import org.quartz.JobBuilder;
import org.quartz.JobDataMap;
import org.quartz.JobDetail;

import com.caibojian.entity.SyncTask;
import com.caibojian.sync.SyncJob;


public class SyncJobBuilder extends JobBuilder {
	public static JobDetail newJob(SyncTask syncTask){
		JobDetail job = newJob(SyncJob.class).withIdentity("job"+syncTask.getId(), "group"+syncTask.getId()).build();
		JobDataMap dataMap = job.getJobDataMap();
		dataMap.put("SyncTask", syncTask);
		return job;
	} 
}
