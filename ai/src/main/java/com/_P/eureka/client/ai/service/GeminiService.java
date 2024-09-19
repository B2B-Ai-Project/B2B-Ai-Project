package com._P.eureka.client.ai.service;

import com._P.eureka.client.ai.dto.GeminiRequest;
import com._P.eureka.client.ai.dto.GeminiResponse;
import com._P.eureka.client.ai.entity.Gemini;
import com._P.eureka.client.ai.entity.GeminiRepository;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.*;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.Collections;

@Service
@RequiredArgsConstructor
public class GeminiService {

  private final GeminiRepository geminiRepository;
  private final RestTemplate restTemplate;
  private final ObjectMapper objectMapper;

  @Value("${googleai.api.key}")
  private String apiKey;

  // 서비스에서 Gemini API 호출하고 응답 받기
  public GeminiResponse RequestAndResponse(GeminiRequest request) {
    String uri = "https://generativelanguage.googleapis.com/v1beta/models/gemini-1.5-flash-latest:generateContent";

    HttpHeaders headers = new HttpHeaders();
    headers.setContentType(MediaType.APPLICATION_JSON); // Request Body를 사용
    headers.setAccept(Collections.singletonList(MediaType.APPLICATION_JSON));
    headers.set("x-goog-api-key", apiKey);

    HttpEntity<GeminiRequest> entity = new HttpEntity<>(request, headers);
    ResponseEntity<GeminiResponse> response = restTemplate.exchange(uri, HttpMethod.POST, entity, GeminiResponse.class);
    // 요청과 응답을 DB에 저장하는 메서드
    saveRequestResponse(request, response);

    return response.getBody();
  }


  // 요청과 응답을 DB에 저장하는 메서드
  private void saveRequestResponse(GeminiRequest request, ResponseEntity<GeminiResponse> response) {
    try {
      // 요청 내용을 JSON 문자열로 변환
      String requestString = objectMapper.writeValueAsString(request.getContents());

      // 응답 내용을 JSON 문자열로 변환
      String responseString = objectMapper.writeValueAsString(response.getBody());
      // JSON 문자열을 Java에서 사용 가능하도록 역직렬화
      GeminiResponse geminiResponse = objectMapper.readValue(responseString, GeminiResponse.class);
      String text = geminiResponse.getCandidates().get(0).getContent().getParts().get(0).getText();
      text = text.replaceAll("[\\r\\n*]", "").substring(0, 50);

      //DB에 저장
      geminiRepository.save(new Gemini(requestString, text));

    } catch (Exception e) {
      // 변환 또는 저장 중 에러가 발생하면 출력
      e.printStackTrace();
    }
  }

}