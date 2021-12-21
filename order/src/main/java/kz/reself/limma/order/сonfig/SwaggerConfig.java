//package kz.reself.limma.order.сonfig;
//
//import org.springframework.context.annotation.Configuration;
//import springfox.documentation.swagger2.annotations.EnableSwagger2;
//
//@Configuration
//@EnableSwagger2
//public class SwaggerConfig {
//
//    /**
//     * Method to set paths to be included through swagger
//     *
//     * @return Docket
//     */
//    @Bean
//    public Docket configApi() {
//        return new Docket(DocumentationType.SWAGGER_2).apiInfo(apiInfo()).pathMapping("/").select()
//                .paths(regex("/api.*")).build();
//    }
//
//
//    /**
//     * Method to set swagger info
//     *
//     * @return ApiInfoBuilder
//     */
//    private ApiInfo apiInfo() {
//        return new ApiInfoBuilder().title("").description("").version("1.0").build();
//    }
//}
