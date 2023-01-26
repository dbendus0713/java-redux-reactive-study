package com.dyyoo.webfluxstudy;

import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;
import reactor.core.publisher.Mono;
import reactor.test.StepVerifier;

public class MonoTest2 {
  @Test
  public void justMono() {
    Mono<String> stringMn = Mono.just("Webflux test")
        .map(msg -> msg.concat(".com"))
        .map(msg -> msg.toUpperCase())
        .log() // 운영에선 log는 성능저하
    ;
//    stringMn.subscribe(System.out::println);

    StepVerifier.create(stringMn)
        .expectNext("WEBFLUX TEST.COM")
        .verifyComplete()
    ;
  }

  @Test
  @Tag("error test")
  public void errorMono() {
    Mono<String> errorMono = Mono.error(new RuntimeException("Check Error Mono"));

    errorMono.subscribe(
        value -> {
          System.out.println("onNext " + value);
        },
        error -> {
          System.out.println("onError " + error.getMessage());
        },
        () -> {
          System.out.println("OnComplete ");
        }
    );

    StepVerifier.create(errorMono)
        .expectError(RuntimeException.class)
        .verify();
  }

  @Test
  public void fromSupplier() {
    //Supplier<String> StrSupplier = () -> "Supplier Message";
    Mono<String> stringMono = Mono.fromSupplier(() -> "Supplier Message").log();
    stringMono.subscribe(System.out::println);

    StepVerifier.create(stringMono)
        .expectNext("Supplier Message")
        .verifyComplete();
    //verifycomplete = expectcomplete + verify
    StepVerifier.create(stringMono)
        .expectNext("Supplier Message")
        .expectComplete()
        .verify();
  }

}
