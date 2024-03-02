package damon.backend.util;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.ObjectWriter;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;
import lombok.extern.slf4j.Slf4j;

/**
 * 커스텀 로그 유틸리티 클래스입니다.
 */
@Slf4j
@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class Log {

    private static final ObjectMapper objectMapper = new ObjectMapper().registerModule(new JavaTimeModule());
    private static final ObjectWriter prettyPrinter = objectMapper.writerWithDefaultPrettyPrinter();

    private static String toJsonString(Object object) {
        try {
            return prettyPrinter.writeValueAsString(object);
        } catch (JsonProcessingException e) {
            log.error("Error converting object to json", e);
            return "";
        }
    }

    public static <T> void trace(T object) {
        log.trace("\n\n{}\n{}\n\n", object.getClass().getSimpleName(), toJsonString(object));
    }

    public static <T> void debug(T object) {
        log.debug("\n\n{}\n{}\n\n", object.getClass().getSimpleName(), toJsonString(object));
    }

    public static <T> void info(T object) {
        log.info("\n\n{}\n{}\n\n", object.getClass().getSimpleName(), toJsonString(object));
    }

    public static <T> void warn(T object) {
        log.warn("\n\n{}\n{}\n\n", object.getClass().getSimpleName(), toJsonString(object));
    }

    public static <T> void error(T object) {
        log.error("\n\n{}\n{}\n\n", object.getClass().getSimpleName(), toJsonString(object));
    }
}