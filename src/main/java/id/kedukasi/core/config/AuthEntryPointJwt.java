package id.kedukasi.core.config;

import java.io.IOException;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.google.gson.Gson;
import id.kedukasi.core.models.Result;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.AuthenticationEntryPoint;
import org.springframework.stereotype.Component;


@Component
public class AuthEntryPointJwt implements AuthenticationEntryPoint {

  private static final Logger logger = LoggerFactory.getLogger(AuthEntryPointJwt.class);

  private Result result;
  private Gson gson = new Gson();

  @Override
  public void commence(HttpServletRequest request,
                       HttpServletResponse response,
                       AuthenticationException authException) throws IOException, ServletException {
    final String expired = (String) request.getAttribute("expired");
    if (expired != null) {
      result = new Result();
      result.setCode(400);
      result.setSuccess(false);
      result.setMessage("Error: Token kadaluarsa");
      String resultString = this.gson.toJson(result);

      response.resetBuffer();
      response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
      response.setHeader("Content-Type", "application/json");
      response.getOutputStream().print(resultString);
      response.flushBuffer();
    } else {
        result = new Result();
        result.setCode(400);
        result.setSuccess(false);
        result.setMessage("Error: Token salah");
        String resultString = this.gson.toJson(result);

        response.resetBuffer();
        response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
        response.setHeader("Content-Type", "application/json");
        response.getOutputStream().print(resultString);
        response.flushBuffer();
    }
  }
}
