package com.remitro.transaction.infrastructure.config;

import static org.apache.kafka.clients.consumer.ConsumerConfig.*;

import java.util.HashMap;
import java.util.Map;

import org.apache.kafka.common.serialization.StringDeserializer;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.kafka.annotation.EnableKafka;
import org.springframework.kafka.config.ConcurrentKafkaListenerContainerFactory;
import org.springframework.kafka.core.ConsumerFactory;
import org.springframework.kafka.core.DefaultKafkaConsumerFactory;

@Configuration
@EnableKafka
public class KafkaConfig {

	@Value("${spring.kafka.bootstrap-servers}")
	private String bootstrapServers;

	@Value("${spring.kafka.consumer.group-id}")
	private String groupId;

	@Bean
	public ConcurrentKafkaListenerContainerFactory<String, String> kafkaListenerContainerFactory() {
		ConcurrentKafkaListenerContainerFactory<String, String> concurrentKafkaListenerContainerFactory =
			new ConcurrentKafkaListenerContainerFactory<>();
		concurrentKafkaListenerContainerFactory.setConsumerFactory(consumerFactory());
		return concurrentKafkaListenerContainerFactory;
	}

	@Bean
	public ConsumerFactory<String, String> consumerFactory() {
		final Map<String, Object> configProperties = new HashMap<>();
		configProperties.put(BOOTSTRAP_SERVERS_CONFIG, bootstrapServers);
		configProperties.put(GROUP_ID_CONFIG, groupId);
		configProperties.put(KEY_DESERIALIZER_CLASS_CONFIG, StringDeserializer.class);
		configProperties.put(VALUE_DESERIALIZER_CLASS_CONFIG, StringDeserializer.class);
		return new DefaultKafkaConsumerFactory<>(configProperties);
	}
}
