package tower_of_fisa.paydeuk.api_gateway.error;

import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.exception.ExceptionUtils;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Component;
import org.springframework.web.server.ServerWebExchange;
import org.springframework.core.io.buffer.DataBuffer;
import reactor.core.publisher.Mono;

import java.nio.charset.StandardCharsets;
import java.time.LocalDateTime;
import com.fasterxml.jackson.databind.ObjectMapper;
import tower_of_fisa.paydeuk.api_gateway.dto.CustomError;
import tower_of_fisa.paydeuk.api_gateway.dto.CustomResponse;
import tower_of_fisa.paydeuk.api_gateway.error.exception.BasicCustomException500;

@Component
@Slf4j(topic = "GlobalErrorResponder")
public class GlobalErrorResponder {
    public static final String TRACE = "trace";

    @Value("${error.printStackTrace}")
    private boolean printStackTrace;

    @Value("${error.printStackTraceLine}")
    private int printStackTraceLine;

    private final ObjectMapper objectMapper;

    public GlobalErrorResponder() { // JSON 직렬화 시에 LocalDateTime을 직렬화 가능하게 하기 위해 설정
        this.objectMapper = new ObjectMapper();
        this.objectMapper.registerModule(new JavaTimeModule());
        this.objectMapper.disable(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS);
    }

    public Mono<Void> respond(ServerWebExchange exchange, HttpStatus status, BasicCustomException500 exception) {
        exchange.getResponse().setStatusCode(status);
        exchange.getResponse().getHeaders().setContentType(MediaType.APPLICATION_JSON);
        ErrorDefineCode errorCode = exception.getCode();


        CustomError error = new CustomError(errorCode.getCode(), LocalDateTime.now());

        if (printStackTrace && isTraceOn(exception)) {
            error.setStackTrace(getStackTrace(exception, printStackTraceLine));
        }
        exception.getCode();
        CustomResponse<CustomError> errorResponseDto = new CustomResponse<>(false, status, errorCode.getMessage(), error);

        // JSON 직렬화
        byte[] jsonBytes;
        try {
            jsonBytes = objectMapper.writeValueAsBytes(errorResponseDto);
        } catch (Exception e) {
            jsonBytes = ("{\"message\":\"응답 변환 오류\"}").getBytes(StandardCharsets.UTF_8);
        }

        DataBuffer buffer = exchange.getResponse()
                .bufferFactory()
                .wrap(jsonBytes);

        return exchange.getResponse().writeWith(Mono.just(buffer));
    }

    private String getStackTrace(Exception e, int line) {
        String stackTrace = ExceptionUtils.getStackTrace(e);

        // 스택 트레이스를 줄 단위로 분할하여 line 줄까지만 사용
        String[] lines = stackTrace.split(System.lineSeparator());
        StringBuilder limitedStackTrace = new StringBuilder();
        int limit = Math.min(lines.length, line);
        for (int i = 0; i < limit; i++) {
            limitedStackTrace.append(lines[i]).append(System.lineSeparator());
        }

        return limitedStackTrace.toString();
    }

    private boolean isTraceOn(Exception exception) {
        return exception.getStackTrace() != null && exception.getStackTrace().length > 0;
    }

}

