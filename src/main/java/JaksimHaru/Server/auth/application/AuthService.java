package JaksimHaru.Server.auth.application;

import JaksimHaru.Server.auth.presentation.dto.SignUpRequest;
import JaksimHaru.Server.common.dto.ApiResponse;
import JaksimHaru.Server.common.dto.Message;
import JaksimHaru.Server.common.exception.advice.assertThat.DefaultAssert;
import JaksimHaru.Server.member.domain.Member;
import JaksimHaru.Server.member.domain.Provider;
import JaksimHaru.Server.member.domain.Role;
import JaksimHaru.Server.member.domain.repository.MemberRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.net.URI;

@Transactional(readOnly = true)
@RequiredArgsConstructor
@Service
@Slf4j
public class AuthService {

    private final MemberRepository memberRepository;
    private final PasswordEncoder passwordEncoder;

    @Transactional
    public ResponseEntity<?> signUp(SignUpRequest signUpRequest) {
        DefaultAssert.isTrue(!memberRepository.existsByEmail(signUpRequest.getEmail()), "해당 이메일이 존재합니다.");

        Member member = Member.builder()
                            .name(signUpRequest.getName())
                            .email(signUpRequest.getEmail())
                            .password(passwordEncoder.encode(signUpRequest.getPassword()))
                            .provider(Provider.local)
                            .role(Role.USER)
                            .build();

        memberRepository.save(member);

        URI location = ServletUriComponentsBuilder
                .fromCurrentContextPath().path("/api/auth/")
                .buildAndExpand(member.getId()).toUri();
        ApiResponse apiResponse = ApiResponse.builder().check(true).information(Message.builder().message("회원가입에 성공하였습니다.").build()).build();

        return ResponseEntity.created(location).body(apiResponse);
    }

}
