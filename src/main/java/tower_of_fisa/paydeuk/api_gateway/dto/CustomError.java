package tower_of_fisa.paydeuk.api_gateway.dto;

import lombok.Data;
import lombok.RequiredArgsConstructor;

import java.time.LocalDateTime;

@Data
@RequiredArgsConstructor
public class CustomError {
    private final String errorCode;
    private final LocalDateTime time;
    private String stackTrace;
}
