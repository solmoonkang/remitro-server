package com.remitro.member.infrastructure.config;

import java.util.HashMap;
import java.util.Map;

import org.apache.kafka.clients.producer.ProducerConfig;
import org.apache.kafka.common.serialization.StringSerializer;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.kafka.core.DefaultKafkaProducerFactory;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.kafka.core.ProducerFactory;

@Configuration
public class KafkaProducerConfig {

	@Value("${spring.kafka.bootstrap-servers}")
	private String bootstrapServers;

	@Bean
	public ProducerFactory<String, String> producerFactory() {
		Map<String, Object> producerFactoryMap = new HashMap<>();

		producerFactoryMap.put(ProducerConfig.BOOTSTRAP_SERVERS_CONFIG, bootstrapServers);
		producerFactoryMap.put(ProducerConfig.KEY_SERIALIZER_CLASS_CONFIG, StringSerializer.class);
		producerFactoryMap.put(ProducerConfig.VALUE_SERIALIZER_CLASS_CONFIG, StringSerializer.class);

		producerFactoryMap.put(ProducerConfig.ACKS_CONFIG, "all");
		producerFactoryMap.put(ProducerConfig.RETRIES_CONFIG, 3);
		producerFactoryMap.put(ProducerConfig.ENABLE_IDEMPOTENCE_CONFIG, true);

		return new DefaultKafkaProducerFactory<>(producerFactoryMap);
	}

	@Bean
	public KafkaTemplate<String, String> kafkaTemplate() {
		return new KafkaTemplate<>(producerFactory());
	}
}
