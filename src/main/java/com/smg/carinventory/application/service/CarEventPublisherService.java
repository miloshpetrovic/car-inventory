package com.smg.carinventory.application.service;

import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;

import com.smg.carinventory.domain.model.CarEvent;

@Service
public class CarEventPublisherService {

	private KafkaTemplate<String, CarEvent> kafkaTemplate;
	
	
	public CarEventPublisherService(KafkaTemplate<String, CarEvent> kafkaTemplate) {
		this.kafkaTemplate = kafkaTemplate;
	}



	public void publish(CarEvent event) {
		kafkaTemplate.send("smg.car.events", event);
	}
	
}
