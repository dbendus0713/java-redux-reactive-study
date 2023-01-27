package com.dyyoo.webfluxstudy.router;

import com.dyyoo.webfluxstudy.entity.Customer;
import com.dyyoo.webfluxstudy.handler.CustomerHandler;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.enums.ParameterIn;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.parameters.RequestBody;
import org.springdoc.core.annotations.RouterOperation;
import org.springdoc.core.annotations.RouterOperations;
import org.springframework.boot.autoconfigure.web.WebProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.reactive.function.server.RouterFunction;
import org.springframework.web.reactive.function.server.RouterFunctions;
import org.springframework.web.reactive.function.server.ServerResponse;

import static org.springframework.http.MediaType.APPLICATION_JSON;
import static org.springframework.web.reactive.function.server.RequestPredicates.*;

@Configuration
public class CustomerRouterFunction {

  @Bean
  public WebProperties.Resources resources() {
    return new WebProperties.Resources();
  }

  @RouterOperations({
      @RouterOperation(
          path = "/router/r2customers",
          method = RequestMethod.GET,
          beanClass = CustomerHandler.class,
          beanMethod = "getCustomers"),
      @RouterOperation(
          path = "/router/r2customers/{id}",
          method = RequestMethod.GET,
          beanClass = CustomerHandler.class,
          beanMethod = "getCustomer",
          operation = @Operation(operationId = "getCustomer",
              parameters = {@Parameter(in = ParameterIn.PATH, name = "id")})),
      @RouterOperation(
      path = "/router/r2customers",
      method = RequestMethod.POST,
      beanClass = CustomerHandler.class,
      beanMethod = "saveCustomer",
      operation = @Operation(operationId = "saveCustomer",
          requestBody = @RequestBody(content = @Content(schema = @Schema(implementation = Customer.class))))),
      @RouterOperation(
          path = "/router/r2customers",
          method = RequestMethod.PUT,
          beanClass = CustomerHandler.class,
          beanMethod = "saveCustomer",
          operation = @Operation(operationId = "updateCustomer",
              requestBody = @RequestBody(content = @Content(schema = @Schema(implementation = Customer.class))))),
      @RouterOperation(
          path = "/router/r2customers/{id}",
          method = RequestMethod.DELETE,
          beanClass = CustomerHandler.class,
          beanMethod = "deleteCustomer",
          operation = @Operation(operationId = "deleteCustomer",
              parameters = {@Parameter(in = ParameterIn.PATH, name = "id")})),
  })
  @Bean
  public RouterFunction<ServerResponse> routerFunction(CustomerHandler customerHandler) {
    return RouterFunctions.route(GET("/router/r2customers").and(accept(APPLICATION_JSON)), customerHandler::getCustomers)
        .andRoute(GET("/router/r2customers/{id}").and(accept(APPLICATION_JSON)), customerHandler::getCustomer)
        .andRoute(POST("/router/r2customers").and(accept(APPLICATION_JSON)), customerHandler::saveCustomer)
        .andRoute(PUT("/router/r2customers/{id}").and(accept(APPLICATION_JSON)), customerHandler::updateCustomer)
        .andRoute(DELETE("/router/r2customers/{id}").and(accept(APPLICATION_JSON)), customerHandler::deleteCustomer)
        ;
  }
}
