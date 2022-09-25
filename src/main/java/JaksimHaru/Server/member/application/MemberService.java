package JaksimHaru.Server.member.application;

import JaksimHaru.Server.member.presentation.dto.ChangePasswordRequest;
import JaksimHaru.Server.common.config.token.UserPrincipal;
import JaksimHaru.Server.common.dto.ApiResponse;
import JaksimHaru.Server.common.dto.Message;
import JaksimHaru.Server.common.exception.advice.assertThat.DefaultAssert;
import JaksimHaru.Server.member.domain.Member;
import JaksimHaru.Server.member.domain.repository.MemberRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

@Transactional(readOnly = true)
@RequiredArgsConstructor
@Service
public class MemberService {

    private final MemberRepository memberRepository;
    private final PasswordEncoder passwordEncoder;

    public ResponseEntity<?> me(UserPrincipal userPrincipal) {
        Optional<Member> member = memberRepository.findById(userPrincipal.getId());
        DefaultAssert.isOptionalPresent(member);

        ApiResponse apiResponse = ApiResponse.builder().check(true).information(member.get()).build();
        return ResponseEntity.ok(apiResponse);
    }

    @Transactional
    public ResponseEntity<?> changePassword(UserPrincipal userPrincipal, ChangePasswordRequest changePasswordRequest) {
        Optional<Member> member = memberRepository.findById(userPrincipal.getId());
        boolean passwordCheck = passwordEncoder.matches(changePasswordRequest.getOldPassword(), member.get().getPassword());
        DefaultAssert.isTrue(passwordCheck, "잘못된 비밀번호 입니다.");

        boolean newPasswordCheck = changePasswordRequest.getNewPassword().equals(changePasswordRequest.getReNewPassword());
        DefaultAssert.isTrue(newPasswordCheck, "신규 등록 비밀번호 값이 일치하지 않습니다.");

        String newPassword = passwordEncoder.encode(changePasswordRequest.getNewPassword());
        member.get().changePassword(newPassword);

        ApiResponse apiResponse = ApiResponse.builder().check(true).information(Message.builder().message("비밀번호가 변경되었습니다.")).build();
        return ResponseEntity.ok(apiResponse);
    }


}
