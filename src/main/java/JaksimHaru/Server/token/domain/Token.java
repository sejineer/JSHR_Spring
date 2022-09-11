package JaksimHaru.Server.token.domain;

import JaksimHaru.Server.common.domain.BaseEntity;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Entity
public class Token extends BaseEntity {

    @Id
    @Column(name = "user_email", nullable = false)
    private String userEmail;

    @Column(name = "refresh_token", length = 1024, nullable = false)
    private String refreshToken;

    public Token updateRefreshToken(String refreshToken) {
        this.refreshToken = refreshToken;
        return this;
    }

    @Builder
    public Token(String userEmail, String refreshToken) {
        this.userEmail = userEmail;
        this.refreshToken = refreshToken;
    }

}
