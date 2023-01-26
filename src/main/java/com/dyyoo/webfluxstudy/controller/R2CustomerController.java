package com.dyyoo.webfluxstudy.controller;

import com.dyyoo.webfluxstudy.entity.Customer;
import com.dyyoo.webfluxstudy.exception.CustomAPIException;
import com.dyyoo.webfluxstudy.repository.R2CustomerRepository;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.http.codec.ServerSentEvent;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import reactor.core.publisher.Sinks;

import java.time.Duration;

@RestController
@RequestMapping("/r2customers")
public class R2CustomerController {
  private final R2CustomerRepository customerRepository;
  private final Sinks.Many<Customer> customerSinksMany;

  public R2CustomerController(R2CustomerRepository customerRepository) {
    this.customerRepository = customerRepository;
    //Sinks.many() => Sinks.ManySpec
    //Sinks.many().multicast() => SInks.MulticastSpec
    //Sinks.many().multicast().onBackPressureBuffer() => Sinks.many
    this.customerSinksMany = Sinks.many().multicast().onBackpressureBuffer();
  }
  //text event stream type
  @GetMapping(produces = MediaType.TEXT_EVENT_STREAM_VALUE)
  public Flux<Customer> findAllCustomers() {
      return customerRepository.findAll().delayElements(Duration.ofSeconds(1)).log();
  }

  @GetMapping("/sse")
  public Flux<ServerSentEvent<Customer>> findAllCustomerSSE() {
    return customerSinksMany.asFlux()
        .mergeWith(customerRepository.findAll())
        .map(c -> ServerSentEvent.builder(c).build())
        .doOnCancel(() -> customerSinksMany.asFlux().blockLast())
        ;
  }

  @PostMapping
  public Mono<Customer> saveCustomer(@RequestBody Customer customer) {
    //tryEmitNest : Try emmitting a non-null element generating an onNext signal.
    return customerRepository.save(customer)
        .doOnNext(rst -> customerSinksMany.tryEmitNext(rst))
        .log();
  }

  @GetMapping("/{id}")
  public Mono<Customer> findCustomer(@PathVariable Long id) {
    return customerRepository.findById(id)
        .switchIfEmpty(Mono.error(new CustomAPIException("Customer not found with id for " + id, HttpStatus.NOT_FOUND)))
        .log();
  }

  @GetMapping("/name/{lastName}")
  public Flux<Customer> findCustomerByName(@PathVariable String lastName) {
    return customerRepository.findByLastName(lastName)
        .switchIfEmpty(Mono.error(new CustomAPIException("Customer not found with lastName for " + lastName, HttpStatus.NOT_FOUND)))
        .log()
        ;
  }

  @PutMapping("/{id}")
  public Mono<Customer> updateCustomer(@PathVariable Long id, @RequestBody Customer customer) {
    return customerRepository.findById(id)
        .flatMap(existCustomer -> {
          if (!existCustomer.getFirstName().equals(customer.getFirstName())) existCustomer.setFirstName(customer.getFirstName());
          if (!existCustomer.getLastName().equals(customer.getLastName())) existCustomer.setLastName(customer.getLastName());
          return customerRepository.save(existCustomer)
//              .doOnNext(rst -> customerSinksMany.)
              .log();
        })
        .switchIfEmpty(Mono.error(new CustomAPIException("Customer not found with id for " + id, HttpStatus.NOT_FOUND)))
        ;

  }

  @DeleteMapping("/{id}")
  public Mono<ResponseEntity<Object>> deleteCustomer(@PathVariable Long id) {
    return customerRepository.findById(id)
        .flatMap(v -> customerRepository.delete(v)
            .then(Mono.just(ResponseEntity.ok().build()))
        )
        .defaultIfEmpty(ResponseEntity.notFound().build())
        ;
  }



}
