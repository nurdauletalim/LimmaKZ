package kz.reself.limma.gatewayportal;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.netflix.eureka.EnableEurekaClient;

@SpringBootApplication
@EnableEurekaClient
public class GatewayPortalApplication {

	public static void main(String[] args) {
		SpringApplication.run(GatewayPortalApplication.class, args);
	}

}
