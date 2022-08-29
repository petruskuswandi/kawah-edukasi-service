package id.kedukasi.core.models;

import java.util.Arrays;


public class Result {

  private String code;
  private boolean success;
  private String message;
  private Object data;

  public Result() {
    this.code = "";
    this.success = true;
    this.message = "success";
    this.data = Arrays.asList();
  }

  public String getCode() {
    return code;
  }

  public void setCode(String code) {
    this.code = code;
  }

  public boolean isSuccess() {
    return success;
  }

  public void setSuccess(boolean success) {
    this.success = success;
  }

  public String getMessage() {
    return message;
  }

  public void setMessage(String message) {
    this.message = message;
  }

  public Object getData() {
    return data;
  }

  public void setData(Object data) {
    this.data = data;
  }

}
