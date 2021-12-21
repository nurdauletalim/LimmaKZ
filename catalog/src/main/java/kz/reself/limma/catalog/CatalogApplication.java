package kz.reself.limma.catalog;

import org.apache.kafka.clients.producer.ProducerConfig;
import org.apache.kafka.common.serialization.StringSerializer;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.cloud.client.circuitbreaker.EnableCircuitBreaker;
import org.springframework.cloud.client.loadbalancer.LoadBalanced;
import org.springframework.cloud.netflix.eureka.EnableEurekaClient;
import org.springframework.context.annotation.Bean;
import org.springframework.http.client.HttpComponentsClientHttpRequestFactory;
import org.springframework.kafka.core.DefaultKafkaProducerFactory;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.kafka.core.ProducerFactory;
import org.springframework.kafka.support.serializer.JsonSerializer;
import org.springframework.web.client.RestTemplate;

import java.util.HashMap;
import java.util.Map;
//import org.springframework.cloud.netflix.hystrix.EnableHystrix;
//import org.springframework.cloud.netflix.hystrix.dashboard.EnableHystrixDashboard;

@SpringBootApplication(scanBasePackages = "kz.reself.limma")
@EnableEurekaClient
@EnableConfigurationProperties
//@EnableHystrix
//@EnableHystrixDashboard
//@EnableCircuitBreaker
public class CatalogApplication {

	public static void main(String[] args) {
		SpringApplication.run(CatalogApplication.class, args);
	}

	@Bean
	@LoadBalanced
	public RestTemplate restTemplate() {
		HttpComponentsClientHttpRequestFactory requestFactory = new HttpComponentsClientHttpRequestFactory();
		requestFactory.setConnectTimeout(3000);
		return new RestTemplate(requestFactory);
	}

	@Bean
	public KafkaTemplate<String, Integer> myMessageKafkaTemplate() {
		return new KafkaTemplate<>(greetingProducerFactory());
	}

	@Bean
	public ProducerFactory<String, Integer> greetingProducerFactory() {
		Map<String, Object> configProps = new HashMap<>();
		configProps.put(ProducerConfig.BOOTSTRAP_SERVERS_CONFIG, "localhost:9095");
		configProps.put(ProducerConfig.KEY_SERIALIZER_CLASS_CONFIG, StringSerializer.class);
		configProps.put(ProducerConfig.VALUE_SERIALIZER_CLASS_CONFIG, JsonSerializer.class);
		return new DefaultKafkaProducerFactory<>(configProps);
	}
}
