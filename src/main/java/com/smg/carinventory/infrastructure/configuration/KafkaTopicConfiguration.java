package com.smg.carinventory.infrastructure.configuration;

import org.apache.kafka.clients.admin.NewTopic;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.kafka.config.TopicBuilder;
import org.springframework.kafka.core.KafkaAdmin;

@Configuration
public class KafkaTopicConfiguration {
	
	@Bean
	public NewTopic topic4(KafkaAdmin admin) {
		//admin.setFatalIfBrokerNotAvailable(true);
	    return TopicBuilder.name("smg.car.events")
	            .build();
	}
}
