package com.dyyoo.webfluxstudy.handler;

import com.dyyoo.webfluxstudy.entity.Customer;
import com.dyyoo.webfluxstudy.exception.CustomAPIException;
import com.dyyoo.webfluxstudy.repository.R2CustomerRepository;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.BodyInserters;
import org.springframework.web.reactive.function.server.ServerRequest;
import org.springframework.web.reactive.function.server.ServerResponse;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@Service
@AllArgsConstructor
public class CustomerHandler {
  private final R2CustomerRepository customerRepository;

//  private Mono<ServerResponse> response404 = ServerResponse.notFound().build();
//  private Mono<ServerResponse> response406 = ServerResponse.status(HttpStatus.NOT_ACCEPTABLE).build();

//  public CustomerHandler(R2CustomerRepository customerRepository) {
//    this.customerRepository = customerRepository;
//  }

  public Mono<ServerResponse> getCustomers(ServerRequest request) {
    Flux<Customer> customerFlux = customerRepository.findAll();
    return ServerResponse.ok()
        .contentType(MediaType.APPLICATION_JSON)
        .body(customerFlux, Customer.class); //Mono<ServerResponse>
  }
  public Mono<ServerResponse> getCustomer(ServerRequest request) {
//    Mono<Customer> customerFlux = customerRepository.findById(Long.parseLong(request.pathVariable("id")))
//    return ServerResponse.ok()
//        .contentType(MediaType.APPLICATION_JSON)
//        .body(customerFlux, Customer.class); //Mono<ServerResponse>
    Long id = Long.parseLong(request.pathVariable("id"));
    return customerRepository.findById(id)
        .flatMap(customer -> ServerResponse.ok()
            .contentType(MediaType.APPLICATION_JSON)
            .body(BodyInserters.fromValue(customer)))
//            .body(customer, Customer.class))
//        .switchIfEmpty(response404)
        .switchIfEmpty(getError("Customer not found with id for ", HttpStatus.NOT_FOUND))
        ;
  }

  //등록
  public Mono<ServerResponse> saveCustomer(ServerRequest request) {
    Mono<Customer> customer = request.bodyToMono(Customer.class);
    return customer.flatMap(
        v -> {
          v.deleteId();
          return customerRepository.save(v)
              .flatMap(savedCustomer ->
                  ServerResponse.accepted()
                      .contentType(MediaType.APPLICATION_JSON)
                      .bodyValue(savedCustomer)
              ).switchIfEmpty(Mono.error(new CustomAPIException("Customer save error", HttpStatus.NOT_ACCEPTABLE)));
        }
    ).switchIfEmpty(getError("Customer not found with id for ", HttpStatus.NOT_FOUND))
    ;
  }

  //수정
  public Mono<ServerResponse> updateCustomer(ServerRequest request) {
    Long id = Long.parseLong(request.pathVariable("id"));
    Mono<Customer> updatedCustomerMono = request.bodyToMono(Customer.class);

    updatedCustomerMono.flatMap(customer ->
        customerRepository.findById(id)
            .flatMap(existCutomer -> {
              existCutomer.setFirstName(customer.getFirstName());
              existCutomer.setLastName(customer.getLastName());
              return customerRepository.save(existCutomer);
            })
    );
    return updatedCustomerMono.flatMap(customer ->
        ServerResponse.ok()
            .contentType(MediaType.APPLICATION_JSON)
            .body(BodyInserters.fromValue(customer))
    ).switchIfEmpty(getError("Customer save error", HttpStatus.NOT_ACCEPTABLE));
  }

  public Mono<ServerResponse> deleteCustomer(ServerRequest request) {
    Long id = Long.parseLong(request.pathVariable("id"));
    return customerRepository.findById(id)
        .flatMap(existCustomer ->
            ServerResponse
                .ok()
                .build(customerRepository.delete(existCustomer)))
        .switchIfEmpty(getError("Customer not found with id for ", HttpStatus.NOT_FOUND));
  }



  private Mono<? extends ServerResponse> getError(String msg, HttpStatus status) {
    return Mono.error(new CustomAPIException(msg, status));
  }
}
