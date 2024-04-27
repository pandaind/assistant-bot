package com.pandaind.assistant.controller;

import dev.langchain4j.chain.ConversationalRetrievalChain;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/chat")
public class ChatController {

    private final ConversationalRetrievalChain conversationalRetrievalChain;

    public ChatController(ConversationalRetrievalChain conversationalRetrievalChain) {
        this.conversationalRetrievalChain = conversationalRetrievalChain;
    }

    @PostMapping
    public ResponseEntity<String> chat(@RequestBody String query) {
        var answer = conversationalRetrievalChain.execute(query);
        return ResponseEntity.ok(answer);
    }


}
