/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package id.kedukasi.core.utils;

import java.io.IOException;
import java.io.PrintWriter;
import java.io.StringWriter;
import java.util.Map;
import java.util.Random;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;


@Component
public class StringUtil {

  private final Logger logger = LoggerFactory.getLogger(this.getClass());

  public String getError(Exception e) {
    StringWriter errors = new StringWriter();
    String result = "";
    try {
      e.printStackTrace(new PrintWriter(errors));
      result = errors.toString();
    } catch (Exception ex) {
      logger.error(getError(ex));
    } finally {
      try {
        errors.close();
      } catch (IOException ex) {
        logger.error(getError(ex));
      }
    }

    return result;
  }

  public String getLogParam(HttpServletRequest request) {
    StringBuilder param = new StringBuilder();
    String result = "";
    try {
      Map<String, String[]> mapParam = request.getParameterMap();
      int i = 0;
      for (Map.Entry<String, String[]> p : mapParam.entrySet()) {
        param.append(i > 0 ? "&" : "").append(p.getKey()).append("=").append(p.getValue()[0]);
        i++;
      }
      result = "<< " + request.getRequestURI() + "?" + param + " >>";
    } catch (Exception e) {
      logger.error(getError(e));
    } finally {
      try {
        request.logout();
      } catch (ServletException ex) {
        logger.error(getError(ex));
      }
    }

    return result;
  }
  
  public static String getRandomNumberString() {
    Random rnd = new Random();
    int number = rnd.nextInt(999999);

    return String.format("%06d", number);
}

}
