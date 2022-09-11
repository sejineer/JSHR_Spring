package JaksimHaru.Server.auth.presentation.dto.request;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Builder;
import lombok.Data;

import javax.validation.constraints.NotBlank;

@Data
public class RefreshTokenRequest {

    @Schema(type = "string", example = "eyJhbGciOiJIUzI1NiJ9.eyJleHAiOjE2NjQxMTcxNTN9.7As3wv2RuEWawpbrqxclXM58awYt9JSFT-Or8ZK5Nys", description = "refresh token")
    @NotBlank
    private String refreshToken;

    public RefreshTokenRequest(){}

    @Builder
    public RefreshTokenRequest(String refreshToken) {
        this.refreshToken = refreshToken;
    }
}
