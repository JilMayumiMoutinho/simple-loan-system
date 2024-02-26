package me.dio.creditapplicationsystem.configuration

import org.springdoc.core.models.GroupedOpenApi
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration

@Configuration
//Class de confi
class Swagger3Config {
    @Bean
    //@Bean para o spring encontrar
    fun publicApi(): GroupedOpenApi? {
        return GroupedOpenApi.builder()
            .group("springcreditapplicationsystem-public")
            .pathsToMatch("/api/customers/**", "/api/credits/**")
            .build()
    }
}

//Para ver: http://localhost:8080/swagger-ui/index.html