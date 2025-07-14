package gift.service;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import gift.dto.LoginRequestDto;
import gift.dto.MemberProfileDto;
import gift.dto.RegisterRequestDto;
import gift.dto.TokenResponse;
import gift.entity.Member;
import gift.exception.EmailAlreadyExistsException;
import gift.exception.LoginFailedException;
import gift.repository.MemberRepository;
import gift.security.JwtTokenProvider;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mindrot.jbcrypt.BCrypt;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.test.context.jdbc.Sql;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.verify;
import java.util.Optional;

@ExtendWith(MockitoExtension.class)
public class MemberServiceTest {

    @InjectMocks
    private MemberService memberService;

    @Mock
    private MemberRepository memberRepository;

    @Mock
    private JwtTokenProvider jwtTokenProvider;

    private RegisterRequestDto registerRequestDto;
    private LoginRequestDto loginRequestDto;
    private Member member;
    private String rawPassword="password";
    @BeforeEach
    void setUp() {
        registerRequestDto=new RegisterRequestDto();
        registerRequestDto.setEmail("test@email.com");
        registerRequestDto.setPassword(rawPassword);

        loginRequestDto=new LoginRequestDto();
        loginRequestDto.setEmail("test@email.com");
        loginRequestDto.setPassword(rawPassword);

        String hashPassword= BCrypt.hashpw(rawPassword, BCrypt.gensalt());
        member=new Member(1L,"test@email.com",hashPassword);
    }


    @Test
    void register_success() {
        given(memberRepository.findByEmail(registerRequestDto.getEmail())).willReturn(Optional.empty());
        given(memberRepository.save(any(Member.class))).willReturn(member);
        given(jwtTokenProvider.createToken(member.getId())).willReturn("test.token");
        TokenResponse tokenResponse = memberService.register(registerRequestDto);
        assertThat(tokenResponse.getToken()).isEqualTo("test.token");
        verify(memberRepository).save(any(Member.class));
    }

    @Test
    void register_fail_email_exists() {
        given(memberRepository.findByEmail(registerRequestDto.getEmail())).willReturn(Optional.of(member));
        assertThatThrownBy(() -> memberService.register(registerRequestDto))
                .isInstanceOf(EmailAlreadyExistsException.class)
                .hasMessageContaining("이미 가입된 이메일입니다");
    }

    @Test
    void login_success() {
        given(memberRepository.findByEmail(loginRequestDto.getEmail())).willReturn(Optional.of(member));
        given(jwtTokenProvider.createToken(member.getId())).willReturn("test.token");
        TokenResponse tokenResponse = memberService.login(loginRequestDto);
        assertThat(tokenResponse.getToken()).isEqualTo("test.token");
    }

    @Test
    void login_fail_unregistered_email() {
        given(memberRepository.findByEmail(loginRequestDto.getEmail())).willReturn(Optional.empty());
        assertThatThrownBy(() -> memberService.login(loginRequestDto))
                .isInstanceOf(LoginFailedException.class)
                .hasMessageContaining("가입되지 않은 이메일입니다.");
    }

    @Test
    void login_fail_password_mismatch() {
        loginRequestDto.setPassword("wrong_password");
        given(memberRepository.findByEmail(loginRequestDto.getEmail())).willReturn(Optional.of(member));
        assertThatThrownBy(() -> memberService.login(loginRequestDto))
                .isInstanceOf(LoginFailedException.class)
                .hasMessageContaining("비밀번호가 일치하지 않습니다.");
    }

    @Test
    void findMemberProfileById_success() {
        given(memberRepository.findById(member.getId())).willReturn(Optional.of(member));
        MemberProfileDto profileDto = memberService.findMemberProfileById(member.getId());
        assertThat(profileDto.id()).isEqualTo(member.getId());
        assertThat(profileDto.email()).isEqualTo(member.getEmail());
    }

    @Test
    void findMemberProfileById_fail_not_found() {
        Long nonExistentId = 999L;
        given(memberRepository.findById(nonExistentId)).willReturn(Optional.empty());
        assertThatThrownBy(() -> memberService.findMemberProfileById(nonExistentId))
                .isInstanceOf(IllegalArgumentException.class);
    }
}
