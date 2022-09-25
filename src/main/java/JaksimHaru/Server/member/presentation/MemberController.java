package JaksimHaru.Server.member.presentation;

import JaksimHaru.Server.member.presentation.dto.ChangePasswordRequest;
import JaksimHaru.Server.common.config.token.CurrentUser;
import JaksimHaru.Server.common.config.token.UserPrincipal;
import JaksimHaru.Server.common.dto.Message;
import JaksimHaru.Server.common.exception.advice.payload.ErrorResponse;
import JaksimHaru.Server.member.application.MemberService;
import JaksimHaru.Server.member.domain.Member;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

@Tag(name = "Users", description = "Users API")
@RequiredArgsConstructor
@RequestMapping("/api/users")
@RestController
public class MemberController {

    private final MemberService memberService;

    @Operation(summary = "유저 정보 확인", description = "현재 접속된 유저정보를 확인합니다.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "유저 확인 성공", content = {@Content(mediaType = "application/json", schema = @Schema(implementation = Member.class))}),
            @ApiResponse(responseCode = "400", description = "유저 확인 실패", content = {@Content(mediaType = "application/json", schema = @Schema(implementation = ErrorResponse.class))})
    })
    @GetMapping("/me")
    public ResponseEntity<?> me(
            @Parameter(description = "AccessToken을 입력해주세요", required = true) @CurrentUser UserPrincipal userPrincipal
    ) {
        return memberService.me(userPrincipal);
    }

    @Operation(summary = "유저 정보 갱신", description = "현재 접속된 유저의 비밀번호를 새로 지정합니다.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "유저 정보 갱신 성공", content = {@Content(mediaType = "application/json", schema = @Schema(implementation = Message.class))}),
            @ApiResponse(responseCode = "400", description = "유저 정보 갱신 실패", content = {@Content(mediaType = "application/json", schema = @Schema(implementation = ErrorResponse.class))})
    })
    @PutMapping("/me")
    public ResponseEntity<?> changePassword(
            @Parameter(description = "AccessToken을 입력해주세요", required = true) @CurrentUser UserPrincipal userPrincipal,
            @Parameter(description = "Schema => ChangePasswordRequest", required = true) @Validated @RequestBody ChangePasswordRequest changePasswordRequest
    ) {
        return memberService.changePassword(userPrincipal, changePasswordRequest);
    }


}
