package com.lyyang.spring_ai.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.ai.chat.model.ChatModel;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;


@RestController
@RequiredArgsConstructor
public class AiController {
    private final ChatModel chatModel;

    @GetMapping("/chat")
    public String chat(String prompt) {
        return chatModel.call(prompt);
    }
}