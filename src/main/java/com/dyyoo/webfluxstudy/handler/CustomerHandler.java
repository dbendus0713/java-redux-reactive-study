package com.dyyoo.webfluxstudy.handler;

import com.dyyoo.webfluxstudy.entity.Customer;
import com.dyyoo.webfluxstudy.repository.R2CustomerRepository;
import lombok.AllArgsConstructor;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.server.ServerRequest;
import org.springframework.web.reactive.function.server.ServerResponse;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@Service
@AllArgsConstructor
public class CustomerHandler {
  private final R2CustomerRepository customerRepository;

//  public CustomerHandler(R2CustomerRepository customerRepository) {
//    this.customerRepository = customerRepository;
//  }

  public Mono<ServerResponse> getCustomers(ServerRequest request) {
    Flux<Customer> customerFlux = customerRepository.findAll();
    return ServerResponse.ok()
        .contentType(MediaType.APPLICATION_JSON)
        .body(customerFlux, Customer.class); //Mono<ServerResponse>
  }
}
