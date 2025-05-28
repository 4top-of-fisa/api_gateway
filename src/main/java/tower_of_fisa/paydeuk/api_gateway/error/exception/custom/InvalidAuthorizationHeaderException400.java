package tower_of_fisa.paydeuk.api_gateway.error.exception.custom;

import tower_of_fisa.paydeuk.api_gateway.error.ErrorDefineCode;
import tower_of_fisa.paydeuk.api_gateway.error.exception.BasicCustomException500;

public class InvalidAuthorizationHeaderException400 extends BasicCustomException500 {
  public InvalidAuthorizationHeaderException400(ErrorDefineCode code) {
    super(code);
  }
}
