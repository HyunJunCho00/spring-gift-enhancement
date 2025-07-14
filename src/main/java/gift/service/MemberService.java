package gift.service;

import gift.dto.LoginRequestDto;
import gift.dto.MemberProfileDto;
import gift.dto.RegisterRequestDto;
import gift.dto.TokenResponse;
import gift.entity.Member;
import gift.exception.EmailAlreadyExistsException;
import gift.exception.LoginFailedException;
import gift.repository.MemberRepository;
import gift.security.JwtTokenProvider;
import org.mindrot.jbcrypt.BCrypt;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
public class MemberService {
    private final MemberRepository memberRepository;
    private final JwtTokenProvider jwtTokenProvider;

    public MemberService(MemberRepository memberRepository, JwtTokenProvider jwtTokenProvider) {
        this.memberRepository = memberRepository;
        this.jwtTokenProvider = jwtTokenProvider;
    }

    public TokenResponse register(RegisterRequestDto request) {
        memberRepository.findByEmail(request.getEmail()).ifPresent(m-> {
            throw new EmailAlreadyExistsException("이미 가입된 이메일입니다: " + request.getEmail());
        });

        String encodedPassword = BCrypt.hashpw(request.getPassword(), BCrypt.gensalt());
        Member member = new Member(request.getEmail(), encodedPassword);
        Member savedMember = memberRepository.save(member);

        String token = jwtTokenProvider.createToken(savedMember.getId());
        return new TokenResponse(token);
    }

    @Transactional(readOnly = true)
    public TokenResponse login(LoginRequestDto request) {
        Member member = memberRepository.findByEmail(request.getEmail())
                .orElseThrow(() -> new LoginFailedException("가입되지 않은 이메일입니다."));

        if (!BCrypt.checkpw(request.getPassword(), member.getPassword())) {
            throw new LoginFailedException("비밀번호가 일치하지 않습니다.");
        }
        String token = jwtTokenProvider.createToken(member.getId());
        return new TokenResponse(token);
    }

    @Transactional(readOnly = true)
    public MemberProfileDto findMemberProfileById(Long memberId) {
        Member member = memberRepository.findById(memberId)
                .orElseThrow(() -> new IllegalArgumentException("사용자를 찾을 수 없습니다. ID: " + memberId));
        return new MemberProfileDto(member.getId(),member.getEmail());
    }
}
