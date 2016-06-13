package com.caibojian.control;

import java.util.Date;
import java.util.List;

import org.quartz.JobDetail;
import org.quartz.Scheduler;
import org.quartz.SchedulerException;
import org.quartz.SchedulerFactory;
import org.quartz.Trigger;
import org.quartz.TriggerKey;
import org.quartz.impl.StdSchedulerFactory;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;


public class TaskControl {
	private Logger syncLog = LoggerFactory.getLogger(TaskControl.class);
	public void run(){
		
	}
	private void listener(Scheduler sched){
		while (true){
			try{
				Thread.sleep(1000*2);			
				Date doDate = new Date();
			}catch (Exception e) {
				syncLog.error("监听错误�?",e);
			}
		}
	}
}
