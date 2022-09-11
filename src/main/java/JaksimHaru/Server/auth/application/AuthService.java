package JaksimHaru.Server.auth.application;

import JaksimHaru.Server.auth.presentation.dto.request.RefreshTokenRequest;
import JaksimHaru.Server.auth.presentation.dto.request.SignInRequest;
import JaksimHaru.Server.auth.presentation.dto.request.SignUpRequest;
import JaksimHaru.Server.auth.presentation.dto.response.AuthResponse;
import JaksimHaru.Server.common.application.CustomTokenProviderService;
import JaksimHaru.Server.common.dto.ApiResponse;
import JaksimHaru.Server.common.dto.Message;
import JaksimHaru.Server.common.dto.TokenMapping;
import JaksimHaru.Server.common.exception.advice.assertThat.DefaultAssert;
import JaksimHaru.Server.member.domain.Member;
import JaksimHaru.Server.member.domain.Provider;
import JaksimHaru.Server.member.domain.Role;
import JaksimHaru.Server.member.domain.repository.MemberRepository;
import JaksimHaru.Server.token.domain.Token;
import JaksimHaru.Server.token.domain.repository.TokenRepository;
import io.swagger.v3.oas.annotations.Operation;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.net.URI;
import java.util.Optional;

@Transactional(readOnly = true)
@RequiredArgsConstructor
@Service
@Slf4j
public class AuthService {

    private final AuthenticationManager authenticationManager;
    private final PasswordEncoder passwordEncoder;
    private final CustomTokenProviderService customTokenProviderService;

    private final MemberRepository memberRepository;
    private final TokenRepository tokenRepository;

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

    @Transactional
    public ResponseEntity<?> signIn(SignInRequest signInRequest) {
        Authentication authentication = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(
                        signInRequest.getEmail(),
                        signInRequest.getPassword()
                )
        );

        SecurityContextHolder.getContext().setAuthentication(authentication);


        TokenMapping tokenMapping = customTokenProviderService.createToken(authentication);
        Token token = Token.builder()
                            .refreshToken(tokenMapping.getRefreshToken())
                            .userEmail(tokenMapping.getUserEmail())
                            .build();
        tokenRepository.save(token);
        AuthResponse authResponse = AuthResponse.builder().accessToken(tokenMapping.getAccessToken()).refreshToken(token.getRefreshToken()).build();

        return ResponseEntity.ok(authResponse);
    }

    @Transactional
    public ResponseEntity<?> refresh(RefreshTokenRequest tokenRefreshRequest) {
        //1차 검증
        boolean checkValid = valid(tokenRefreshRequest.getRefreshToken());
        DefaultAssert.isAuthentication(checkValid);

        Optional<Token> token = tokenRepository.findByRefreshToken(tokenRefreshRequest.getRefreshToken());
        Authentication authentication = customTokenProviderService.getAuthenticationByEmail(token.get().getUserEmail());

        //4.refresh token 정보 값을 업데이트 한다.
        //시간 유효성 확인
        TokenMapping tokenMapping;

        Long expirationTime = customTokenProviderService.getExpiration(tokenRefreshRequest.getRefreshToken());
        if (expirationTime > 0) {
            tokenMapping = customTokenProviderService.refreshToken(authentication, token.get().getRefreshToken());
        } else {
            tokenMapping = customTokenProviderService.createToken(authentication);
        }

        Token updateToken = token.get().updateRefreshToken(tokenMapping.getRefreshToken());
        tokenRepository.save(updateToken);

        AuthResponse authResponse = AuthResponse.builder().accessToken(tokenMapping.getAccessToken()).refreshToken(updateToken.getRefreshToken()).build();

        return ResponseEntity.ok(authResponse);
    }

    @Transactional
    public ResponseEntity<?> signout(RefreshTokenRequest tokenRefreshRequest) {
        boolean checkValid = valid(tokenRefreshRequest.getRefreshToken());
        DefaultAssert.isAuthentication(checkValid);

        //4. token 정보를 삭제한다.
        Optional<Token> token = tokenRepository.findByRefreshToken(tokenRefreshRequest.getRefreshToken());
        tokenRepository.delete(token.get());
        ApiResponse apiResponse = ApiResponse.builder().check(true).information(Message.builder().message("로그아웃 하였습니다").build()).build();

        return ResponseEntity.ok(apiResponse);
    }

    private boolean valid(String refreshToken) {

        //1. 토큰 형식 물리적 검증
        boolean validateCheck = customTokenProviderService.validateToken(refreshToken);
        DefaultAssert.isTrue(validateCheck, "Token 검증 실패");

        //2. refresh token 값 불러옴
        Optional<Token> token = tokenRepository.findByRefreshToken(refreshToken);
        DefaultAssert.isTrue(token.isPresent(), "탈퇴 처리된 회원입니다.");

        //3. email 값을 통해 인증값 불러옴
        Authentication authentication = customTokenProviderService.getAuthenticationByEmail(token.get().getUserEmail());
        DefaultAssert.isTrue(token.get().getUserEmail().equals(authentication.getName()), "사용자 인증에 실패하였습니다");

        return true;
    }
}
