package com.cartisan.common.configs;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.client.RestTemplate;

/**
 * <p>Title: RestTemplateConfig</p>
 * <p>Description: </p>
 *
 * @author colin
 */
@Configuration
public class RestTemplateConfig {
    @Bean
//    @LoadBalanced
    public RestTemplate restTemplate() {
        return new RestTemplate();
    }
}
