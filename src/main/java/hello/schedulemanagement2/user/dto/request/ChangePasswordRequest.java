package hello.schedulemanagement2.user.dto.request;

import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Setter
@Getter
@AllArgsConstructor // 테스트 사용
public class ChangePasswordRequest {

    @Size(min = 1, max = 20, message = "비밀번호는 1글자 이상 20글자 이하여야 합니다.")
    private String currentPassword;

    @Size(min = 1, max = 20, message = "비밀번호는 1글자 이상 20글자 이하여야 합니다.")
    private String newPassword;
}
