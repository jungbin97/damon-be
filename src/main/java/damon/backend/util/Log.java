package damon.backend.util;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.ObjectWriter;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import lombok.extern.slf4j.Slf4j;

@Slf4j
public class Log {

    private static final ObjectMapper objectMapper = new ObjectMapper().registerModule(new JavaTimeModule());
    private static final ObjectWriter prettyPrinter = objectMapper.writerWithDefaultPrettyPrinter();

    private Log() {
        // 유틸리티 클래스이므로 인스턴스 생성을 막기 위해 private 생성자 사용
    }

    public static <T> void info(T object) {
        try {
            String jsonString = prettyPrinter.writeValueAsString(object);
            log.info("{} as json\n{}", object.getClass().getSimpleName(), jsonString);
        } catch (JsonProcessingException e) {
            log.error("Error converting object to json", e);
        }
    }
}