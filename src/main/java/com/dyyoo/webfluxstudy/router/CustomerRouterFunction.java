package com.dyyoo.webfluxstudy.router;

import com.dyyoo.webfluxstudy.handler.CustomerHandler;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.reactive.function.server.RouterFunction;
import org.springframework.web.reactive.function.server.RouterFunctions;
import org.springframework.web.reactive.function.server.ServerResponse;

import static org.springframework.http.MediaType.APPLICATION_JSON;
import static org.springframework.web.reactive.function.server.RequestPredicates.GET;
import static org.springframework.web.reactive.function.server.RequestPredicates.accept;

@Configuration
public class CustomerRouterFunction {
  @Bean
  public RouterFunction<ServerResponse> routerFunction(CustomerHandler customerHandler) {
    return RouterFunctions.route(GET("/router/r2customers").and(accept(APPLICATION_JSON)), customerHandler::getCustomers)
//        .andRoute()
        ;
  }
}
