package com._P.eureka.client.auth.application.service;


import com._P.eureka.client.auth.application.dto.UserResponseDto;
import com._P.eureka.client.auth.application.dto.UserUpdateDto;
import com._P.eureka.client.auth.domain.model.User;
import com._P.eureka.client.auth.domain.model.UserRoleEnum;
import com._P.eureka.client.auth.domain.repository.UserRepository;
import jakarta.persistence.EntityNotFoundException;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.UUID;

@Service
@RequiredArgsConstructor
public class UserService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;


    // 탈퇴
    @Transactional
    public ResponseEntity<String> deleteUser(UUID userId) {
        // 사용자 조회
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new EntityNotFoundException("해당 사용자를 찾을 수 없습니다."));

        // 빌더를 사용하여 isDeleted 상태 변경
        User deletedUser = user.deleteUser();

        // 논리 삭제된 사용자 저장
        userRepository.save(deletedUser);

        return ResponseEntity.ok("탈퇴가 완료되었습니다.");
    }

    // 정보 수정
    @Transactional
    public ResponseEntity<UserResponseDto> updateUser(UUID userId, UserUpdateDto requestDto) {

        // 사용자 조회
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new EntityNotFoundException("해당 사용자를 찾을 수 없습니다."));

        // 빌더를 통한 업데이트 (null 필드 처리 및 암호화 처리 포함)
        User updatedUser = user.updateUser(requestDto, passwordEncoder);

        // 수정된 사용자 저장
        userRepository.save(updatedUser);

        // ResponseDto 반환
        return ResponseEntity.ok(new UserResponseDto(updatedUser));
    }

    // 회원 목록 조회 (페이징 및 정렬)
    public Page<UserResponseDto> getUserList(Pageable pageable) {
        Page<User> users = userRepository.findAll(pageable);
        return users.map(UserResponseDto::new);
    }

    // 회원 단일 조회 (관리자가 특정 사용자 정보를 조회)
    @Transactional
    public UserResponseDto getUserById(UUID userId) {
        // userId로 사용자를 조회, 없을 경우 예외 발생
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new EntityNotFoundException("해당 사용자를 찾을 수 없습니다."));

        // 조회된 사용자를 UserResponseDto로 변환하여 반환
        return new UserResponseDto(user);
    }


    // 배송 담당자 생성 검증
    public String checkUser(UUID userId) {
        User user = userRepository.findById(userId).orElseThrow(()->
                new IllegalArgumentException("존재하지 않는 유저 입니다."));

        if (!user.getRole().equals(UserRoleEnum.DELIVERY_PERSON)){
            throw new IllegalArgumentException("잘못된 권한 접근 입니다.");
        }
        // DeliveryPerson Application 에서 email(SlackId)가 필요하다
        return user.getEmail();
    }



}
