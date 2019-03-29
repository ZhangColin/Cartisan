package com.cartisan.common.config;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.cloud.context.config.annotation.RefreshScope;
import org.springframework.stereotype.Component;

/**
 * @author colin
 */
@Data
@Component
@ConfigurationProperties("cartisan.info")
@RefreshScope
public class SystemInfo {
    private String title;
    private String serviceUrl;
    private String version;
    private Contact contact;

    @Data
    public static class Contact{
        private String name;
        private String url;
        private String email;
    }
}