package com.lyyang.spring_ai.controller;

import com.lyyang.spring_ai.service.AiService;
import lombok.RequiredArgsConstructor;
import org.springframework.ai.chat.model.ChatResponse;
import org.springframework.core.io.Resource;
import org.springframework.http.MediaType;
import org.springframework.http.codec.multipart.FilePart;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import reactor.core.publisher.Flux;

import java.util.Map;


@RestController
@RequiredArgsConstructor
public class AiController {

    private final Resource promptResource;

    private final Resource codeResource;

    private final AiService aiService;

    private static String getResponseText(ChatResponse response) {
        return response.getResult() != null && response.getResult().getOutput() != null && response.getResult().getOutput().getText() != null ? response.getResult().getOutput().getText() : "";
    }

    @GetMapping(value = "/chat", produces = MediaType.TEXT_EVENT_STREAM_VALUE)
    public Flux<String> chat(String prompt) {
        return aiService.chat(prompt);
    }

    @GetMapping(value = "/chatModel", produces = MediaType.TEXT_EVENT_STREAM_VALUE)
    public Flux<String> chatModel(String prompt) {
        return aiService.chatWithContext(prompt)
                .map(AiController::getResponseText);
    }

    @GetMapping(value = "/template1")
    public Flux<String> template1(@RequestParam String llm) {
        return aiService.chatWithTemplate(
                        Map.of("llm", llm),
                        promptResource
                )
                .map(AiController::getResponseText);

    }

    @GetMapping(value = "/template2")
    public Flux<String> template2(@RequestParam String language, @RequestParam String methodName) {
        return aiService.chatWithTemplate(
                        Map.of("language", language, "methodName", methodName),
                        codeResource
                )
                .map(AiController::getResponseText);
    }

    @PostMapping(value = "/imagequery", consumes = MediaType.MULTIPART_FORM_DATA_VALUE,
            produces = MediaType.TEXT_EVENT_STREAM_VALUE)
    public Flux<String> imageQuery(@RequestPart FilePart file, @RequestPart String message) {
        return aiService.imageQuery(file, message);
    }

}