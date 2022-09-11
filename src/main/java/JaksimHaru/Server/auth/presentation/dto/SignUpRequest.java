package JaksimHaru.Server.auth.presentation.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;

@Data
public class SignUpRequest {

    @Schema(type = "string", example = "string", description = "이름")
    @NotBlank
    private String name;

    @Schema(type = "string", example = "string@naver.com", description = "이메일")
    @NotBlank
    @Email
    private String email;

    @Schema(type = "string", example = "string", description = "비밀번호")
    @NotBlank
    private String password;

}
