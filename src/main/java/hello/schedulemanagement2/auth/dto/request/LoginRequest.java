package hello.schedulemanagement2.auth.dto.request;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@AllArgsConstructor
@NoArgsConstructor
public class LoginRequest {

    @Email(message = "이메일 형식이 아닙니다.")
    @NotBlank(message = "이메일을 입력하세요.")
    private String email;

    @NotNull(message = "비밀번호를 입력하세요.")
    @Size(min = 1, max = 20, message = "비밀번호는 1글자 이상 20글자 이하여야 합니다.")
    private String password;
}
