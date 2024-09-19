package com._P.eureka.client.ai.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Getter
@AllArgsConstructor
@NoArgsConstructor
public class Gemini {
  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long id;

  @Column(name = "request_contents")
  private String requestContents;
  @Column(name = "response_contents")
  private String responseContents;


  public Gemini(String request, String response) {
    this.requestContents = request;
    this.responseContents = response;
  }
}
