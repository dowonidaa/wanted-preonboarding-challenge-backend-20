package com.market.wanted.member.controller;

import com.market.wanted.member.dto.SignupRequest;
import com.market.wanted.member.dto.SignupResponse;
import com.market.wanted.member.service.MemberService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping("/api/members")
public class MemberRestController {

    private final MemberService memberService;

    @PostMapping("/signup")
    public ResponseEntity<SignupResponse> joinMember(@RequestBody @Validated SignupRequest request, BindingResult result) {
        if (result.hasErrors()) {
            return ResponseEntity.badRequest().body(new SignupResponse("회원 가입 실패"));
        }
        memberService.joinMember(request);
        return ResponseEntity.ok().body(new SignupResponse("회원 가입 성공"));
    }




}
