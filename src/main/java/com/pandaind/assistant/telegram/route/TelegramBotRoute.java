package com.pandaind.assistant.telegram.route;

import com.pandaind.assistant.telegram.TelegramProperties;
import com.pandaind.assistant.telegram.processor.TelegramBotProcessor;
import org.apache.camel.builder.RouteBuilder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class TelegramBotRoute extends RouteBuilder {

    private final TelegramBotProcessor telegramBotProcessor;

    private final TelegramProperties telegramProperties;

    @Autowired
    public TelegramBotRoute(TelegramBotProcessor telegramBotProcessor, TelegramProperties telegramProperties) {
        this.telegramBotProcessor = telegramBotProcessor;
        this.telegramProperties = telegramProperties;
    }

    @Override
    public void configure() {
        from("telegram:bots?lazyStartProducer=true&authorizationToken=" + telegramProperties.getToken())
                .routeId("TELEGRAM_BOTS")
                //.throttle(1).timePeriodMillis(60000)
                .process(telegramBotProcessor)
                .to("telegram:bots?lazyStartProducer=true&authorizationToken=" + telegramProperties.getToken());
    }
}
