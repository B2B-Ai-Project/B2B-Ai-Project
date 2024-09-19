package com._P.eureka.client.ai.entity;

import com._P.eureka.client.ai.dto.GeminiRequest;
import com._P.eureka.client.ai.dto.GeminiResponse;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.service.annotation.HttpExchange;
import org.springframework.web.service.annotation.PostExchange;

@HttpExchange("/v1beta/models/")
public interface GeminiInterface {
  @PostExchange("gemini-1.5-flash-latest:generateContent")
  GeminiResponse getCompletion(
          @PathVariable String model,
          @RequestBody GeminiRequest request
  );
}
