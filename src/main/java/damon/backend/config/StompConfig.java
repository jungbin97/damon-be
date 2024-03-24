package damon.backend.config;

import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Configuration;
import org.springframework.messaging.simp.config.MessageBrokerRegistry;
import org.springframework.web.socket.config.annotation.EnableWebSocketMessageBroker;
import org.springframework.web.socket.config.annotation.StompEndpointRegistry;
import org.springframework.web.socket.config.annotation.WebSocketMessageBrokerConfigurer;

@Configuration
@EnableWebSocketMessageBroker
@Slf4j
public class StompConfig implements WebSocketMessageBrokerConfigurer {
    @Override
    public void registerStompEndpoints(StompEndpointRegistry registry) {
        log.info("SOCKET 연결");
        registry.addEndpoint("/stomp/chat")
//                .addInterceptors(new MyHandshakeInterceptor())  // 인터셉터 추가
                .setAllowedOriginPatterns("*");
    }

    @Override
    public void configureMessageBroker(MessageBrokerRegistry registry) {
        // Client가 SEND할 수 있는 경로
        registry.setApplicationDestinationPrefixes("/pub"); // 클라이언트가 서버로 메시지를 보낼 때 사용하는 경로
        // SimpleBroker를 사용하여 Client가 SUBSCRIBE할 수 있는 경로(Message Broker는 Redis, RabbitMQ, ActiveMQ 등을 사용할 수 있음. 여기서는 SimpleBroker를 사용함.)
        registry.enableSimpleBroker("/sub", "/queue", "/topic"); // 클라이언트가 메시지를 구독할 때 사용하는 경로
    }
}
