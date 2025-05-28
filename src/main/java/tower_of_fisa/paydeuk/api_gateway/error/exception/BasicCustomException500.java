package tower_of_fisa.paydeuk.api_gateway.error.exception;

import lombok.Getter;
import tower_of_fisa.paydeuk.api_gateway.error.ErrorDefineCode;

@Getter
public class BasicCustomException500 extends RuntimeException {
  private final ErrorDefineCode code;

  public BasicCustomException500(ErrorDefineCode code) {
    super(code.getMessage());
    this.code = code;
  }


  public BasicCustomException500(Exception e) {
    super(e.getMessage());
    this.code = ErrorDefineCode.EXPIRED_TOKEN;
  }
}