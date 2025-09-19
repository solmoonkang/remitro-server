package com.remitro.transaction.infrastructure.config;

import static org.apache.kafka.clients.consumer.ConsumerConfig.*;
import static org.apache.kafka.clients.consumer.ConsumerConfig.BOOTSTRAP_SERVERS_CONFIG;
import static org.apache.kafka.clients.producer.ProducerConfig.*;

import java.util.HashMap;
import java.util.Map;

import org.apache.kafka.common.serialization.StringDeserializer;
import org.apache.kafka.common.serialization.StringSerializer;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.kafka.annotation.EnableKafka;
import org.springframework.kafka.config.ConcurrentKafkaListenerContainerFactory;
import org.springframework.kafka.core.ConsumerFactory;
import org.springframework.kafka.core.DefaultKafkaConsumerFactory;
import org.springframework.kafka.core.DefaultKafkaProducerFactory;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.kafka.core.ProducerFactory;
import org.springframework.kafka.listener.DeadLetterPublishingRecoverer;
import org.springframework.kafka.listener.DefaultErrorHandler;
import org.springframework.util.backoff.FixedBackOff;

@Configuration
@EnableKafka
public class KafkaConfig {

	@Value("${spring.kafka.bootstrap-servers}")
	private String bootstrapServers;

	@Value("${spring.kafka.consumer.group-id}")
	private String groupId;

	@Value("${spring.kafka.listener.retry-count}")
	private int retryCount;

	@Value("${spring.kafka.listener.retry-interval}")
	private long retryInterval;

	@Bean
	public ConcurrentKafkaListenerContainerFactory<String, String> kafkaListenerContainerFactory() {
		ConcurrentKafkaListenerContainerFactory<String, String> concurrentKafkaListenerContainerFactory =
			new ConcurrentKafkaListenerContainerFactory<>();
		concurrentKafkaListenerContainerFactory.setConsumerFactory(consumerFactory());

		concurrentKafkaListenerContainerFactory.setCommonErrorHandler(new DefaultErrorHandler(
			new DeadLetterPublishingRecoverer(kafkaTemplate()), new FixedBackOff(retryInterval, retryCount)
		));

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

	@Bean
	public KafkaTemplate<String, String> kafkaTemplate() {
		return new KafkaTemplate<>(producerFactory());
	}

	@Bean
	public ProducerFactory<String, String> producerFactory() {
		Map<String, Object> configProperties = new HashMap<>();
		configProperties.put(BOOTSTRAP_SERVERS_CONFIG, bootstrapServers);
		configProperties.put(KEY_SERIALIZER_CLASS_CONFIG, StringSerializer.class);
		configProperties.put(VALUE_SERIALIZER_CLASS_CONFIG, StringSerializer.class);
		return new DefaultKafkaProducerFactory<>(configProperties);
	}
}
