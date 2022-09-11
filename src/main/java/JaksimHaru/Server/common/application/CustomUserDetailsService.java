package JaksimHaru.Server.common.application;

import JaksimHaru.Server.common.config.token.UserPrincipal;
import JaksimHaru.Server.common.exception.advice.assertThat.DefaultAssert;
import JaksimHaru.Server.member.domain.Member;
import JaksimHaru.Server.member.domain.repository.MemberRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

@Transactional(readOnly = true)
@RequiredArgsConstructor
@Service
public class CustomUserDetailsService implements UserDetailsService {

    private final MemberRepository memberRepository;

    @Override
    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {

        Member member = memberRepository.findByEmail(email)
                .orElseThrow(() ->
                        new UsernameNotFoundException("유저 정보를 찾을 수 없습니다."));

        return UserPrincipal.create(member);
    }

    public UserDetails loadUserById(Long id) {
        Optional<Member> member = memberRepository.findById(id);
        DefaultAssert.isOptionalPresent(member);

        return UserPrincipal.create(member.get());
    }
}
