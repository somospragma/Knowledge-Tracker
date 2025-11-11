package com.pragma.vc.tracker.shared.infrastructure.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.annotation.EnableAsync;

/**
 * Configuration to enable asynchronous event processing in Spring.
 * This allows @Async methods to execute in separate threads.
 */
@Configuration
@EnableAsync
public class AsyncConfig {
    // Spring will use a default SimpleAsyncTaskExecutor
    // For production, consider configuring a custom ThreadPoolTaskExecutor
}
