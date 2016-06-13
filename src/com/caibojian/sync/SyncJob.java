package com.caibojian.sync;

import org.quartz.Job;
import org.quartz.JobDataMap;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.caibojian.entity.SyncTask;


public class SyncJob implements Job {
	private Logger syncLog = LoggerFactory.getLogger(SyncJob.class);
	@Override
	public void execute(JobExecutionContext context) throws JobExecutionException {
		JobDataMap dataMap = context.getJobDetail().getJobDataMap();
		SyncTask syncTask = (SyncTask)dataMap.get("SyncTask");
		if(syncTask.isRunTemp()){
			return;
		}		
		syncTask.setRunTemp(true);
		syncLog.info("{}：任务开始。",syncTask.getName());
		if (syncTask.getStatus()==0){
			//初始化
			syncLog.info("{}：初始化。",syncTask.getName());
//			SyncBuilder sb = new SyncBuilder(syncTask);
//			sb.build();
		}else if(syncTask.getStatus()==1){			
			//同步数据
			syncLog.info("{}：同步数据。",syncTask.getName());
//			SyncCarrier  sc = new SyncCarrier(syncTask);
//			sc.sync();			
		}
		syncLog.info("{}：任务结束。",syncTask.getName());	
		syncTask.setRunTemp(false);
	}
}
