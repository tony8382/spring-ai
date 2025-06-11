package com.lyyang.spring_ai.controller;

import com.lyyang.spring_ai.service.AiService;
import lombok.RequiredArgsConstructor;
import org.springframework.ai.chat.model.ChatResponse;
import org.springframework.core.io.Resource;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import reactor.core.publisher.Flux;

import java.util.Map;


@RestController
@RequiredArgsConstructor
public class AiController {

    private final Resource promptResource;

    private final Resource codeResource;

    private final AiService aiService;

    private static String apply(ChatResponse response) {
        return response.getResult() != null && response.getResult().getOutput() != null && response.getResult().getOutput().getText() != null ? response.getResult().getOutput().getText() : "";
    }

    @GetMapping(value = "/chat", produces = MediaType.TEXT_EVENT_STREAM_VALUE)
    public Flux<String> chat(String prompt) {
        return aiService.chat(prompt);
    }

    @GetMapping(value = "/chatModel", produces = MediaType.TEXT_EVENT_STREAM_VALUE)
    public Flux<String> chatModel(String prompt) {
        return aiService.chatWithContext(prompt)
                .map(AiController::apply);
    }

    @GetMapping(value = "/template1")
    public Flux<String> template1(@RequestParam String llm) {
        return aiService.chatWithTemplate(
                        Map.of("llm", llm),
                        promptResource
                )
                .map(AiController::apply);

    }

    @GetMapping(value = "/template2")
    public Flux<String> template2(@RequestParam String language, @RequestParam String methodName) {
        return aiService.chatWithTemplate(
                        Map.of("language", language, "methodName", methodName),
                        codeResource
                )
                .map(AiController::apply);
    }

}