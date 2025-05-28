package tower_of_fisa.paydeuk.api_gateway.error.exception.custom;

import tower_of_fisa.paydeuk.api_gateway.error.ErrorDefineCode;
import tower_of_fisa.paydeuk.api_gateway.error.exception.BasicCustomException500;

public class NoSuchRefreshTokenCookieException404 extends BasicCustomException500 {
  public NoSuchRefreshTokenCookieException404(ErrorDefineCode code) {
    super(code);
  }
}
