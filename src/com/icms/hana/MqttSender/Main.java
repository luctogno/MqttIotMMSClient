package com.icms.hana.MqttSender;

import java.util.concurrent.TimeUnit;

import org.eclipse.paho.client.mqttv3.MqttException;
import org.eclipse.paho.client.mqttv3.MqttPersistenceException;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

/**
 * Hello world!
 *
 */
public class Main {
	// YOUR DATA
	private static final String HANA_CLOUD_NAME = "p1942250341trial";
	private static final String MY_DEVICE_ID = "ICMS335";

	private static final String MQTT_HOST = "secret.ddns.net";
	private static final int MQTT_PORT = 1883;
	private static final String MQTT_USER = "admin";
	private static final String MQTT_PASS = "secret";

	public static void main(String[] args) throws MqttPersistenceException, MqttException, InterruptedException {

		IotMMSJsonManager manager = new IotMMSJsonManager(MQTT_HOST, MQTT_PORT, MQTT_USER, MQTT_PASS);

		while (true) {

			IotMMSJsonMessage iMex = manager.generateMex();

			Gson gson = new GsonBuilder().setDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSS'Z'").create();

			System.out.println("Genereted message: " + gson.toJson(iMex));

			String topic = manager.generateTopic(HANA_CLOUD_NAME, MY_DEVICE_ID);

			manager.sendMex(topic, iMex);

			System.out.println("   ---> SENDED to topic: " + topic);
			
			// ASPETTO 5 secondi
			TimeUnit.SECONDS.sleep(5);
		}
	}

}
