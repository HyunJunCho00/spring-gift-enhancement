package gift.controller.api;

import gift.dto.LoginRequestDto;
import gift.dto.MemberProfileDto;
import gift.dto.RegisterRequestDto;
import gift.dto.TokenResponse;
import gift.service.MemberService;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/members")
public class MemberApiController {

    private final MemberService memberService;

    public MemberApiController(MemberService memberService) {
        this.memberService = memberService;
    }

    @PostMapping("/register")
    public ResponseEntity<TokenResponse> register(@RequestBody RegisterRequestDto request) {
        TokenResponse tokenResponse = memberService.register(request);
        return ResponseEntity.status(HttpStatus.CREATED).body(tokenResponse);
    }

    @PostMapping("/login")
    public ResponseEntity<TokenResponse> login(@RequestBody LoginRequestDto request) {
        TokenResponse tokenResponse = memberService.login(request);
        return ResponseEntity.ok(tokenResponse);
    }

    @GetMapping("/me")
    public ResponseEntity<MemberProfileDto> getMyInfo(HttpServletRequest request) {
        Long memberId = (Long) request.getAttribute("memberId");
        MemberProfileDto memberProfile = memberService.findMemberProfileById(memberId);
        return ResponseEntity.ok(memberProfile);
    }
}
