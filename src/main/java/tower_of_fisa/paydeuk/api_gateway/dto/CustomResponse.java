package tower_of_fisa.paydeuk.api_gateway.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import org.springframework.http.HttpStatus;

@Data
@AllArgsConstructor
public class CustomResponse<T> {
  private final boolean success;
  private final HttpStatus status;
  private final String message;
  private final T response;
}
