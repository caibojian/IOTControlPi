package com.caibojian.iotservice;

import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Random;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.eclipse.paho.client.mqttv3.IMqttActionListener;
import org.eclipse.paho.client.mqttv3.IMqttDeliveryToken;
import org.eclipse.paho.client.mqttv3.IMqttToken;
import org.eclipse.paho.client.mqttv3.MqttAsyncClient;
import org.eclipse.paho.client.mqttv3.MqttCallback;
import org.eclipse.paho.client.mqttv3.MqttClient;
import org.eclipse.paho.client.mqttv3.MqttConnectOptions;
import org.eclipse.paho.client.mqttv3.MqttException;
import org.eclipse.paho.client.mqttv3.MqttMessage;
import org.eclipse.paho.client.mqttv3.MqttPersistenceException;
import org.eclipse.paho.client.mqttv3.persist.MemoryPersistence;

import com.caibojian.IOTCallbackInterface;

public class AWSIoTConsumer extends AWSIoTBase{

	private Log log = LogFactory.getLog(MQTTSubscriber.class);
	IOTCallbackInterface callback;
	int state = BEGIN;
	static final int BEGIN = 0;
	static final int CONNECTED = 1;
	static final int SUBSCRIBED = 2;
	static final int DISCONNECTED = 3;
	static final int FINISH = 4;
	static final int ERROR = 5;
	static final int DISCONNECT = 6;
	static final int PUBLISHED = 7;
	public MqttAsyncClient client;
	private  MqttAsyncClient publisher = null;
	String brokerUrl;
	private boolean clean;
	Throwable ex = null;
	Object waiter = new Object();
	boolean donext = false;
	
	private  MqttClient consumer = null;
	private AWSIoTParams params = new AWSIoTParams();
	private MqttConnectOptions conOpt = new MqttConnectOptions();
	
	public AWSIoTConsumer(IOTCallbackInterface callback)  {
		super();
		this.callback = callback;
		AWSIoTConfig awsConfig = null;
			awsConfig = ConfigLoader.loadConfig();
		params.setMessage("Hello AWS Iot"); // default message if no events file
		params.setQos(0);
		params.setAwsConfig(awsConfig);
		conOpt.setCleanSession(true);
		conOpt.setSocketFactory(SslUtil.getSocketFactory(params.getAwsConfig().getRootCA(),
				params.getAwsConfig().getCertificate(), params.getAwsConfig().getPrivateKey(), "password"));
		System.out.println(String.format("Subscribing to broker %s: ", params.getAwsConfig().getUrl()));
		
		try {
			client = new MqttAsyncClient(params.getAwsConfig().getUrl(),
					String.format("clientId %d", new Random().nextInt(100)), new MemoryPersistence());
			client.setCallback(this);
			publisher= new MqttAsyncClient(params.getAwsConfig().getUrl(),
					String.format("clientId %d", new Random().nextInt(100)), new MemoryPersistence());
			publisher.setCallback(this);
		} catch (MqttException e) {
			e.printStackTrace();
		}
	}

	public void subscribe(String topic) {
		try {
			this.subscribe(topic, params.getQos());
			new Thread(new Runnable() {
				public void run() {
				}
			}).start();
		} catch (Throwable e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		System.out.println("Subscribing to topic :"+topic);
	}
	
	public void publish(String topic, String message) {
		try {
			System.out.println("Starting topic: " +topic+"--"+message);
			this.publish(topic, params.getQos(), message.getBytes());
			System.out.println("published："+message);
		} catch (Throwable e) {
			log.error(e.toString());
			e.printStackTrace();
		} finally {
			log.debug("Finishing Thread: " +topic+"--"+message);
		}
	}

	public boolean isConnected() throws Exception {
		return client.isConnected();
	}
	
	/**
	 * Wait for a maximum amount of time for a state change event to occur
	 * 
	 * @param maxTTW
	 *            maximum time to wait in milliseconds
	 * @throws MqttException
	 */
	private void waitForStateChange(int maxTTW) throws MqttException {
		synchronized (waiter) {
			if (!donext) {
				try {
					waiter.wait(maxTTW);
				} catch (InterruptedException e) {
					e.printStackTrace();
					log.error("Timed out");
				}

				if (ex != null) {
					throw (MqttException) ex;
				}
			}
			donext = false;
		}
	}
	
	/**
	 * Subscribe to a topic on an MQTT server Once subscribed this method waits
	 * for the messages to arrive from the server that match the subscription.
	 * It continues listening for messages until the enter key is pressed.
	 * 
	 * @param topicName
	 *            to subscribe to (can be wild carded)
	 * @param qos
	 *            the maximum quality of service to receive messages at for this
	 *            subscription
	 * @throws MqttException
	 */
	public void subscribe(String topicName, int qos) throws Throwable {
		// Use a state machine to decide which step to do next. State change
		// occurs
		// when a notification is received that an MQTT action has completed
		while (state != FINISH) {
			System.out.println("subscribe连接状态-"+topicName+"："+state);
			switch (state) {
			case BEGIN:
				// Connect using a non-blocking connect
				MqttConnector con = new MqttConnector();
				con.doConnect();
				break;
			case CONNECTED:
				// Subscribe using a non-blocking subscribe
				Subscriber sub = new Subscriber();
				sub.doSubscribe(topicName, qos);
				break;
			case SUBSCRIBED:
				break;
			case DISCONNECT:
				Disconnector disc = new Disconnector();
				disc.doDisconnect();
				break;
			case ERROR:
				throw ex;
			case DISCONNECTED:
				state = FINISH;
				donext = true;
				break;
			}

			// if (state != FINISH && state != DISCONNECT) {
			waitForStateChange(10000);
		}
		// }
	}
	
	/**
	 * Publish / send a message to an MQTT server
	 * 
	 * @param topicName
	 *            the name of the topic to publish to
	 * @param qos
	 *            the quality of service to delivery the message at (0,1,2)
	 * @param payload
	 *            the set of bytes to send to the MQTT server
	 * @throws MqttException
	 */
	public void publish(String topicName, int qos, byte[] payload)
			throws Throwable {
		// Use a state machine to decide which step to do next. State change
		// occurs
		// when a notification is received that an MQTT action has completed
		while (state != FINISH) {
			System.out.println("publish连接状态-"+topicName+"："+state);
			switch (state) {
			case BEGIN:
				// Connect using a non-blocking connect
				MqttConnector con = new MqttConnector();
				con.doConnect();
				break;
			case CONNECTED:
				// Publish using a non-blocking publisher
				Publisher pub = new Publisher();
				pub.doPublish(topicName, qos, payload);
				break;
			case PUBLISHED:
				Thread.sleep(2000);
				state = DISCONNECT;
				donext = true;
				break;
			case DISCONNECT:
				Disconnector disc = new Disconnector();
				disc.doDisconnect();
				break;
			case ERROR:
				throw ex;
			case DISCONNECTED:
				state = FINISH;
				donext = true;
				break;
			}

			if (state != FINISH) {
				waitForStateChange(30000);
			}
		}
	}
	
	/****************************************************************/
	/* Methods to implement the MqttCallback interface */
	/****************************************************************/

	/**
	 * @see MqttCallback#connectionLost(Throwable)
	 */
	public void connectionLost(Throwable cause) {
		// Called when the connection to the server has been lost.
		// An application may choose to implement reconnection
		// logic at this point. This sample simply exits.
		log.debug("Connection to " + brokerUrl + " lost!" + cause);
		System.exit(1);
		// TODO: reconnect code
	}

	/**
	 * @see MqttCallback#deliveryComplete(IMqttDeliveryToken)
	 */
	public void deliveryComplete(IMqttDeliveryToken token) {
	}

	/**
	 * @see MqttCallback#messageArrived(String, MqttMessage)
	 */
	public void messageArrived(String topic, MqttMessage message)
			throws MqttException {
		// Called when a message arrives from the server that matches any
		// subscription made by the client
		String stringMessage = new String(message.getPayload());
		log.debug("Message " + new String(stringMessage)
				+ " received from topic " + topic);
		callback.processMessage(stringMessage, topic);
	}
	
	/**
	 * Connect in a non-blocking way and then sit back and wait to be notified
	 * that the action has completed.
	 */
	public class MqttConnector {

		public MqttConnector() {
		}

		public void doConnect() {
			// Connect to the server
			// Get a token and setup an asynchronous listener on the token which
			// will be notified once the connect completes
			log.debug("Connecting to " + brokerUrl);

			IMqttActionListener conListener = new IMqttActionListener() {
				public void onSuccess(IMqttToken asyncActionToken) {
					log.debug("Connected");
					state = CONNECTED;
					carryOn();
				}

				public void onFailure(IMqttToken asyncActionToken,
						Throwable exception) {
					ex = exception;
					state = ERROR;
					log.debug("Connect Failed: " + exception);
					carryOn();
				}

				public void carryOn() {
					synchronized (waiter) {
						donext = true;
						waiter.notifyAll();
					}
				}
			};

			try {
				// Connect using a non-blocking connect
				client.connect(conOpt, "Connect sample context", conListener);
			} catch (MqttException e) {
				// If though it is a non-blocking connect an exception can be
				// thrown if validation of parms fails or other checks such
				// as already connected fail.
				e.printStackTrace();
				log.debug("Connect Failed: " + e);
				state = ERROR;
				donext = true;
				ex = e;
			}
		}
	}
	/**
	 * Subscribe in a non-blocking way and then sit back and wait to be notified
	 * that the action has completed.
	 */
	public class Subscriber {
		public void doSubscribe(String topicName, int qos) {
			// Make a subscription
			// Get a token and setup an asynchronous listener on the token which
			// will be notified once the subscription is in place.
			log.debug("Subscribing to topic \"" + topicName);

			IMqttActionListener subListener = new IMqttActionListener() {
				public void onSuccess(IMqttToken asyncActionToken) {
					log.debug("Subscribe completed");
					state = SUBSCRIBED;
					carryOn();
				}

				public void onFailure(IMqttToken asyncActionToken,
						Throwable exception) {
					ex = exception;
					state = ERROR;
					log.debug("Subscribe failed" + exception);
					carryOn();
				}

				public void carryOn() {
					synchronized (waiter) {
						donext = true;
						waiter.notifyAll();
					}
				}
			};

			try {
				client.subscribe(topicName, qos, "Subscribe sample context",
						subListener);
			} catch (MqttException e) {
				e.printStackTrace();
				state = ERROR;
				donext = true;
				ex = e;
			}
		}
	}
	
	/**
	 * Publish in a non-blocking way and then sit back and wait to be notified
	 * that the action has completed.
	 */
	public class Publisher {
		public void doPublish(String topicName, int qos, byte[] payload) {
			// Send / publish a message to the server
			// Get a token and setup an asynchronous listener on the token which
			// will be notified once the message has been delivered
			MqttMessage message = new MqttMessage(payload);
			message.setQos(qos);

			log.debug("Publishing message " + message + " to topic "
					+ topicName);

			// Setup a listener object to be notified when the publish
			// completes.
			//
			IMqttActionListener pubListener = new IMqttActionListener() {
				public void onSuccess(IMqttToken asyncActionToken) {
					log.debug("Message Published");
					state = PUBLISHED;
					carryOn();
				}

				public void onFailure(IMqttToken asyncActionToken,
						Throwable exception) {
					ex = exception;
					state = ERROR;
					log.debug("Publish failed: " + exception);
					carryOn();
				}

				public void carryOn() {
					synchronized (waiter) {
						donext = true;
						waiter.notifyAll();
					}
				}
			};

			try {
				// Publish the message
				client.publish(topicName, message, "AmazonThing Pub context",
						pubListener);
			} catch (MqttException e) {
				e.printStackTrace();
				state = ERROR;
				donext = true;
				ex = e;
			}
		}
	}
	
	/**
	 * Disconnect in a non-blocking way and then sit back and wait to be
	 * notified that the action has completed.
	 */
	public class Disconnector {
		public void doDisconnect() {
			// Disconnect the client
			log.debug("Disconnecting");

			IMqttActionListener discListener = new IMqttActionListener() {
				public void onSuccess(IMqttToken asyncActionToken) {
					log.debug("Disconnect Completed");
					state = DISCONNECTED;
					carryOn();
				}

				public void onFailure(IMqttToken asyncActionToken,
						Throwable exception) {
					ex = exception;
					state = ERROR;
					log.debug("Disconnect failed: " + exception);
					carryOn();
				}

				public void carryOn() {
					synchronized (waiter) {
						donext = true;
						waiter.notifyAll();
					}
				}
			};

			try {
				client.disconnect("Disconnect sample context", discListener);
			} catch (MqttException e) {
				e.printStackTrace();
				state = ERROR;
				donext = true;
				ex = e;
			}
		}
	}

}
