package com.lyyang.spring_ai;

import lombok.extern.slf4j.Slf4j;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.Import;
import org.springframework.core.io.Resource;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;

@Slf4j
@SpringBootTest
@Import(AiPromptConfig.class)
public class AiPromptConfigTest {

    @Autowired
    private Resource promptResource;

    @Autowired
    private Resource codeResource;

    @Test
    void promptResource_shouldBeReadableAndContainText() throws Exception {
        Assertions.assertThat(promptResource.exists()).isTrue();
        String content = readResourceAsString(promptResource);
        Assertions.assertThat(content).isNotBlank();
        log.info("prompt.txt content:{}", content);
    }

    @Test
    void codeResource_shouldBeReadableAndContainText() throws Exception {
        Assertions.assertThat(codeResource.exists()).isTrue();
        String content = readResourceAsString(codeResource);
        Assertions.assertThat(content).isNotBlank();
        log.info("code.txt content:{}", content);
    }

    private String readResourceAsString(Resource resource) throws Exception {
        try (BufferedReader reader = new BufferedReader(
                new InputStreamReader(resource.getInputStream(), StandardCharsets.UTF_8))) {
            return reader.lines().reduce("", (a, b) -> a + "\n" + b);
        }
    }
}