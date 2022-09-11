package JaksimHaru.Server.common.dto;

import lombok.AccessLevel;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
public class TokenMapping {
    private String userEmail;
    private String accessToken;
    private String refreshToken;

    public TokenMapping(){}

    @Builder
    public TokenMapping(String userEmail, String accessToken, String refreshToken) {
        this.userEmail = userEmail;
        this.accessToken = accessToken;
        this.refreshToken = refreshToken;
    }
}
