package kz.reself.limma.product;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.netflix.eureka.EnableEurekaClient;
import org.springframework.context.annotation.Bean;

@SpringBootApplication
@EnableEurekaClient
public class ProductApplication {

	public static void main(String[] args) {
		SpringApplication.run(ProductApplication.class, args);
	}

//	@Bean
//	public OpenAPI customOpenAPI(@Value("${application-description}") String appDesciption, @Value("${application-version}") String appVersion) {
//
//		return new OpenAPI()
//				.info(new Info()
//						.title("LIMMA server application API")
//						.version(appVersion)
//						.description(appDesciption)
//						.termsOfService("http://swagger.io/terms/")
//						.license(new License().name("RESELF TEAM").url("https://RESELF.kz")));
//	}
}
