package kz.reself.limma.gatewaycrm;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.netflix.eureka.EnableEurekaClient;

@SpringBootApplication
@EnableEurekaClient
public class GatewayCrmApplication {

	public static void main(String[] args) {
		SpringApplication.run(GatewayCrmApplication.class, args);
	}

}
