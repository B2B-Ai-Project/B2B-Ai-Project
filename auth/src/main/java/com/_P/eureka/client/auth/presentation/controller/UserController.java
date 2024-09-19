package com._P.eureka.client.auth.presentation.controller;

import com._P.eureka.client.auth.application.dto.UserResponseDto;
import com._P.eureka.client.auth.application.dto.UserUpdateDto;
import com._P.eureka.client.auth.application.service.UserService;
import com._P.eureka.client.auth.domain.model.User;
import com._P.eureka.client.auth.domain.model.UserRoleEnum;
import com._P.eureka.client.auth.infrastructure.security.UserDetailsImpl;
import io.swagger.v3.oas.annotations.Operation;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;
import java.util.UUID;

@RestController
@RequestMapping("/api")
@RequiredArgsConstructor
public class UserController {

    private final UserService userService;

    // user 탈퇴
    @DeleteMapping("/master/{user_id}")
    @Operation(summary = "회원탈퇴 API", description = "User을 논리적으로 삭제합니다.")
    public ResponseEntity<String> deleteUser(@PathVariable("user_id") UUID userId,
                                             @AuthenticationPrincipal UserDetailsImpl userDetails) {

        if (userDetails == null) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Unauthorized: userDetails is null");
        }

        // 요청한 id와 로그인한 id가 일치하는지 검증, role이 master가 아닐경우 403
        if(!userDetails.getUser().getUserId().equals(userId) &
                userDetails.getUser().getRole() != UserRoleEnum.MASTER){
            return ResponseEntity.status(HttpStatus.FORBIDDEN).body("권한이 없습니다.");
        }

        return userService.deleteUser(userId);
    }


    // 회원 정보수정
    @PutMapping("/master/{user_id}")
    @Operation(summary = "회원정보수정 API", description = "User를 update 합니다.")
    public ResponseEntity<UserResponseDto> updateUser(
            @PathVariable UUID userId,
            @RequestBody UserUpdateDto requestDto,
            @AuthenticationPrincipal UserDetailsImpl userDetails) {

        // 마스터 권한 체크
        if (!userDetails.getUser().getUserId().equals(userId) &
                userDetails.getUser().getRole() != UserRoleEnum.MASTER) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN).body(null);
        }

        // 서비스에서 업데이트 처리 후 ResponseEntity로 반환
        return userService.updateUser(userId, requestDto);
    }

    // 내 정보 조회 (로그인한 사용자 정보 반환)
    @GetMapping("/master/user")
    @Operation(summary = "내 정보 조회 API", description = "로그인한 사용자의 정보를 조회합니다.")
    public ResponseEntity<UserResponseDto> getMyInfo(@AuthenticationPrincipal UserDetailsImpl userDetails) {
        User user = userDetails.getUser();
        return ResponseEntity.ok(new UserResponseDto(user));
    }

    // 회원 단일 조회 (관리자가 특정 사용자 정보 조회)
    @GetMapping("/master/{user_id}")
    @Operation(summary = "회원 단일 조회 API", description = "관리자가 특정 사용자의 정보를 조회합니다.")
    public ResponseEntity<UserResponseDto> getUserById(@PathVariable UUID userId,
                                                       @AuthenticationPrincipal UserDetailsImpl userDetails) {
        // 마스터 권한 체크
        if (userDetails.getUser().getRole() != UserRoleEnum.MASTER) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN).build();
        }

        UserResponseDto userResponse = userService.getUserById(userId);
        return ResponseEntity.ok(userResponse);
    }




    // 회원 목록 조회 (페이징 및 정렬)
    @GetMapping("/master/list")
    @Operation(summary = "회원목록 조회 API", description = "회원목록을 페이징 및 정렬하여 조회합니다.")
    public ResponseEntity<Page<UserResponseDto>> getUserList(
            @PageableDefault(size = 10, sort = "username", direction = Sort.Direction.ASC) Pageable pageable,
            @AuthenticationPrincipal UserDetailsImpl userDetails) {

        // 마스터 권한 체크
        if (userDetails.getUser().getRole() != UserRoleEnum.MASTER) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN).body(null);
        }

        return ResponseEntity.ok(userService.getUserList(pageable));
    }

    // 배송 담당자 생성 검증
    @PostMapping("/create/deliveryPerson/{userId}")
    public String createDeliveryPerson(
            @PathVariable UUID userId
    ){
        // 검증 완료 시 slackId 반환
        return userService.checkUser(userId);
    }





}
