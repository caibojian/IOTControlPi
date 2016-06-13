package com.caibojian.control;

import org.quartz.SimpleScheduleBuilder;
import org.quartz.Trigger;
import org.quartz.TriggerBuilder;

import com.caibojian.entity.SyncTask;


public class SyncTriggerBuilder{
	public static Trigger newTrigger(SyncTask syncTask){		
		int l = Integer.parseInt(syncTask.getTriggerPattern());
		Trigger trigger =  TriggerBuilder.newTrigger().withIdentity("trigger"+syncTask.getId() , "group"+syncTask.getId()).startNow().withSchedule(SimpleScheduleBuilder.simpleSchedule().withIntervalInSeconds(l).repeatForever()).build();
		return trigger;
	}
}
