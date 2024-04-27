package com.pandaind.assistant.telegram.processor;

import dev.langchain4j.chain.ConversationalRetrievalChain;
import org.apache.camel.Exchange;
import org.apache.camel.Processor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class TelegramBotProcessor implements Processor {

    private final ConversationalRetrievalChain conversationalRetrievalChain;

    @Autowired
    public TelegramBotProcessor(ConversationalRetrievalChain conversationalRetrievalChain) {
        this.conversationalRetrievalChain = conversationalRetrievalChain;
    }

    @Override
    public void process(Exchange exchange) throws Exception {
        String query = exchange.getIn().getBody(String.class);
        if (query != null && !query.isEmpty()) {
            var answer = conversationalRetrievalChain.execute(query);
            exchange.getIn().setBody(answer);
        } else {
            exchange.getIn().setBody("Please, try after sometime");
        }
    }
}
