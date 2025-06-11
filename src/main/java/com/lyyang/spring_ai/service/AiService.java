package com.lyyang.spring_ai.service;

import lombok.RequiredArgsConstructor;
import org.springframework.ai.chat.messages.Message;
import org.springframework.ai.chat.messages.SystemMessage;
import org.springframework.ai.chat.messages.UserMessage;
import org.springframework.ai.chat.model.ChatModel;
import org.springframework.ai.chat.model.ChatResponse;
import org.springframework.ai.chat.prompt.Prompt;
import org.springframework.ai.chat.prompt.PromptTemplate;
import org.springframework.ai.openai.OpenAiChatOptions;
import org.springframework.core.io.Resource;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;

import java.util.List;
import java.util.Map;

@Service
@RequiredArgsConstructor
public class AiService {
    private final ChatModel chatModel;

    public Flux<String> chat(String prompt) {
        return chatModel.stream(prompt);
    }

    public Flux<ChatResponse> chatWithContext(String prompt) {
        List<Message> messages = List.of(
                new SystemMessage("你是一個說故事大師，如果找不到資料或是最近的新聞就編造一個聽起來讓人開心的消息"),
                new UserMessage(prompt)
        );

        return chatModel.stream(new Prompt(
                messages,
                OpenAiChatOptions.builder().temperature(1.0).build())
        );
    }

    public Flux<ChatResponse> chatWithTemplate(Map<String, Object> map, Resource template) {
        PromptTemplate promptTemplate = new PromptTemplate(template);
        Prompt prompt = promptTemplate.create(map);

        return chatModel.stream(prompt);
    }

}
