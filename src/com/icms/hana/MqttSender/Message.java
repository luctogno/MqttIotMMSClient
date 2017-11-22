package com.icms.hana.MqttSender;

import java.util.Date;

/**
 * Class holding information on a person.
 */
public class Message {

	public static final String MESSAGE_TYPE = "b1b42152f7d1481b5363";

	private Date timestamp;
	private double temperature;

	public Date getTimestamp() {
		return timestamp;
	}

	public void setTimestamp(Date timestamp) {
		this.timestamp = timestamp;
	}

	public double getTemperature() {
		return temperature;
	}

	public void setTemperature(double temperature) {
		this.temperature = temperature;
	}

}
