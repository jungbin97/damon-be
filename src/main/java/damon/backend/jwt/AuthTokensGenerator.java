//package damon.backend.jwt;
//
//import damon.backend.entity.AuthTokens;
//import lombok.RequiredArgsConstructor;
//import lombok.extern.slf4j.Slf4j;
//import org.springframework.stereotype.Component;
//
//import java.util.Date;
//
//@Component
//@RequiredArgsConstructor
//@Slf4j
//public class AuthTokensGenerator {
//    private static final String BEARER_TYPE = "Bearer";
//    private static final long ACCESS_TOKEN_EXPIRE_TIME = 1000 * 60 * 60; 	//1시간
//    private static final long REFRESH_TOKEN_EXPIRE_TIME = 1000 * 60 * 60 * 24 * 14;  // 14일
//
//    private final JwtTokenProvider jwtTokenProvider;
//
//    //id 받아 Access Token 생성
//    public AuthTokens generate(String id) {
//        log.info("AuthTokensGenerator의 generate 실행");
//        long now = (new Date()).getTime();
//        Date accessTokenExpiredAt = new Date(now + ACCESS_TOKEN_EXPIRE_TIME);
//        Date refreshTokenExpiredAt = new Date(now + REFRESH_TOKEN_EXPIRE_TIME);
//
//        //String subject = email.toString();
//        String accessToken = jwtTokenProvider.accessTokenGenerate(id, accessTokenExpiredAt);
//        String refreshToken = jwtTokenProvider.refreshTokenGenerate(refreshTokenExpiredAt);
//        log.info("accessToken={}",accessToken);
//        log.info("refreshToken={}",refreshToken);
//
//        return AuthTokens.of(accessToken, refreshToken, BEARER_TYPE, ACCESS_TOKEN_EXPIRE_TIME / 1000L);
//    }
//}
