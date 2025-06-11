package com.lyyang.spring_ai.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.ai.chat.messages.Message;
import org.springframework.ai.chat.messages.SystemMessage;
import org.springframework.ai.chat.messages.UserMessage;
import org.springframework.ai.chat.model.ChatModel;
import org.springframework.ai.chat.model.ChatResponse;
import org.springframework.ai.chat.prompt.Prompt;
import org.springframework.ai.chat.prompt.PromptTemplate;
import org.springframework.ai.openai.OpenAiChatOptions;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.Resource;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import reactor.core.publisher.Flux;

import java.util.List;
import java.util.Map;


@RestController
@RequiredArgsConstructor
public class AiController {
    private final ChatModel chatModel;

    @Value("classpath:prompt.st")
    private Resource templateResource;

    private static String apply(ChatResponse response) {
        return response.getResult() != null && response.getResult().getOutput() != null && response.getResult().getOutput().getText() != null ? response.getResult().getOutput().getText() : "";
    }

    @GetMapping(value = "/chat", produces = MediaType.TEXT_EVENT_STREAM_VALUE)
    public Flux<String> chat(String prompt) {
        return chatModel.stream(prompt);
    }

    @GetMapping(value = "/chatModel", produces = MediaType.TEXT_EVENT_STREAM_VALUE)
    public Flux<String> chatModel(String prompt) {
        List<Message> messages = List.of(
                new SystemMessage("你是一個說故事大師，如果找不到資料或是最近的新聞就編造一個聽起來讓人開心的消息"),
                new UserMessage(prompt)
        );

        return chatModel.stream(new Prompt(
                        messages,
                        OpenAiChatOptions.builder().temperature(1.0).build())
                )
                .map(AiController::apply);

    }

    @GetMapping(value = "/template1")
    public Flux<String> template1(@RequestParam String llm) {
        PromptTemplate promptTemplate = new PromptTemplate(templateResource);
        Prompt prompt = promptTemplate.create(Map.of("llm", llm));

        return chatModel.stream(prompt).map(AiController::apply);

    }
}