package hello.schedulemanagement2.user.dto.request;

import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@AllArgsConstructor // 테스트 사용
@NoArgsConstructor
public class DeleteUserRequest {
    @Size(min = 1, max = 20, message = "비밀번호는 1글자 이상 20글자 이하여야 합니다.")
    private String currentPassword;
}
