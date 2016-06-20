package com.caibojian.iotservice;

import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

public class ConfigLoader {
	static AWSIoTConfig _awsConfig = new AWSIoTConfig();

	public static AWSIoTConfig loadConfig() {
		return _awsConfig;
	}

	public static void getConfig() {

		ClassLoader classLoader = ConfigLoader.class.getClassLoader();
		InputStream input = classLoader.getResourceAsStream("credentials.properties");
		Properties properties = new Properties();
		try {
			properties.load(input);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		String endpoint = properties.getProperty("awsiot.endpoint");
		String rootCA = properties.getProperty("awsiot.rootCA");
		String privateKey = properties.getProperty("awsiot.privateKey");
		String certificate = properties.getProperty("awsiot.certificate");
		String protocol = properties.getProperty("awsiot.protocol");
		String port = properties.getProperty("awsiot.port");

		_awsConfig.setBroker(endpoint);
		_awsConfig.setProtocol(protocol);
		_awsConfig.setRootCA(rootCA);
		_awsConfig.setPrivateKey(privateKey);
		_awsConfig.setCertificate(certificate);
		_awsConfig.setUrl(String.format("%s%s:%s", protocol, endpoint, port));
	}
}
