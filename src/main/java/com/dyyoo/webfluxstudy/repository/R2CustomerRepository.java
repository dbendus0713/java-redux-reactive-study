package com.dyyoo.webfluxstudy.repository;

import com.dyyoo.webfluxstudy.entity.Customer;
import org.springframework.data.r2dbc.repository.Query;
import org.springframework.data.repository.reactive.ReactiveCrudRepository;
import org.springframework.stereotype.Repository;
import reactor.core.publisher.Flux;

//@Repository
public interface R2CustomerRepository extends ReactiveCrudRepository<Customer, Long> {

  @Query("select * from customer where last_name = :lastName")
  Flux<Customer> findByLastName(String lastName);
}
