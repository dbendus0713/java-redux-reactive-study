package com.dyyoo.webfluxstudy.exception.handler;

import org.springframework.boot.autoconfigure.web.WebProperties;
import org.springframework.boot.autoconfigure.web.reactive.error.AbstractErrorWebExceptionHandler;
import org.springframework.boot.web.error.ErrorAttributeOptions;
import org.springframework.boot.web.reactive.error.ErrorAttributes;
import org.springframework.context.ApplicationContext;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.codec.ServerCodecConfigurer;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.BodyInserters;
import org.springframework.web.reactive.function.server.*;
import reactor.core.publisher.Mono;

import java.util.Map;

@Component
public class CustomAPIExceptionHandler extends AbstractErrorWebExceptionHandler {
  public CustomAPIExceptionHandler(ErrorAttributes errorAttributes,
                                   WebProperties.Resources resources,
                                   ApplicationContext applicationContext,
                                   ServerCodecConfigurer serverCodecConfigurer
                                   ) {
    super(errorAttributes, resources, applicationContext);
    this.setMessageReaders(serverCodecConfigurer.getReaders());
    this.setMessageWriters(serverCodecConfigurer.getWriters());
  }

  @Override
  protected RouterFunction<ServerResponse> getRoutingFunction(ErrorAttributes errorAttributes) {
    return RouterFunctions.route(RequestPredicates.all(), this::renderException);
  }

//  private Mono<ServerResponse> renderException(ServerRequest request) {
//    Map<String, Object> errorMap = this.getErrorAttributes(request, ErrorAttributeOptions.defaults());
//    errorMap.remove("requestId");
//    return ServerResponse.status(HttpStatus.BAD_REQUEST)
//        .contentType(MediaType.APPLICATION_JSON)
//        .body(BodyInserters.fromValue(errorMap));
//  }
  private Mono<ServerResponse> renderException(ServerRequest request) {
    Map<String, Object> errorMap = this.getErrorAttributes(request, ErrorAttributeOptions.defaults());
    errorMap.remove("requestId");

    System.out.println(errorMap.get("status").toString()); //Object -> String
    String statusMsg = errorMap.get("status").toString().substring(4); //400 BAD_REQUEST

    return ServerResponse.status(HttpStatus.valueOf(statusMsg))
        .contentType(MediaType.APPLICATION_JSON)
        .body(BodyInserters.fromValue(errorMap));
  }
}
