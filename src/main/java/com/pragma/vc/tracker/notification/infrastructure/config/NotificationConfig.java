package com.pragma.vc.tracker.notification.infrastructure.config;

import com.pragma.vc.tracker.notification.application.usecase.NotificationService;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * Spring configuration for the Notification bounded context.
 * Registers the NotificationService as a Spring bean.
 */
@Configuration
public class NotificationConfig {

    @Bean
    public NotificationService notificationService() {
        return new NotificationService();
    }
}
