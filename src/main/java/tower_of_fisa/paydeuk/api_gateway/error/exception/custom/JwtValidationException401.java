package tower_of_fisa.paydeuk.api_gateway.error.exception.custom;

import tower_of_fisa.paydeuk.api_gateway.error.exception.BasicCustomException500;

public class JwtValidationException401 extends BasicCustomException500 {
  public JwtValidationException401(Exception e) {
    super(e);
  }
}
