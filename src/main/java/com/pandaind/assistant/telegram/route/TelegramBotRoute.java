package com.pandaind.assistant.telegram.route;

import com.pandaind.assistant.telegram.TelegramConstant;
import com.pandaind.assistant.telegram.processor.TelegramBotProcessor;
import org.apache.camel.builder.RouteBuilder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class TelegramBotRoute extends RouteBuilder {

    private final TelegramBotProcessor telegramBotProcessor;

    @Autowired
    public TelegramBotRoute(TelegramBotProcessor telegramBotProcessor) {
        this.telegramBotProcessor = telegramBotProcessor;
    }

    @Override
    public void configure() throws Exception {
        from("telegram:bots?lazyStartProducer=true&authorizationToken=" + TelegramConstant.TOKEN)
                .routeId("TELEGRAM_BOTS")
                .throttle(1).timePeriodMillis(60000)
                .process(telegramBotProcessor)
                .to("telegram:bots?lazyStartProducer=true&authorizationToken=" + TelegramConstant.TOKEN);
    }
}
