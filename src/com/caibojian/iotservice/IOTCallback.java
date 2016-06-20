/** 
 * Copyright 2015 Amazon.com, Inc. or its affiliates. All Rights Reserved.
 *
 * Licensed under the Apache License, Version 2.0 (the "License"). You may not use 
 * this file except in compliance with the License. A copy of the License is located at
 *
 *     http://aws.amazon.com/apache2.0/
 *
 * or in the "license" file accompanying this file. This file is distributed on an "AS IS"
 * BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied. See the 
 * License for the specific language governing permissions and limitations under the License.
 */

package com.caibojian.iotservice;

import java.awt.Color;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Random;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.eclipse.paho.client.mqttv3.MqttException;

import com.caibojian.IOTCallbackInterface;
import com.caibojian.app.bean.Friends;
import com.caibojian.app.bean.FriendsGroup;
import com.caibojian.app.bean.MyMessage;
import com.caibojian.app.bean.User;
import com.caibojian.app.constants.Constants;
import com.caibojian.app.utils.JsonUtil;
import com.caibojian.pi.ControlGpio;
import com.pi4j.io.gpio.PinState;

/**
 * Implementation of the Callback Interface for handling Delta notifications.
 *
 * @author Fabio Silva (silfabio@amazon.com)
 */
public class IOTCallback implements IOTCallbackInterface {
	private Log log = LogFactory.getLog(IOTCallback.class);
	String deleteMessage = "{ \"state\": null }";
	String clientId = "raspberry";
	Random rand = new Random();

	public void processMessage(String message, String topic) {
		System.out.println(message);
		User user1 = new User();
		user1.setAccount("caibojian");
		user1.setAge(20);
		user1.setId(0);
		user1.setName("帅哥");

		User user2 = new User();
		user2.setAccount("test");
		user2.setAge(20);
		user2.setId(1);
		user2.setName("美女");

		MyMessage recMsg = null;
		MyMessage sendMsg = null;
		String sendMsgs = null;
		String toTopic = null;
		

		String time = new Timestamp(System.currentTimeMillis()).toString();
		System.out.println(
				"=>Recieved at : " + time + "  ,Topic: " + topic + "  ,Message:" + message);
		System.out.println("");
		switch (topic) {
		case Constants.IOT_TOPOIC_LOGIN:

			sendMsg = new MyMessage();
			recMsg = JsonUtil.fromJson(new String(message), MyMessage.class);
			if (Constants.MYMSG_TYPE_LOGIN_REQ == recMsg.getMsgType()) {
				User user = JsonUtil.fromJson(recMsg.getContent(), User.class);
				// ====后台处理逻辑=====
				System.out.println(user.getAccount() + "====" + user.getPassword());
				if ("caibojian".equals(user.getAccount())) {
					user1.setOnline(true);
					sendMsg.setContent(JsonUtil.toJson(user1));
				} else if ("test".equals(user.getAccount())) {
					user2.setOnline(true);
					sendMsg.setContent(JsonUtil.toJson(user2));
				} else {
					user.setOnline(false);
				}
				sendMsg.setToId(user.getUuid());

				sendMsg.setDate(new Date());
				sendMsg.setFromId("system");
				sendMsg.setMsgType(Constants.MYMSG_TYPE_LOGIN_RESP);
				sendMsgs = JsonUtil.toJson(sendMsg);
				// ====后台处理逻辑=====

				toTopic = user.getUuid();
				try {
					MQTTPublisher loginPublisher = new MQTTPublisher(
							toTopic, sendMsgs, clientId+ "-MYMSG_TYPE_LOGIN_RESP" + rand.nextInt(100000), true);
					new Thread(loginPublisher).start();
					log.info(sendMsgs);
				} catch (MqttException e) {
					e.printStackTrace();
				}
			}
			break;
		case Constants.IOT_TOPOIC_GETFRIENDS:

			System.out.println("执行了-------IOT_TOPOIC_GETFRIENDS");
			sendMsg = new MyMessage();
			recMsg = JsonUtil.fromJson(new String(message), MyMessage.class);
			List<Friends> friends = new ArrayList<Friends>();
			// for (int i=0; i<100; i++){
			// Friends friend = new Friends();
			// friend.setId(i);
			// friend.setAge(i);
			// friend.setOnline(true);
			// friend.setUserId(i);
			// friend.setName("名字是"+i);
			// Random random = new Random();
			// int a=random.nextInt(5);
			// friend.setFriendsGroupId(a);
			// friends.add(friend);
			// }
			Friends friend1 = new Friends();
			friend1.setId(1);
			friend1.setAge(20);
			friend1.setOnline(true);
			friend1.setUserId(1);
			friend1.setName("大美女");
			Random random = new Random();
			int a = random.nextInt(5);
			friend1.setFriendsGroupId(a);

			Friends friend0 = new Friends();
			friend0.setId(0);
			friend0.setAge(20);
			friend0.setOnline(true);
			friend0.setUserId(0);
			friend0.setName("大帅哥");
			friend0.setFriendsGroupId(a);

			if ("0".equals(recMsg.getFromId())) {
				friends.add(friend1);
			} else if ("1".equals(recMsg.getFromId())) {
				friends.add(friend0);
			}

			sendMsg.setToId(recMsg.getFromId());
			sendMsg.setContent(JsonUtil.toJson(friends));
			sendMsg.setDate(new Date());
			sendMsg.setFromId("system");
			sendMsg.setMsgType(Constants.MYMSG_TYPE_GETFRIENDS_RESP);
			sendMsgs = JsonUtil.toJson(sendMsg);

			toTopic = recMsg.getFromId();

			try {
				MQTTPublisher getFriendsPublisher = new MQTTPublisher(
						toTopic, sendMsgs, clientId+ "-MYMSG_TYPE_LOGIN_RESP" + rand.nextInt(100000), true);
				new Thread(getFriendsPublisher).start();
				log.info(sendMsgs);
			} catch (MqttException e) {
				e.printStackTrace();
			}
			break;
		case Constants.IOT_TOPOIC_GETFRIENDSGROUP:

			System.out.println("执行了-------IOT_TOPOIC_GETFRIENDSGROUP");
			sendMsg = new MyMessage();
			recMsg = JsonUtil.fromJson(new String(message), MyMessage.class);
			List<FriendsGroup> friendsGroups = new ArrayList<FriendsGroup>();
			for (int i = 0; i < 5; i++) {
				FriendsGroup friendsGroup = new FriendsGroup();
				friendsGroup.setId(i);
				friendsGroup.setName("朋友分组" + i);
				friendsGroup.setPosition(i);
				friendsGroups.add(friendsGroup);
			}

			sendMsg.setToId(recMsg.getFromId());
			sendMsg.setContent(JsonUtil.toJson(friendsGroups));
			sendMsg.setDate(new Date());
			sendMsg.setFromId("system");
			sendMsg.setMsgType(Constants.MYMSG_TYPE_GETFRIENDSGROUP_RESP);
			sendMsgs = JsonUtil.toJson(sendMsg);

			toTopic = recMsg.getFromId();

			try {
				MQTTPublisher getFriendsGroupPublisher = new MQTTPublisher(
						toTopic, sendMsgs, clientId+ "-MYMSG_TYPE_LOGIN_RESP" + rand.nextInt(100000), true);
				new Thread(getFriendsGroupPublisher).start();
				log.info(sendMsgs);
			} catch (MqttException e) {
				e.printStackTrace();
			}
			break;
		case Constants.IOT_TOPOIC_LEDCONTROL:
			System.out.println("执行了-------IOT_TOPOIC_LEDCONTROL");
			sendMsg = new MyMessage();
			
			recMsg = JsonUtil.fromJson(new String(message), MyMessage.class);
			if("0" .equals(recMsg.getContent())){
				PinState state = ControlGpio.getState();
				if(state ==PinState.LOW ){
					ControlGpio.ledOn();
				}
			}else{
				PinState state = ControlGpio.getState();
				if(state ==PinState.HIGH ){
					ControlGpio.ledOff();
				}
			}
		default:
			break;
		}
	}
}