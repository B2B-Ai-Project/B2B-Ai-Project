package com._P.eureka.client.ai.controller;

import com._P.eureka.client.ai.dto.GeminiRequest;
import com._P.eureka.client.ai.dto.GeminiResponse;
import com._P.eureka.client.ai.service.GeminiService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
public class GeminiController {
  private final GeminiService geminiService;


  @PostMapping("/api/ai")
  public GeminiResponse getGeminiCompletion(
          @RequestBody
          GeminiRequest request
  ) {
    // 서비스에서 Gemini API 호출하고 응답 받기
    return geminiService.RequestAndResponse(request);
  }

}