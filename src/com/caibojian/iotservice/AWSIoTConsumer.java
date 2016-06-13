package com.caibojian.iotservice;

import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Random;

import org.eclipse.paho.client.mqttv3.IMqttDeliveryToken;
import org.eclipse.paho.client.mqttv3.MqttCallback;
import org.eclipse.paho.client.mqttv3.MqttClient;
import org.eclipse.paho.client.mqttv3.MqttConnectOptions;
import org.eclipse.paho.client.mqttv3.MqttException;
import org.eclipse.paho.client.mqttv3.MqttMessage;
import org.eclipse.paho.client.mqttv3.persist.MemoryPersistence;

import com.caibojian.app.bean.Friends;
import com.caibojian.app.bean.FriendsGroup;
import com.caibojian.app.bean.MyMessage;
import com.caibojian.app.bean.User;
import com.caibojian.app.constants.Constants;
import com.caibojian.app.utils.JsonUtil;
import com.caibojian.pi.ControlGpio;
import com.pi4j.io.gpio.PinState;

public class AWSIoTConsumer implements MqttCallback {

	private static MqttClient consumer = null;

	public AWSIoTConsumer() throws MqttException {
		super();
		AWSIoTConfig awsConfig = null;
		try {
			awsConfig = ConfigLoader.loadConfig();
		} catch (Exception e1) {
			e1.printStackTrace();
		}
		AWSIoTParams params = new AWSIoTParams();
		params.setMessage("Hello AWS Iot"); // default message if no events file
		params.setQos(0);
		params.setAwsConfig(awsConfig);
		consumer = new MqttClient(params.getAwsConfig().getUrl(),
				String.format("clientId %d", new Random().nextInt(100)), new MemoryPersistence());
		consumer.setCallback(this);
		MqttConnectOptions connOpts = new MqttConnectOptions();
		// connOpts.setConnectionTimeout(10);
		// connOpts.setKeepAliveInterval(10);
		connOpts.setCleanSession(true);
		connOpts.setSocketFactory(SslUtil.getSocketFactory(params.getAwsConfig().getRootCA(),
				params.getAwsConfig().getCertificate(), params.getAwsConfig().getPrivateKey(), "password"));
		System.out.println(String.format("Subscribing to broker %s: ", params.getAwsConfig().getUrl()));
		consumer.connect(connOpts);
	}

	public void subscribe(String topic) throws Exception {
		System.out.println("Subscribing to topic :" + topic);
		
		consumer.subscribe(topic);
	}

	public void publish(AWSIoTParams params) throws Exception {
		MqttMessage mqttMessage = null;
		System.out.println(String.format("Publishing %s ", params.getMessage()));
		mqttMessage = new MqttMessage(params.getMessage().getBytes());
		mqttMessage.setQos(params.getQos());
		consumer.publish(params.getTopic(), mqttMessage);
//		consumer.disconnect();
	}

	@Override
	public void messageArrived(String topic, MqttMessage message) throws Exception {
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
		AWSIoTParams params = new AWSIoTParams();

		MyMessage recMsg = null;
		MyMessage sendMsg = null;
		String sendMsgs = null;
		String toTopic = null;
		AWSIoTConfig awsConfig = ConfigLoader.loadConfig();
		params.setQos(1);
		params.setAwsConfig(awsConfig);

		String time = new Timestamp(System.currentTimeMillis()).toString();
		System.out.println(
				"=>Recieved at : " + time + "  ,Topic: " + topic + "  ,Message:" + new String(message.getPayload()));
		System.out.println("");
		switch (topic) {
		case Constants.IOT_TOPOIC_LOGIN:

			sendMsg = new MyMessage();
			recMsg = JsonUtil.fromJson(new String(message.getPayload()), MyMessage.class);
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
				params.setTopic(toTopic);
				params.setMessage(sendMsgs);
				publish(params);
			}
			break;
		case Constants.IOT_TOPOIC_GETFRIENDS:

			System.out.println("执行了-------IOT_TOPOIC_GETFRIENDS");
			sendMsg = new MyMessage();
			recMsg = JsonUtil.fromJson(new String(message.getPayload()), MyMessage.class);
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

			params.setMessage(sendMsgs);
			params.setTopic(toTopic);
			publish(params);
			break;
		case Constants.IOT_TOPOIC_GETFRIENDSGROUP:

			System.out.println("执行了-------IOT_TOPOIC_GETFRIENDSGROUP");
			sendMsg = new MyMessage();
			recMsg = JsonUtil.fromJson(new String(message.getPayload()), MyMessage.class);
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

			params.setMessage(sendMsgs);
			params.setTopic(toTopic);
			publish(params);
			break;
		case Constants.IOT_TOPOIC_LEDCONTROL:
			if("0" .equals(new String(message.getPayload()))){
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

	@Override
	public void connectionLost(Throwable arg0) {
		AWSIoTConfig awsConfig = null;
		try {
			awsConfig = ConfigLoader.loadConfig();
		} catch (Exception e1) {
			e1.printStackTrace();
		}
		AWSIoTParams params = new AWSIoTParams();
		params.setMessage("Hello AWS Iot"); // default message if no events file
		params.setQos(0);
		params.setAwsConfig(awsConfig);
		try {
			consumer = new MqttClient(params.getAwsConfig().getUrl(),
					String.format("clientId %d", new Random().nextInt(100)), new MemoryPersistence());
		} catch (MqttException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		consumer.setCallback(this);
		MqttConnectOptions connOpts = new MqttConnectOptions();
		// connOpts.setConnectionTimeout(10);
		// connOpts.setKeepAliveInterval(10);
		connOpts.setCleanSession(true);
		connOpts.setSocketFactory(SslUtil.getSocketFactory(params.getAwsConfig().getRootCA(),
				params.getAwsConfig().getCertificate(), params.getAwsConfig().getPrivateKey(), "password"));
		System.out.println("Subscribing to topic :" + params.getTopic());
		System.out.println(String.format("Subscribing to broker %s: ", params.getAwsConfig().getUrl()));
		try {
			consumer.connect(connOpts);
		} catch (MqttException e) {
			e.printStackTrace();
		}

	}

	@Override
	public void deliveryComplete(IMqttDeliveryToken arg0) {
		// TODO Auto-generated method stub

	}
	
	public boolean isConnected() throws Exception {
		return consumer.isConnected();
	}

}
