package com.dyyoo.webfluxstudy.exception;

import org.springframework.http.HttpStatus;

public class CustomAPIException extends Exception{
  private HttpStatus httpStatus;

  public CustomAPIException(String message) {
    //417
    this(message, HttpStatus.EXPECTATION_FAILED);
  }

  public CustomAPIException(String message, HttpStatus httpStatus) {
    super(message);
    this.httpStatus = httpStatus;
  }

  public HttpStatus getHttpStatus() {

    return httpStatus;
  }
}
