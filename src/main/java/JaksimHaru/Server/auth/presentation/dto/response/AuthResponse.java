package JaksimHaru.Server.auth.presentation.dto.response;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Builder;
import lombok.Data;

@Data
public class AuthResponse {

    @Schema(type = "string", example = "eyJhbGciOiJIUzI1NiJ9.eyJleHAiOjE2NjQxMDA4Njd9.JsOfD8Xf7ayjKGa2jUZgDaZ82E2WYCqNbnxuqoI_Yn8", description = "access token")
    private String accessToken;

    @Schema(type = "string", example = "eyJhbGciOiJIUzI1NiJ9.eyJleHAiOjE2NjQxMDA4Njd9.JsOfD8Xf7ayjKGa2jUZgDaZ82E2WYCqNbnxuqoI_Yn8", description = "refresh token")
    private String refreshToken;

    @Schema(type = "string", example = "Bearer", description = "권한(Authorization) 값 헤더 명칭 지정")
    private String tokenType = "Bearer";

    public AuthResponse(){}

    @Builder
    public AuthResponse(String accessToken, String refreshToken) {
        this.accessToken = accessToken;
        this.refreshToken = refreshToken;
    }

}
