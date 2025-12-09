package com.remitro.transaction.infrastructure.config;

import java.util.HashMap;
import java.util.Map;

import org.apache.kafka.clients.consumer.ConsumerConfig;
import org.apache.kafka.common.serialization.StringDeserializer;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.kafka.annotation.EnableKafka;
import org.springframework.kafka.config.ConcurrentKafkaListenerContainerFactory;
import org.springframework.kafka.core.ConsumerFactory;
import org.springframework.kafka.core.DefaultKafkaConsumerFactory;
import org.springframework.kafka.listener.ContainerProperties;
import org.springframework.kafka.support.converter.StringJsonMessageConverter;
import org.springframework.kafka.support.serializer.JsonDeserializer;

@Configuration
@EnableKafka
public class KafkaConsumerConfig {

	@Value("${spring.kafka.bootstrap-servers}")
	private String bootstrapServers;

	@Value("${spring.kafka.consumer.group-id}")
	private String consumerGroupId;

	@Bean
	public ConsumerFactory<String, String> consumerFactory() {
		Map<String, Object> consumerFactoryMap = new HashMap<>();

		consumerFactoryMap.put(ConsumerConfig.BOOTSTRAP_SERVERS_CONFIG, bootstrapServers);
		consumerFactoryMap.put(ConsumerConfig.GROUP_ID_CONFIG, consumerGroupId);

		consumerFactoryMap.put(ConsumerConfig.KEY_DESERIALIZER_CLASS_CONFIG, StringDeserializer.class);
		consumerFactoryMap.put(ConsumerConfig.VALUE_DESERIALIZER_CLASS_CONFIG, StringDeserializer.class);

		consumerFactoryMap.put(JsonDeserializer.TRUSTED_PACKAGES, "*");

		return new DefaultKafkaConsumerFactory<>(consumerFactoryMap);
	}

	@Bean
	public ConcurrentKafkaListenerContainerFactory<String, String> kafkaListenerContainerFactory() {
		ConcurrentKafkaListenerContainerFactory<String, String> kafkaListenerContainerFactory =
			new ConcurrentKafkaListenerContainerFactory<>();

		kafkaListenerContainerFactory.setConsumerFactory(consumerFactory());
		kafkaListenerContainerFactory.setConcurrency(3);
		kafkaListenerContainerFactory.getContainerProperties().setAckMode(ContainerProperties.AckMode.BATCH);

		kafkaListenerContainerFactory.setRecordMessageConverter(new StringJsonMessageConverter());

		return kafkaListenerContainerFactory;
	}
}
