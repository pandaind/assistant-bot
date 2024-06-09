package com.pandaind.assistant;

import com.pandaind.assistant.config.GcpProperties;
import com.pandaind.assistant.config.HuggingFaceProperties;
import com.pandaind.assistant.config.OllamaProperties;
import com.pandaind.assistant.telegram.TelegramProperties;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.properties.EnableConfigurationProperties;

@SpringBootApplication
@EnableConfigurationProperties(value = {TelegramProperties.class, GcpProperties.class,
        HuggingFaceProperties.class, OllamaProperties.class})
public class AssistantBotApplication {
    public static void main(String[] args) {
        SpringApplication.run(AssistantBotApplication.class, args);
    }

}
