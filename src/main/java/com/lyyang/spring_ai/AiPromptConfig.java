package com.lyyang.spring_ai;


import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.io.ClassPathResource;
import org.springframework.core.io.Resource;

@Configuration
public class AiPromptConfig {

    @Bean
    public Resource promptResource() {
        return new ClassPathResource("prompt.st");
    }

    @Bean
    public Resource codeResource() {
        return new ClassPathResource("code.txt");
    }
}
