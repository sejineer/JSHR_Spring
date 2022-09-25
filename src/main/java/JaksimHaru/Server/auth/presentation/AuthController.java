package JaksimHaru.Server.auth.presentation;

import JaksimHaru.Server.auth.application.AuthService;
import JaksimHaru.Server.auth.presentation.dto.request.RefreshTokenRequest;
import JaksimHaru.Server.auth.presentation.dto.request.SignInRequest;
import JaksimHaru.Server.auth.presentation.dto.request.SignUpRequest;
import JaksimHaru.Server.auth.presentation.dto.response.AuthResponse;
import JaksimHaru.Server.common.config.token.CurrentUser;
import JaksimHaru.Server.common.config.token.UserPrincipal;
import JaksimHaru.Server.common.dto.Message;
import JaksimHaru.Server.common.exception.advice.payload.ErrorResponse;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

@Tag(name = "Authorization", description = "Authorization API")
@RequiredArgsConstructor
@RestController
@RequestMapping("/api/auth")
public class AuthController {

    private final AuthService authService;

    @Operation(summary = "유저 회원가입", description = "유저 회원가입")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "회원가입 성공", content = {@Content(mediaType = "application/json", schema = @Schema(implementation = Message.class))}),
            @ApiResponse(responseCode = "400", description = "회원가입 실패", content = {@Content(mediaType = "application/json", schema = @Schema(implementation = ErrorResponse.class))})
    })
    @PostMapping("/signup")
    public ResponseEntity<?> signUp(
            @Parameter(description = "Schemas => SignUpRequest", required = true) @Valid @RequestBody SignUpRequest signUpRequest
    ) {
        return authService.signUp(signUpRequest);
    }

    @Operation(summary = "유저 로그인", description = "유저 로그인")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "유저 로그인 성공", content = {@Content(mediaType = "application/json", schema = @Schema(implementation = AuthResponse.class))}),
            @ApiResponse(responseCode = "400", description = "유저 로그인 실패", content = {@Content(mediaType = "application/json", schema = @Schema(implementation = ErrorResponse.class))})
    })
    @PostMapping("/signin")
    public ResponseEntity<?> signIn(@Parameter(description = "Schemas => SignInRequest", required = true) @Valid @RequestBody SignInRequest signInRequest) {
        return authService.signIn(signInRequest);
    }

    @Operation(summary = "토큰 갱신", description = "신규 토큰을 갱신합니다.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "토큰 갱신 성공", content = {@Content(mediaType = "application/json", schema = @Schema(implementation = AuthResponse.class))}),
            @ApiResponse(responseCode = "400", description = "토큰 갱신 실패", content = {@Content(mediaType = "application/json", schema = @Schema(implementation = ErrorResponse.class))})
    })
    @PostMapping("/refresh")
    public ResponseEntity<?> refresh(@Parameter(description = "Schemas => RefreshTokenRequest", required = true) @Valid @RequestBody RefreshTokenRequest tokenRefreshRequest) {
        return authService.refresh(tokenRefreshRequest);
    }

    @Operation(summary = "유저 로그아웃", description = "유저 로그아웃")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "로그아웃 성공", content = {@Content(mediaType = "application/json", schema = @Schema(implementation = Message.class))}),
            @ApiResponse(responseCode = "400", description = "로그아웃 실패", content = {@Content(mediaType = "application/json", schema = @Schema(implementation = ErrorResponse.class))})
    })
    @PostMapping("/signout")
    public ResponseEntity<?> signOut(
            @Parameter(description = "AccessToken", required = true) @CurrentUser UserPrincipal userPrincipal,
            @Parameter(description = "Schemas => RefreshTokenRequest", required = true) @Valid @RequestBody RefreshTokenRequest tokenRefreshRequest
    ) {
        return authService.signOut(tokenRefreshRequest);
    }

}
