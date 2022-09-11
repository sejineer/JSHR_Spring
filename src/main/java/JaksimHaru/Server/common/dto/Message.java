package JaksimHaru.Server.common.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Builder;
import lombok.Data;
import lombok.ToString;

@ToString
@Data
public class Message {

    @Schema(type = "string", example = "메세지 문구 출력", description = "메세지")
    private String message;

    public Message() {};

    @Builder
    public Message(String message) {
        this.message = message;
    }
}
