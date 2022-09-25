package JaksimHaru.Server.member.presentation.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import javax.validation.constraints.NotBlank;

@Data
public class ChangePasswordRequest {

    @Schema(type = "string", example = "string", description = "기존 비밀번호")
    @NotBlank
    private String oldPassword;

    @Schema(type = "string", example = "string123", description = "신규 비밀번호")
    @NotBlank
    private String newPassword;

    @Schema(type = "string", example = "string123", description = "신규 비밀번호 확인")
    @NotBlank
    private String reNewPassword;

}
