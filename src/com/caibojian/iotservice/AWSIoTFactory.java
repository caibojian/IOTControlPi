package com.caibojian.iotservice;

import org.eclipse.paho.client.mqttv3.MqttException;

public class AWSIoTFactory {

	public AWSIoTConsumer create() throws MqttException {
		AWSIoTConsumer cmd = null;
		cmd = new AWSIoTConsumer();
		return cmd;
	}
}
