package com.caibojian;

import java.util.Random;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;

import org.eclipse.paho.client.mqttv3.MqttException;

import com.caibojian.app.constants.Constants;
import com.caibojian.iotservice.ConfigLoader;
import com.caibojian.iotservice.IOTCallback;
import com.caibojian.iotservice.MQTTSubscriber;

public class AutoStartServer extends HttpServlet {
	@Override
	public void init() throws ServletException {
		Thread thread = new Thread(new Runnable() {
			@Override
			public void run() {
//				 Sync.run();
//				AWSIoTFactory iotFactory = new AWSIoTFactory();
//				try {
//					String topic = Constants.IOT_TOPOIC_LOGIN;
//					String topic1 = Constants.IOT_TOPOIC_GETFRIENDS;
//					String topic2 = Constants.IOT_TOPOIC_GETFRIENDSGROUP;
//					String topic3 = Constants.IOT_TOPOIC_LEDCONTROL;
//					AWSIoTConsumer cmd = iotFactory.create();
//					//System.out.println(cmd.isConnected());
//					while(true){
//						System.out.println(cmd.isConnected());
//						Thread.sleep(2000);
//						if(cmd.isConnected()){
//							cmd.subscribe(topic);
//							cmd.subscribe(topic1);
//							cmd.subscribe(topic2);
//							cmd.subscribe(topic3);
//							break;
//						}
//					}
//				} catch (Exception e) {
//					e.printStackTrace();
//				}
				ConfigLoader.getConfig();
				String clientId = "raspberry";
				Random rand = new Random();
				String topic_login = Constants.IOT_TOPOIC_LOGIN;
				String topic_getFriends = Constants.IOT_TOPOIC_GETFRIENDS;
				String topic_getFriendsGroup = Constants.IOT_TOPOIC_GETFRIENDSGROUP;
				String topic_ledControl = Constants.IOT_TOPOIC_LEDCONTROL;
				
				try {
					MQTTSubscriber login = new MQTTSubscriber(
							new IOTCallback(), true, clientId + "-login" + rand.nextInt(100000),topic_login);
					new Thread(login).start();
					MQTTSubscriber getFriends = new MQTTSubscriber(
							new IOTCallback(), true, clientId + "-getFriends" + rand.nextInt(100000),topic_getFriends);
					new Thread(getFriends).start();
					MQTTSubscriber getFriendsGroup = new MQTTSubscriber(
							new IOTCallback(), true, clientId + "-getFriendsGroup" + rand.nextInt(100000),topic_getFriendsGroup);
					new Thread(getFriendsGroup).start();
					MQTTSubscriber ledControl = new MQTTSubscriber(
							new IOTCallback(), true, clientId + "-ledControl" + rand.nextInt(100000),topic_ledControl);
					new Thread(ledControl).start();
				} catch (MqttException e) {
					e.printStackTrace();
				}
			}
		});
		thread.start();
	}

}