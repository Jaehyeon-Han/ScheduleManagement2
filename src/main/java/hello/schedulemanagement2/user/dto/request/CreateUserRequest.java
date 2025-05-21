package hello.schedulemanagement2.user.dto.request;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class CreateUserRequest {

    @Email(message = "이메일 형식이 아닙니다.")
    @NotBlank(message = "이메일은 비어있을 수 없습니다.")
    private String email;

    @Size(min = 1, max = 20, message = "비밀번호는 1글자 이상 20글자 이하여야 합니다.")
    private String password;

    @Size(min = 2, max = 50, message = "이름은 2글자 이상 50글자 이하여야 합니다.")
    private String name;
}
