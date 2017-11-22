package com.icms.hana.MqttSender;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Random;

import org.eclipse.paho.client.mqttv3.MqttClient;
import org.eclipse.paho.client.mqttv3.MqttConnectOptions;
import org.eclipse.paho.client.mqttv3.MqttException;
import org.eclipse.paho.client.mqttv3.MqttMessage;
import org.eclipse.paho.client.mqttv3.MqttPersistenceException;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

public class IotMMSJsonManager {

	private static final String TOPIC_TEMPLATE = "iot/data/iotmms<HANA_CLOUD_NAME>/v1/<MY_DEVICE_ID>";

	private MqttClient client;

	public IotMMSJsonManager() {
		// TODO Auto-generated constructor stub
	}

	public IotMMSJsonManager(String host, int port, String user, String pass) throws MqttException {
		this.connect(host, port, user, pass);
	}

	private static final String MEX_MODE = "async";

	public IotMMSJsonMessage generateMex() {
		Message mex = new Message();

		mex.setTimestamp(new Date());
		// GENERO O LEGGO LA TEMPERATURA
		Random r = new Random();
		double rangeMin = 10.0;
		double rangeMax = 25.0;
		double temperatureRaw = rangeMin + (rangeMax - rangeMin) * r.nextDouble();
		double temperature = new BigDecimal(temperatureRaw).setScale(2, RoundingMode.HALF_UP).doubleValue();
		mex.setTemperature(temperature);

		// MANDO UN SOLO MEX alla volta

		IotMMSJsonMessage iotMex = new IotMMSJsonMessage();
		iotMex.setMessageType(Message.MESSAGE_TYPE);
		iotMex.setMode(MEX_MODE);

		List<Message> mexs = new ArrayList<Message>();
		mexs.add(mex);

		iotMex.setMessages(mexs);

		return iotMex;
	}

	public void sendMex(String topic, IotMMSJsonMessage iMex) throws MqttPersistenceException, MqttException {

		Gson gson = new GsonBuilder().setDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSS'Z'").create();
		String sMex = gson.toJson(iMex);

		if (isConnected()) {
			MqttMessage mMex = new MqttMessage();
			mMex.setPayload(sMex.getBytes(Charset.forName("UTF-8")));
			mMex.setQos(0);
			mMex.setRetained(false);
			client.publish(topic, mMex);
		} else {
			System.out.println("ERR : No MQTT Connection");
		}
	}

	private boolean isConnected() {
		return client != null || client.isConnected();
	}

	public void connect(String host, int port, String user, String pass) throws MqttException {
		client = new MqttClient("tcp://" + host + ":" + port, "MyJAVAClient");

		MqttConnectOptions options = new MqttConnectOptions();
		options.setUserName(user);
		options.setPassword(pass.toCharArray());

		client.connect(options);
	}

	public String generateTopic(String hanaCloudName, String myDeviceId) {
		String topic = TOPIC_TEMPLATE.replace("<HANA_CLOUD_NAME>", hanaCloudName).replace("<MY_DEVICE_ID>", myDeviceId);
		return topic;
	}
}
