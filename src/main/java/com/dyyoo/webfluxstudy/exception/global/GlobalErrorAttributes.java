package com.dyyoo.webfluxstudy.exception.global;

import com.dyyoo.webfluxstudy.exception.CustomAPIException;
import org.springframework.boot.web.error.ErrorAttributeOptions;
import org.springframework.boot.web.reactive.error.DefaultErrorAttributes;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.server.ServerRequest;

import java.util.HashMap;
import java.util.Map;

@Component
public class GlobalErrorAttributes extends DefaultErrorAttributes {

  @Override
  public Map<String, Object> getErrorAttributes(ServerRequest request, ErrorAttributeOptions options) {
    Map<String, Object> errorMap = new HashMap<>();
    Throwable error = super.getError(request);

    if (error instanceof CustomAPIException) {
      CustomAPIException customAPIException = (CustomAPIException) getError(request);
      errorMap.put("message", customAPIException.getMessage());
      errorMap.put("status_code", customAPIException.getHttpStatus().value());
      errorMap.put("Endpoint_Url", request.path());
    }
    return errorMap;
  }
}
