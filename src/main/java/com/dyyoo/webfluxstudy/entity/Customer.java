package com.dyyoo.webfluxstudy.entity;

import lombok.*;
import org.springframework.data.annotation.Id;
import org.springframework.data.relational.core.mapping.Column;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@EqualsAndHashCode(of = "id")
public class Customer {
  @Id
  private Long id;
  @Column
  private String lastName;
  @Column
  private String firstName;

  public Customer(String lastName, String firstName) {
    this.lastName = lastName;
    this.firstName = firstName;
  }

  @Override
  public String toString() {
    return "id: " + this.id + ", lastName: " + this.lastName + ", firstName: " + this.firstName;
  }


}
