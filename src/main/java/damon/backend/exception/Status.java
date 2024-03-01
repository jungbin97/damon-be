package damon.backend.exception;

/**
 * HTTP 응답 상태 코드를 정의하는 클래스입니다.
 * 각 상태 코드에 대한 설명과 함께 사용됩니다.
 */
public class Status {
    // 200 - OK: 요청이 성공적으로 처리되었음
    public static final String OK = "200 OK";

    // 201 - Created: 요청이 성공적으로 처리되었고 새로운 리소스가 생성됨
    public static final String CREATED = "201 Created";

    // 204 - No Content: 요청이 성공적으로 처리되었고 응답 본문에 컨텐츠가 없음
    public static final String NO_CONTENT = "204 No Content";

    // 400 - Bad Request: 잘못된 요청
    public static final String BAD_REQUEST = "400 Bad Request";

    // 401 - Unauthorized: 권한이 없음
    public static final String UNAUTHORIZED = "401 Unauthorized";

    // 403 - Forbidden: 접근 금지
    public static final String FORBIDDEN = "403 Forbidden";

    // 404 - Not Found: 리소스를 찾을 수 없음
    public static final String NOT_FOUND = "404 Not Found";

    // 500 - Internal Server Error: 서버 내부 오류
    public static final String INTERNAL_SERVER_ERROR = "500 Internal Server Error";

    // 503 - Service Unavailable: 서비스 이용 불가
    public static final String SERVICE_UNAVAILABLE = "503 Service Unavailable";
}